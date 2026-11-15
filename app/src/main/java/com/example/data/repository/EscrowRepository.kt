package com.example.data.repository

import com.example.data.dao.EscrowDao
import com.example.data.entity.AuditLogEntity
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.DisputeEntity
import com.example.data.entity.ListingEntity
import com.example.data.entity.TimelineEventEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.WalletAccountEntity
import com.example.data.entity.WithdrawalEntity
import com.example.data.model.CourierProvider
import com.example.data.model.DisputeReason
import com.example.data.model.DisputeStatus
import com.example.data.model.PaymentProvider
import com.example.data.model.ProductType
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionState
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class EscrowRepository(private val dao: EscrowDao) {

    val allListings: Flow<List<ListingEntity>> = dao.getAllListings()
    val allTransactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()
    val allDisputes: Flow<List<DisputeEntity>> = dao.getAllDisputes()
    val allAuditLogs: Flow<List<AuditLogEntity>> = dao.getAllAuditLogs()

    fun getTransaction(id: String): Flow<TransactionEntity?> = dao.getTransactionById(id)
    fun getTimeline(id: String): Flow<List<TimelineEventEntity>> = dao.getTimelineForTransaction(id)
    fun getChatMessages(id: String): Flow<List<ChatMessageEntity>> = dao.getChatMessagesForTransaction(id)
    fun getDispute(trxId: String): Flow<DisputeEntity?> = dao.getDisputeForTransaction(trxId)
    fun getWallet(userId: String): Flow<WalletAccountEntity?> = dao.getWalletForUser(userId)
    fun getWithdrawals(userId: String): Flow<List<WithdrawalEntity>> = dao.getWithdrawalsForUser(userId)

    suspend fun createListing(
        title: String,
        titleBn: String,
        description: String,
        descriptionBn: String,
        category: String,
        categoryBn: String,
        price: Double,
        deliveryFee: Double,
        productType: ProductType,
        sellerId: String,
        sellerName: String,
        sellerPhone: String,
        digitalSecret: String,
        returnDays: Int,
        location: String
    ): String {
        val id = "LIST-" + System.currentTimeMillis().toString().takeLast(6)
        val protectionFee = (price * 0.015).coerceAtLeast(30.0)
        val listing = ListingEntity(
            id = id,
            title = title,
            titleBn = titleBn.ifBlank { title },
            description = description,
            descriptionBn = descriptionBn.ifBlank { description },
            category = category,
            categoryBn = categoryBn,
            price = price,
            deliveryFee = deliveryFee,
            protectionFee = protectionFee,
            productType = productType,
            sellerId = sellerId,
            sellerName = sellerName,
            sellerPhone = sellerPhone,
            sellerRating = 5.0,
            sellerCompletedOrders = 0,
            sellerDisputeRate = 0.0,
            sellerVerified = true,
            imageUrl = if (productType == ProductType.PHYSICAL) "ic_box" else "ic_key",
            digitalSecret = digitalSecret,
            returnWindowDays = returnDays,
            location = location
        )
        dao.insertListing(listing)
        return id
    }

    suspend fun createProtectedOrder(
        listingId: String,
        buyerId: String,
        buyerName: String,
        buyerPhone: String,
        deliveryAddress: String,
        courier: CourierProvider,
        paymentProvider: PaymentProvider
    ): TransactionEntity {
        val listing = dao.getListingById(listingId)
            ?: throw IllegalArgumentException("Listing not found")
        val now = System.currentTimeMillis()
        val randomSuffix = (10000..99999).random()
        val trxId = "TRX-BD-$randomSuffix"

        val deliveryFee = if (listing.productType == ProductType.PHYSICAL) courier.defaultCharge else 0.0
        val protectionFee = (listing.price * 0.015).coerceAtLeast(30.0)
        val totalAmount = listing.price + deliveryFee + protectionFee

        val tx = TransactionEntity(
            id = trxId,
            listingId = listing.id,
            productTitle = listing.title,
            productTitleBn = listing.titleBn,
            productType = listing.productType,
            buyerId = buyerId,
            buyerName = buyerName,
            buyerPhone = buyerPhone,
            sellerId = listing.sellerId,
            sellerName = listing.sellerName,
            sellerPhone = listing.sellerPhone,
            price = listing.price,
            deliveryFee = deliveryFee,
            protectionFee = protectionFee,
            totalAmount = totalAmount,
            state = TransactionState.ORDER_CREATED,
            paymentProvider = paymentProvider,
            paymentTxId = "PENDING",
            paymentSecuredTime = 0L,
            courier = courier,
            trackingNumber = if (listing.productType == ProductType.PHYSICAL) "AWAITING_DISPATCH" else "DIGITAL_VAULT",
            deliveryAddress = deliveryAddress,
            digitalPayload = listing.digitalSecret,
            digitalRevealedAt = 0L,
            buyerConfirmationDeadline = 0L,
            autoReleaseAt = 0L,
            fraudRiskScore = RiskLevel.LOW,
            createdAt = now,
            updatedAt = now
        )
        dao.insertTransaction(tx)

        // Add initial timeline event
        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Order Created with Escrow Protection",
                eventTitleBn = "এসক্রো প্রোটেকশন সহ অর্ডার তৈরি হয়েছে",
                description = "Order initialized. Awaiting secure payment gateway authorization.",
                descriptionBn = "অর্ডার প্রস্তুত হয়েছে। পেমেন্ট গেটওয়ের অনুমোদন অপেক্ষমান।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = trxId,
                action = "ORDER_INITIATED",
                performedBy = buyerName,
                details = "Order placed for ${listing.title} (৳$totalAmount). Escrow contract initialized.",
                timestamp = now
            )
        )

        return tx
    }

    suspend fun verifyAndLockPayment(
        trxId: String,
        provider: PaymentProvider,
        gatewayTxId: String
    ): TransactionEntity {
        val tx = dao.getTransactionByIdDirect(trxId)
            ?: throw IllegalArgumentException("Transaction not found")
        val now = System.currentTimeMillis()

        val updatedTx = tx.copy(
            state = TransactionState.FUNDS_HELD,
            paymentProvider = provider,
            paymentTxId = gatewayTxId,
            paymentSecuredTime = now,
            updatedAt = now
        )
        dao.updateTransaction(updatedTx)

        // Update seller's held funds
        val sellerWallet = dao.getWalletForUserDirect(tx.sellerId) ?: WalletAccountEntity(
            userId = tx.sellerId,
            heldFunds = 0.0,
            availableBalance = 0.0,
            pendingPayout = 0.0,
            totalEarnings = 0.0
        )
        dao.insertOrUpdateWallet(
            sellerWallet.copy(heldFunds = sellerWallet.heldFunds + tx.price)
        )

        // Add timeline events
        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Payment Verified via ${provider.providerName}",
                eventTitleBn = "${provider.providerName}-এর মাধ্যমে পেমেন্ট যাচাই সম্পন্ন",
                description = "Bank/MFS webhook confirmed receipt of ৳${tx.totalAmount} (TxID: $gatewayTxId).",
                descriptionBn = "গেটওয়ে মারফত ৳${tx.totalAmount} প্রাপ্তি নিশ্চিত হয়েছে (TxID: $gatewayTxId)।",
                timestamp = now,
                isCompleted = true,
                isCurrent = false
            )
        )

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Funds Secured by Nirapod",
                eventTitleBn = "নিরাপদে ফান্ড সুরক্ষিত",
                description = "Funds are legally held by platform. Seller will NOT receive payment until delivery conditions are verified.",
                descriptionBn = "প্ল্যাটফর্ম ফান্ড সুরক্ষিত রেখেছে। ডেলিভারি শর্ত পূরণ না হওয়া পর্যন্ত বিক্রেতা কোনো অর্থ পাবেন না।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        // System notification in chat
        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "🔒 Nirapod Protection Active: ৳${tx.totalAmount} secured via ${provider.providerName}. Seller notified to fulfill order.",
                timestamp = now
            )
        )

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = trxId,
                action = "PAYMENT_LOCKED_IN_ESCROW",
                performedBy = "GATEWAY_SERVICE_${provider.name}",
                details = "Transaction amount ৳${tx.totalAmount} locked. Seller payment withheld.",
                timestamp = now
            )
        )

        // If digital product, auto-transition to review period or ready for reveal
        if (tx.productType == ProductType.DIGITAL_KEY || tx.productType == ProductType.DIGITAL_FILE) {
            val digitalTx = updatedTx.copy(
                state = TransactionState.BUYER_CONFIRMATION_PENDING,
                buyerConfirmationDeadline = now + 86400_000L,
                autoReleaseAt = now + 86400_000L,
                updatedAt = now
            )
            dao.updateTransaction(digitalTx)

            dao.insertTimelineEvent(
                TimelineEventEntity(
                    transactionId = trxId,
                    eventTitle = "Digital Delivery Ready",
                    eventTitleBn = "ডিজিটাল সরবরাহ প্রস্তুত",
                    description = "Secure license key is now available in your buyer portal. 24-hour review countdown initiated.",
                    descriptionBn = "নিরাপদ লাইসেন্স কি সিস্টেমে উন্মুক্ত হয়েছে। ২৪ ঘণ্টার নিরীক্ষা সময় শুরু।",
                    timestamp = now,
                    isCompleted = true,
                    isCurrent = true
                )
            )

            dao.insertChatMessage(
                ChatMessageEntity(
                    transactionId = trxId,
                    senderId = "SYSTEM",
                    senderName = "Nirapod Escrow Bot",
                    isSystem = true,
                    messageText = "⚡ Digital Asset Ready: You can now view and verify your digital license key securely.",
                    timestamp = now
                )
            )
            return digitalTx
        }

        return updatedTx
    }

    suspend fun sellerAcceptOrder(trxId: String) {
        val tx = dao.getTransactionByIdDirect(trxId) ?: return
        val now = System.currentTimeMillis()
        val updated = tx.copy(state = TransactionState.SELLER_ACCEPTED, updatedAt = now)
        dao.updateTransaction(updated)

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Seller Accepted Order",
                eventTitleBn = "বিক্রেতা অর্ডার গ্রহণ করেছেন",
                description = "${tx.sellerName} accepted order and is packaging for courier handover.",
                descriptionBn = "${tx.sellerName} অর্ডার গ্রহণ করেছেন এবং প্যাকিং প্রস্তুত করছেন।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "📦 Order Accepted: Seller has confirmed item preparation.",
                timestamp = now
            )
        )
    }

    suspend fun sellerDispatchPhysical(
        trxId: String,
        courier: CourierProvider,
        trackingNumber: String
    ) {
        val tx = dao.getTransactionByIdDirect(trxId) ?: return
        val now = System.currentTimeMillis()
        val updated = tx.copy(
            state = TransactionState.SHIPPED,
            courier = courier,
            trackingNumber = trackingNumber,
            updatedAt = now
        )
        dao.updateTransaction(updated)

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Dispatched via ${courier.providerName}",
                eventTitleBn = "${courier.providerName}-এর মাধ্যমে প্রেরিত",
                description = "Tracking Consignment: $trackingNumber. Expected transit: 24-48 hours.",
                descriptionBn = "ট্র্যাকিং কনসাইনমেন্ট: $trackingNumber। সম্ভাব্য ডেলিভারি সময়: ২৪-৪৮ ঘণ্টা।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "🚚 Shipment Update: Handed over to ${courier.providerName}. Consignment #$trackingNumber.",
                timestamp = now
            )
        )

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = trxId,
                action = "PARCEL_DISPATCHED",
                performedBy = tx.sellerName,
                details = "Courier: ${courier.name}, Consignment: $trackingNumber",
                timestamp = now
            )
        )
    }

    suspend fun markCourierDelivered(trxId: String) {
        val tx = dao.getTransactionByIdDirect(trxId) ?: return
        val now = System.currentTimeMillis()
        val updated = tx.copy(
            state = TransactionState.BUYER_CONFIRMATION_PENDING,
            buyerConfirmationDeadline = now + (48 * 3600_000L),
            autoReleaseAt = now + (48 * 3600_000L),
            updatedAt = now
        )
        dao.updateTransaction(updated)

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Delivered to Doorstep",
                eventTitleBn = "ঠিকানায় ডেলিভারি সম্পন্ন",
                description = "Courier confirms delivery at buyer address. 48-Hour Inspection Window begins.",
                descriptionBn = "কুরিয়ার ডেলিভারি নিশ্চিত করেছে। ৪৮ ঘণ্টার যাচাইকরণ সময় গণনা শুরু।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "🎉 Parcel Delivered: Please inspect your item and tap 'Confirm Receipt' to release funds to the seller.",
                timestamp = now
            )
        )
    }

    suspend fun buyerConfirmReceiptAndRelease(trxId: String) {
        val tx = dao.getTransactionByIdDirect(trxId) ?: return
        val now = System.currentTimeMillis()

        val updated = tx.copy(
            state = TransactionState.COMPLETED,
            updatedAt = now
        )
        dao.updateTransaction(updated)

        // Release funds from held to available balance in seller wallet
        val sellerWallet = dao.getWalletForUserDirect(tx.sellerId)
        if (sellerWallet != null) {
            val newHeld = (sellerWallet.heldFunds - tx.price).coerceAtLeast(0.0)
            val newAvailable = sellerWallet.availableBalance + tx.price
            val newEarnings = sellerWallet.totalEarnings + tx.price
            dao.insertOrUpdateWallet(
                sellerWallet.copy(
                    heldFunds = newHeld,
                    availableBalance = newAvailable,
                    totalEarnings = newEarnings
                )
            )
        }

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Buyer Confirmed Satisfaction & Received",
                eventTitleBn = "ক্রেতা পণ্য গ্রহণ ও সন্তুষ্টি নিশ্চিত করেছেন",
                description = "Buyer verified product condition. Escrow conditions satisfied.",
                descriptionBn = "ক্রেতা পণ্যের সঠিকতা যাচাই করেছেন। এসক্রো শর্তাবলী বাস্তবায়িত।",
                timestamp = now,
                isCompleted = true,
                isCurrent = false
            )
        )

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Funds Released to Seller Wallet",
                eventTitleBn = "বিক্রেতার ওয়ালেটে ফান্ড রিলিজ সম্পন্ন",
                description = "৳${tx.price} credited to ${tx.sellerName}'s withdrawable balance. Transaction finished.",
                descriptionBn = "৳${tx.price} ${tx.sellerName}-এর উত্তোলনযোগ্য ব্যালেন্সে যুক্ত হয়েছে। লেনদেন সম্পন্ন।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "✅ Transaction Completed: Buyer confirmed satisfaction. ৳${tx.price} released to seller wallet.",
                timestamp = now
            )
        )

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = trxId,
                action = "ESCROW_FUNDS_RELEASED",
                performedBy = tx.buyerName,
                details = "Released ৳${tx.price} to ${tx.sellerId}. Platform retained ৳${tx.protectionFee} fee.",
                timestamp = now
            )
        )
    }

    suspend fun openDispute(
        trxId: String,
        openedBy: String,
        openedByName: String,
        reason: DisputeReason,
        description: String,
        evidenceNotes: String
    ): DisputeEntity {
        val tx = dao.getTransactionByIdDirect(trxId)
            ?: throw IllegalArgumentException("Transaction not found")
        val now = System.currentTimeMillis()

        // Freeze transaction
        val updatedTx = tx.copy(
            state = TransactionState.DISPUTED,
            updatedAt = now
        )
        dao.updateTransaction(updatedTx)

        val disputeId = "DSP-${now.toString().takeLast(6)}"
        val dispute = DisputeEntity(
            id = disputeId,
            transactionId = trxId,
            openedBy = openedBy,
            openedByName = openedByName,
            reason = reason,
            description = description,
            evidenceNotes = evidenceNotes,
            status = DisputeStatus.UNDER_REVIEW,
            createdAt = now
        )
        dao.insertDispute(dispute)

        dao.insertTimelineEvent(
            TimelineEventEntity(
                transactionId = trxId,
                eventTitle = "Dispute Filed: ${reason.titleEn}",
                eventTitleBn = "বিতর্ক দায়ের: ${reason.titleBn}",
                description = "Dispute #$disputeId raised. Escrow funds strictly FROZEN. Neither party can withdraw until resolved.",
                descriptionBn = "বিতর্ক #$disputeId উন্মুক্ত। এসক্রো ফান্ড সম্পূর্ণ স্থগিত করা হয়েছে।",
                timestamp = now,
                isCompleted = true,
                isCurrent = true
            )
        )

        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Dispute Desk",
                isSystem = true,
                messageText = "⚠️ Dispute Opened (${reason.titleEn}): Escrow payout frozen. Nirapod trust arbitrator assigned.",
                timestamp = now
            )
        )

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = trxId,
                action = "DISPUTE_RAISED",
                performedBy = openedByName,
                details = "Reason: ${reason.name}, Evidence: $evidenceNotes. Funds frozen.",
                timestamp = now
            )
        )

        return dispute
    }

    suspend fun respondToDispute(
        trxId: String,
        sellerResponse: String,
        sellerEvidence: String
    ) {
        val dispute = dao.getDisputeForTransactionDirect(trxId) ?: return
        val updated = dispute.copy(
            sellerResponse = sellerResponse,
            sellerEvidenceNotes = sellerEvidence,
            status = DisputeStatus.UNDER_REVIEW
        )
        dao.updateDispute(updated)

        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = "SYSTEM",
                senderName = "Nirapod Dispute Desk",
                isSystem = true,
                messageText = "📝 Seller submitted counter-evidence for dispute review.",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun resolveDispute(
        disputeId: String,
        resolutionStatus: DisputeStatus,
        notes: String
    ) {
        val dispute = dao.getAllDisputes() // we find from direct list
        val all = dao.getAllDisputes()
        // direct lookup helper
        val allList = dao.getDisputeForTransactionDirect(disputeId)
        val target = if (allList != null && allList.id == disputeId) allList else null
        // fallback query by trxId
        val tx = dao.getTransactionByIdDirect(target?.transactionId ?: disputeId) ?: return
        val now = System.currentTimeMillis()

        when (resolutionStatus) {
            DisputeStatus.RESOLVED_BUYER_REFUND -> {
                // Refund buyer, remove seller held funds
                val updatedTx = tx.copy(state = TransactionState.REFUNDED, updatedAt = now)
                dao.updateTransaction(updatedTx)

                val sellerWallet = dao.getWalletForUserDirect(tx.sellerId)
                if (sellerWallet != null) {
                    dao.insertOrUpdateWallet(
                        sellerWallet.copy(
                            heldFunds = (sellerWallet.heldFunds - tx.price).coerceAtLeast(0.0)
                        )
                    )
                }

                dao.insertTimelineEvent(
                    TimelineEventEntity(
                        transactionId = tx.id,
                        eventTitle = "Dispute Resolved: Full Buyer Refund",
                        eventTitleBn = "বিতর্ক সমাধান: ক্রেতাকে সম্পূর্ণ রিফান্ড",
                        description = "Platform arbitration approved 100% refund of ৳${tx.totalAmount} to buyer. $notes",
                        descriptionBn = "ক্রেতার অনুকূলে সম্পূর্ণ ৳${tx.totalAmount} রিফান্ড অনুমোদিত। $notes",
                        timestamp = now,
                        isCompleted = true,
                        isCurrent = true
                    )
                )

                dao.insertChatMessage(
                    ChatMessageEntity(
                        transactionId = tx.id,
                        senderId = "SYSTEM",
                        senderName = "Nirapod Dispute Desk",
                        isSystem = true,
                        messageText = "🛡️ Arbitration Decision: Full refund granted to buyer. Funds returned to original payment source.",
                        timestamp = now
                    )
                )
            }
            DisputeStatus.RESOLVED_SELLER_RELEASE -> {
                // Release to seller
                val updatedTx = tx.copy(state = TransactionState.COMPLETED, updatedAt = now)
                dao.updateTransaction(updatedTx)

                val sellerWallet = dao.getWalletForUserDirect(tx.sellerId)
                if (sellerWallet != null) {
                    dao.insertOrUpdateWallet(
                        sellerWallet.copy(
                            heldFunds = (sellerWallet.heldFunds - tx.price).coerceAtLeast(0.0),
                            availableBalance = sellerWallet.availableBalance + tx.price,
                            totalEarnings = sellerWallet.totalEarnings + tx.price
                        )
                    )
                }

                dao.insertTimelineEvent(
                    TimelineEventEntity(
                        transactionId = tx.id,
                        eventTitle = "Dispute Resolved: Payment Released to Seller",
                        eventTitleBn = "বিতর্ক সমাধান: বিক্রেতাকে ফান্ড প্রদান",
                        description = "Seller fulfilled transaction obligations. ৳${tx.price} released. $notes",
                        descriptionBn = "বিক্রেতা শতভাগ সঠিক ডেলিভারি প্রমাণ উপস্থাপন করায় ৳${tx.price} রিলিজ করা হলো। $notes",
                        timestamp = now,
                        isCompleted = true,
                        isCurrent = true
                    )
                )
            }
            DisputeStatus.RESOLVED_SPLIT -> {
                // E.g. Intentional buyer refusal: seller receives delivery costs, buyer receives product price refund
                val updatedTx = tx.copy(state = TransactionState.REFUNDED, updatedAt = now)
                dao.updateTransaction(updatedTx)

                val sellerWallet = dao.getWalletForUserDirect(tx.sellerId)
                if (sellerWallet != null) {
                    dao.insertOrUpdateWallet(
                        sellerWallet.copy(
                            heldFunds = (sellerWallet.heldFunds - tx.price).coerceAtLeast(0.0),
                            availableBalance = sellerWallet.availableBalance + tx.deliveryFee
                        )
                    )
                }

                dao.insertTimelineEvent(
                    TimelineEventEntity(
                        transactionId = tx.id,
                        eventTitle = "Dispute Resolved: Delivery Cost Protection",
                        eventTitleBn = "বিতর্ক সমাধান: কুরিয়ার খরচ সুরক্ষা প্রদান",
                        description = "Seller awarded ৳${tx.deliveryFee} for courier loss due to buyer non-receipt. Balance refunded to buyer.",
                        descriptionBn = "অযৌক্তিক পার্সেল প্রত্যাখ্যানের কারণে বিক্রেতাকে কুরিয়ার খরচ প্রদান করা হলো। অবশিষ্ট ক্রেতাকে রিফান্ড।",
                        timestamp = now,
                        isCompleted = true,
                        isCurrent = true
                    )
                )
            }
            else -> {}
        }

        // Update dispute status if exists
        val currentDispute = dao.getDisputeForTransactionDirect(tx.id)
        if (currentDispute != null) {
            dao.updateDispute(
                currentDispute.copy(
                    status = resolutionStatus,
                    resolutionNotes = notes,
                    resolvedAt = now
                )
            )
        }

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = tx.id,
                action = "DISPUTE_ARBITRATED_${resolutionStatus.name}",
                performedBy = "PLATFORM_ADMIN_OPS",
                details = notes,
                timestamp = now
            )
        )
    }

    suspend fun revealDigitalKey(trxId: String): String {
        val tx = dao.getTransactionByIdDirect(trxId) ?: return ""
        val now = System.currentTimeMillis()
        if (tx.digitalRevealedAt == 0L) {
            dao.updateTransaction(tx.copy(digitalRevealedAt = now))
            dao.insertAuditLog(
                AuditLogEntity(
                    transactionId = trxId,
                    action = "DIGITAL_KEY_REVEALED",
                    performedBy = tx.buyerName,
                    details = "Key accessed and revealed in secure UI at $now",
                    timestamp = now
                )
            )
        }
        return tx.digitalPayload
    }

    suspend fun sendChatMessage(trxId: String, senderId: String, senderName: String, text: String) {
        dao.insertChatMessage(
            ChatMessageEntity(
                transactionId = trxId,
                senderId = senderId,
                senderName = senderName,
                isSystem = false,
                messageText = text,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun requestWithdrawal(userId: String, amount: Double, method: String, accountNumber: String): Boolean {
        val wallet = dao.getWalletForUserDirect(userId) ?: return false
        if (wallet.availableBalance < amount || amount < 500.0) {
            return false
        }
        val now = System.currentTimeMillis()
        val withdrawalId = "WTH-" + (1000..9999).random()

        dao.insertWithdrawal(
            WithdrawalEntity(
                id = withdrawalId,
                userId = userId,
                amount = amount,
                paymentMethod = "$method ($accountNumber)",
                accountNumber = accountNumber,
                status = "PROCESSING",
                requestedAt = now
            )
        )

        dao.insertOrUpdateWallet(
            wallet.copy(
                availableBalance = wallet.availableBalance - amount,
                pendingPayout = wallet.pendingPayout + amount
            )
        )

        dao.insertAuditLog(
            AuditLogEntity(
                transactionId = "WALLET-$userId",
                action = "WITHDRAWAL_REQUESTED",
                performedBy = userId,
                details = "Amount ৳$amount via $method ($accountNumber). Status: PROCESSING",
                timestamp = now
            )
        )
        return true
    }
}
