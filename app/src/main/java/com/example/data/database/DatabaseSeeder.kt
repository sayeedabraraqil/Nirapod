package com.example.data.database

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

object DatabaseSeeder {
    suspend fun seed(dao: EscrowDao) {
        val now = System.currentTimeMillis()
        val oneHour = 3600_000L
        val oneDay = 86400_000L

        // 1. Seed Listings
        val listings = listOf(
            ListingEntity(
                id = "LIST-101",
                title = "NVIDIA GeForce RTX 4070 Dual Fan (12GB GDDR6X)",
                titleBn = "এনভিডিয়া জিফোর্স আরটিএক্স ৪০৭০ ডুয়াল ফ্যান (১২জিবি)",
                description = "Brand new sealed box with 3 years official warranty. Tested and authentic. Shipped via Steadfast with insured packaging.",
                descriptionBn = "সম্পূর্ণ নতুন সিলযুক্ত বক্স, ৩ বছরের অফিশিয়াল ওয়ারেন্টি। স্টেডফাস্ট কুরিয়ারের মাধ্যমে সুরক্ষিত প্যাকেজিংয়ে পাঠানো হবে।",
                category = "Electronics & PC",
                categoryBn = "ইলেকট্রনিক্স ও পিসি",
                price = 68500.0,
                deliveryFee = 120.0,
                protectionFee = 1050.0,
                productType = ProductType.PHYSICAL,
                sellerId = "seller_techmatrix",
                sellerName = "TechMatrix BD",
                sellerPhone = "+8801711223344",
                sellerRating = 4.9,
                sellerCompletedOrders = 342,
                sellerDisputeRate = 0.3,
                sellerVerified = true,
                imageUrl = "ic_gpu",
                returnWindowDays = 3,
                location = "Multiplan Center, New Elephant Road, Dhaka"
            ),
            ListingEntity(
                id = "LIST-102",
                title = "JetBrains All Products Pack 1-Year Commercial License",
                titleBn = "জেটব্রেইনস অল প্রোডাক্টস প্যাক ১-বছরের কমার্শিয়াল লাইসেন্স",
                description = "Official JetBrains activation key for IntelliJ, PyCharm, WebStorm, Rider. Instant encrypted delivery via Nirapod upon payment lock.",
                descriptionBn = "অফিসিয়াল অ্যাক্টিভেশন কি। পেমেন্ট এসক্রোতে সুরক্ষিত হওয়া মাত্রই নিরাপদে তাৎক্ষণিক ডিক্রিপ্ট হবে।",
                category = "Software & Licenses",
                categoryBn = "সফটওয়্যার ও লাইসেন্স",
                price = 14500.0,
                deliveryFee = 0.0,
                protectionFee = 290.0,
                productType = ProductType.DIGITAL_KEY,
                sellerId = "seller_devstore",
                sellerName = "DevStore BD",
                sellerPhone = "+8801812345678",
                sellerRating = 5.0,
                sellerCompletedOrders = 520,
                sellerDisputeRate = 0.1,
                sellerVerified = true,
                imageUrl = "ic_jetbrains",
                digitalSecret = "JB-2026-BD982-LIC-X77Q-K981-AUTHENTIC",
                returnWindowDays = 1,
                location = "Online / Cloud Delivery"
            ),
            ListingEntity(
                id = "LIST-103",
                title = "Handloom Traditional Pure Jamdani Saree (84 Count)",
                titleBn = "ঐতিহ্যবাহী তাঁতের খাঁটি জামদানি শাড়ি (৮৪ কাউন্ট)",
                description = "Authentic hand-woven masterpiece from skilled weavers of Tangail. Pure cotton and silk blend with golden zari work.",
                descriptionBn = "টাঙ্গাইলের দক্ষ তাঁতিদের বোনা খাঁটি জামদানি শাড়ি। সুতি ও রেশমের মিশ্রণ ও সোনালি জরির কাজ।",
                category = "Fashion & Handloom",
                categoryBn = "ফ্যাশন ও তাঁতশিল্প",
                price = 9200.0,
                deliveryFee = 80.0,
                protectionFee = 180.0,
                productType = ProductType.PHYSICAL,
                sellerId = "seller_aarong_crafts",
                sellerName = "Aarong Artisan Collective",
                sellerPhone = "+8801919876543",
                sellerRating = 4.8,
                sellerCompletedOrders = 189,
                sellerDisputeRate = 0.5,
                sellerVerified = true,
                imageUrl = "ic_saree",
                returnWindowDays = 2,
                location = "Bajitpur, Tangail"
            ),
            ListingEntity(
                id = "LIST-104",
                title = "Fintech & E-Commerce Design System (Figma Professional)",
                titleBn = "ফিনটেক ও ই-কমার্স ডিজাইন সিস্টেম (ফিগমা প্রো)",
                description = "Over 450+ responsive components, tokenized design variables, dark/light modes, and full prototype interactions.",
                descriptionBn = "৪৫০+ রেসপন্সিভ কম্পোনেন্ট, ডার্ক/লাইট মোড এবং সম্পূর্ণ প্রোটোটাইপ ইন্টারঅ্যাকশন।",
                category = "Design Assets",
                categoryBn = "ডিজাইন অ্যাসেটস",
                price = 3800.0,
                deliveryFee = 0.0,
                protectionFee = 95.0,
                productType = ProductType.DIGITAL_FILE,
                sellerId = "seller_studiocraft",
                sellerName = "StudioCraft Dhaka",
                sellerPhone = "+8801611009988",
                sellerRating = 4.9,
                sellerCompletedOrders = 115,
                sellerDisputeRate = 0.0,
                sellerVerified = true,
                imageUrl = "ic_figma",
                digitalSecret = "https://nirapod.app/vault/figma-fintech-v2.9-source.fig",
                returnWindowDays = 2,
                location = "Banani, Dhaka"
            ),
            ListingEntity(
                id = "LIST-105",
                title = "Sony WH-1000XM5 Wireless Noise Cancelling Headphones",
                titleBn = "সোনি WH-1000XM5 ওয়্যারলেস নয়েজ ক্যানসেলিং হেডফোন",
                description = "Industry leading noise cancellation, silver edition, authentic box with serial number verification.",
                descriptionBn = "অরিজিনাল সোনি সিলভার এডিশন, অ্যাক্টিভ নয়েজ ক্যানসেলিং ও অথেন্টিক সিরিয়াল ভেরিফিকেশন।",
                category = "Audio & Electronics",
                categoryBn = "অডিও ও ইলেকট্রনিক্স",
                price = 34000.0,
                deliveryFee = 100.0,
                protectionFee = 550.0,
                productType = ProductType.PHYSICAL,
                sellerId = "seller_gadgetbd",
                sellerName = "GadgetBD Express",
                sellerPhone = "+8801755667788",
                sellerRating = 4.7,
                sellerCompletedOrders = 210,
                sellerDisputeRate = 1.2,
                sellerVerified = true,
                imageUrl = "ic_headphones",
                returnWindowDays = 3,
                location = "Jamuna Future Park, Dhaka"
            )
        )
        dao.insertListings(listings)

        // 2. Seed Transactions
        val trx1 = TransactionEntity(
            id = "TRX-BD-89214",
            listingId = "LIST-101",
            productTitle = "NVIDIA GeForce RTX 4070 Dual Fan (12GB GDDR6X)",
            productTitleBn = "এনভিডিয়া জিফোর্স আরটিএক্স ৪০৭০ ডুয়াল ফ্যান (১২জিবি)",
            productType = ProductType.PHYSICAL,
            buyerId = "buyer_current",
            buyerName = "Rahim Chowdhury",
            buyerPhone = "+8801712987654",
            sellerId = "seller_techmatrix",
            sellerName = "TechMatrix BD",
            sellerPhone = "+8801711223344",
            price = 68500.0,
            deliveryFee = 120.0,
            protectionFee = 1050.0,
            totalAmount = 69670.0,
            state = TransactionState.SHIPPED,
            paymentProvider = PaymentProvider.BKASH,
            paymentTxId = "BKH992014A89X",
            paymentSecuredTime = now - 28 * oneHour,
            courier = CourierProvider.STEADFAST,
            trackingNumber = "SFC-DH-9018274",
            deliveryAddress = "Flat 4B, Green Heritage, GEC Circle, Nasirabad, Chittagong",
            buyerConfirmationDeadline = now + 44 * oneHour,
            autoReleaseAt = now + 72 * oneHour,
            fraudRiskScore = RiskLevel.LOW,
            createdAt = now - 30 * oneHour,
            updatedAt = now - 8 * oneHour
        )

        val trx2 = TransactionEntity(
            id = "TRX-BD-44109",
            listingId = "LIST-102",
            productTitle = "JetBrains All Products Pack 1-Year Commercial License",
            productTitleBn = "জেটব্রেইনস অল প্রোডাক্টস প্যাক ১-বছরের কমার্শিয়াল লাইসেন্স",
            productType = ProductType.DIGITAL_KEY,
            buyerId = "buyer_current",
            buyerName = "Rahim Chowdhury",
            buyerPhone = "+8801712987654",
            sellerId = "seller_devstore",
            sellerName = "DevStore BD",
            sellerPhone = "+8801812345678",
            price = 14500.0,
            deliveryFee = 0.0,
            protectionFee = 290.0,
            totalAmount = 14790.0,
            state = TransactionState.BUYER_CONFIRMATION_PENDING,
            paymentProvider = PaymentProvider.NAGAD,
            paymentTxId = "NGD-88273910P",
            paymentSecuredTime = now - 12 * oneHour,
            courier = CourierProvider.DIGITAL_INSTANT,
            trackingNumber = "DIGITAL-VAULT-JB-44109",
            deliveryAddress = "rahim.chowdhury.dev@gmail.com",
            digitalPayload = "JB-2026-BD982-LIC-X77Q-K981-AUTHENTIC",
            digitalRevealedAt = now - 4 * oneHour,
            buyerConfirmationDeadline = now + 20 * oneHour,
            autoReleaseAt = now + 20 * oneHour,
            fraudRiskScore = RiskLevel.LOW,
            createdAt = now - 14 * oneHour,
            updatedAt = now - 4 * oneHour
        )

        val trx3 = TransactionEntity(
            id = "TRX-BD-77301",
            listingId = "LIST-105",
            productTitle = "Sony WH-1000XM5 Wireless Noise Cancelling Headphones",
            productTitleBn = "সোনি WH-1000XM5 ওয়্যারলেস নয়েজ ক্যানসেলিং হেডফোন",
            productType = ProductType.PHYSICAL,
            buyerId = "buyer_sadia",
            buyerName = "Sadia Afrin",
            buyerPhone = "+8801833445566",
            sellerId = "seller_gadgetbd",
            sellerName = "GadgetBD Express",
            sellerPhone = "+8801755667788",
            price = 34000.0,
            deliveryFee = 100.0,
            protectionFee = 550.0,
            totalAmount = 34650.0,
            state = TransactionState.DISPUTED,
            paymentProvider = PaymentProvider.CARD_GATEWAY,
            paymentTxId = "SSL-VISA-8831920",
            paymentSecuredTime = now - 48 * oneHour,
            courier = CourierProvider.PATHAO,
            trackingNumber = "PTH-EXP-7729104",
            deliveryAddress = "House 14, Road 7, Sector 3, Uttara, Dhaka",
            buyerConfirmationDeadline = 0L,
            autoReleaseAt = 0L,
            fraudRiskScore = RiskLevel.MEDIUM,
            createdAt = now - 52 * oneHour,
            updatedAt = now - 6 * oneHour
        )

        val trx4 = TransactionEntity(
            id = "TRX-BD-10293",
            listingId = "LIST-103",
            productTitle = "Handloom Traditional Pure Jamdani Saree (84 Count)",
            productTitleBn = "ঐতিহ্যবাহী তাঁতের খাঁটি জামদানি শাড়ি (৮৪ কাউন্ট)",
            productType = ProductType.PHYSICAL,
            buyerId = "buyer_current",
            buyerName = "Rahim Chowdhury",
            buyerPhone = "+8801712987654",
            sellerId = "seller_aarong_crafts",
            sellerName = "Aarong Artisan Collective",
            sellerPhone = "+8801919876543",
            price = 9200.0,
            deliveryFee = 80.0,
            protectionFee = 180.0,
            totalAmount = 9460.0,
            state = TransactionState.COMPLETED,
            paymentProvider = PaymentProvider.BKASH,
            paymentTxId = "BKH66778899Z",
            paymentSecuredTime = now - 5 * oneDay,
            courier = CourierProvider.PATHAO,
            trackingNumber = "PTH-DHK-5510294",
            deliveryAddress = "Dhanmondi 27, Dhaka",
            buyerConfirmationDeadline = now - 2 * oneDay,
            autoReleaseAt = now - 2 * oneDay,
            fraudRiskScore = RiskLevel.LOW,
            createdAt = now - 6 * oneDay,
            updatedAt = now - 2 * oneDay
        )

        dao.insertTransactions(listOf(trx1, trx2, trx3, trx4))

        // 3. Timeline Events for TRX-BD-89214
        val eventsTrx1 = listOf(
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Order Created",
                eventTitleBn = "অর্ডার তৈরি হয়েছে",
                description = "Buyer placed order with Nirapod Escrow protection request.",
                descriptionBn = "ক্রেতা নিরাপদ এসক্রো প্রোটেকশন সহ অর্ডার সম্পন্ন করেছেন।",
                timestamp = now - 30 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Payment Verified via bKash PGW",
                eventTitleBn = "বিকাশ পেমেন্ট গেটওয়ে যাচাই সম্পন্ন",
                description = "৳69,670 successfully authorized via server-to-server webhook verification.",
                descriptionBn = "সার্ভার-টু-সার্ভার ওয়েবহুকের মাধ্যমে ৳৬৯,৬৭০ অনুমোদিত হয়েছে।",
                timestamp = now - 28 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Funds Secured by Nirapod",
                eventTitleBn = "নিরাপদে ফান্ড লক করা হয়েছে",
                description = "Payment is strictly held by platform. Seller has NOT received payout.",
                descriptionBn = "ফান্ড নিরাপদে প্ল্যাটফর্ম হোল্ড করছে। বিক্রেতা এখনও টাকা পাননি।",
                timestamp = now - 28 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Seller Accepted Order",
                eventTitleBn = "বিক্রেতা অর্ডার গ্রহণ করেছেন",
                description = "TechMatrix BD confirmed stock availability and initiated packaging.",
                descriptionBn = "টেকম্যাট্রিক্স বিডি স্টক নিশ্চিত করে প্যাকেজিং শুরু করেছে।",
                timestamp = now - 25 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Dispatched via Steadfast Courier",
                eventTitleBn = "স্টেডফাস্ট কুরিয়ারে হস্তান্তর সম্পন্ন",
                description = "Consignment ID #SFC-DH-9018274 handed over at Dhaka Hub.",
                descriptionBn = "কনসাইনমেন্ট নং #SFC-DH-9018274 ঢাকা হাবে হস্তান্তর করা হয়েছে।",
                timestamp = now - 8 * oneHour,
                isCompleted = true,
                isCurrent = true
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Out for Delivery (GEC Chittagong)",
                eventTitleBn = "ডেলিভারির জন্য বের হবে",
                description = "Courier agent will attempt doorstep delivery.",
                descriptionBn = "কুরিয়ার প্রতিনিধি গ্রাহকের ঠিকানায় পৌঁছাবেন।",
                timestamp = now + 12 * oneHour,
                isCompleted = false,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-89214",
                eventTitle = "Buyer Inspection & Confirmation",
                eventTitleBn = "ক্রেতার পরিদর্শন ও অনুমোদন",
                description = "48-hour inspection period begins upon delivery. Funds release upon buyer confirmation.",
                descriptionBn = "ডেলিভারির পর ৪৮ ঘণ্টার যাচাই সময় শুরু হবে। ক্রেতার অনুমোদনের পরই টাকা রিলিজ হবে।",
                timestamp = now + 48 * oneHour,
                isCompleted = false,
                isCurrent = false
            )
        )
        dao.insertTimelineEvents(eventsTrx1)

        // 4. Timeline Events for TRX-BD-44109 (Digital Key)
        val eventsTrx2 = listOf(
            TimelineEventEntity(
                transactionId = "TRX-BD-44109",
                eventTitle = "Order Created",
                eventTitleBn = "অর্ডার তৈরি হয়েছে",
                description = "Buyer ordered JetBrains All Products Commercial License.",
                descriptionBn = "ক্রেতা জেটব্রেইনস লাইসেন্স কি অর্ডার করেছেন।",
                timestamp = now - 14 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-44109",
                eventTitle = "Payment Secured by Nirapod",
                eventTitleBn = "নিরাপদে ফান্ড সুরক্ষিত",
                description = "৳14,790 secured via Nagad PGW. Encrypted delivery locker unlocked.",
                descriptionBn = "নগদ গেটওয়ে মারফত ৳১৪,৭৯০ সুরক্ষিত হয়েছে। এনক্রিপ্টেড ডিজিটাল লকার প্রস্তুত।",
                timestamp = now - 12 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-44109",
                eventTitle = "Digital Secret Delivered & Logged",
                eventTitleBn = "ডিজিটাল কি সরবরাহ সম্পন্ন",
                description = "License key revealed to buyer with tamper-proof timestamp log.",
                descriptionBn = "টাইমস্ট্যাম্প লগ সহ ক্রেতার জন্য লাইসেন্স কি ডিক্রিপ্ট করা হয়েছে।",
                timestamp = now - 4 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-44109",
                eventTitle = "Buyer 24-Hour Review Period Active",
                eventTitleBn = "২৪ ঘণ্টার যাচাই সময় চলছে",
                description = "Test your license activation. Tap 'Confirm Receipt' to release seller payment.",
                descriptionBn = "লাইসেন্স চালু করে পরীক্ষা করুন। বিক্রেতাকে পেমেন্ট রিলিজ করতে অনুমোদন দিন।",
                timestamp = now,
                isCompleted = false,
                isCurrent = true
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-44109",
                eventTitle = "Auto-Release / Seller Payout",
                eventTitleBn = "স্বয়ংক্রিয় রিলিজ বা পে-আউট",
                description = "If no dispute is filed within deadline, funds auto-release to seller wallet.",
                descriptionBn = "নির্দিষ্ট সময়ে কোনো অভিযোগ না আসলে স্বয়ংক্রিয়ভাবে বিক্রেতার একাউন্টে যুক্ত হবে।",
                timestamp = now + 20 * oneHour,
                isCompleted = false,
                isCurrent = false
            )
        )
        dao.insertTimelineEvents(eventsTrx2)

        // 5. Timeline Events for TRX-BD-77301 (Disputed Case)
        val eventsTrx3 = listOf(
            TimelineEventEntity(
                transactionId = "TRX-BD-77301",
                eventTitle = "Payment Secured in Escrow",
                eventTitleBn = "এসক্রোতে পেমেন্ট সুরক্ষিত",
                description = "৳34,650 secured via Visa Card.",
                descriptionBn = "ভিসা কার্ডের মাধ্যমে ৳৩৪,৬৫০ সুরক্ষিত।",
                timestamp = now - 48 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-77301",
                eventTitle = "Delivered via Pathao",
                eventTitleBn = "পাঠাও এর মাধ্যমে ডেলিভারি সম্পন্ন",
                description = "Parcel received at buyer address.",
                descriptionBn = "ক্রেতার ঠিকানায় পার্সেল গ্রহণ করা হয়েছে।",
                timestamp = now - 12 * oneHour,
                isCompleted = true,
                isCurrent = false
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-77301",
                eventTitle = "Dispute Filed: Shipping Damage Claim",
                eventTitleBn = "বিতর্ক দায়ের: পণ্য ক্ষতিগ্রস্ত দাবি",
                description = "Buyer reported cracked earcup and crushed packaging. Escrow payout FROZEN.",
                descriptionBn = "ক্রেতা ক্ষতিগ্রস্ত ইয়ারকাপের অভিযোগ করেছেন। বিক্রেতার পেআউট সাময়িক স্থগিত।",
                timestamp = now - 6 * oneHour,
                isCompleted = true,
                isCurrent = true
            ),
            TimelineEventEntity(
                transactionId = "TRX-BD-77301",
                eventTitle = "Arbitration & Platform Review",
                eventTitleBn = "প্ল্যাটফর্ম আরবিট্রেশন ও তদন্ত",
                description = "Platform operations reviewing courier handover photos & unboxing video.",
                descriptionBn = "কুরিয়ার হ্যান্ডওভার রেকর্ড ও আনবক্সিং প্রমাণ প্ল্যাটফর্ম পর্যালোচনা করছে।",
                timestamp = now - 2 * oneHour,
                isCompleted = false,
                isCurrent = false
            )
        )
        dao.insertTimelineEvents(eventsTrx3)

        // 6. Dispute Entity for TRX-BD-77301
        val dispute1 = DisputeEntity(
            id = "DSP-2026-991",
            transactionId = "TRX-BD-77301",
            openedBy = "buyer_sadia",
            openedByName = "Sadia Afrin",
            reason = DisputeReason.DAMAGED,
            description = "The package arrived with severe exterior crushing. Upon opening, the right swivel hinge of the Sony WH-1000XM5 headphone is cracked and audio is buzzing.",
            evidenceNotes = "Attached 3 unboxing photos showing damaged courier bubble-wrap and serial number match.",
            sellerResponse = "We shipped the parcel in double-layered bubble wrap with Fragile stickers. The damage clearly occurred during Pathao transit. We request courier liability coverage.",
            sellerEvidenceNotes = "Pre-dispatch CCTV capture #PKG-8821 available.",
            status = DisputeStatus.UNDER_REVIEW,
            resolutionNotes = "Nirapod dispute specialist examining Pathao transit insurance and seller packaging standards.",
            refundAmount = 0.0,
            createdAt = now - 6 * oneHour,
            resolvedAt = 0L
        )
        dao.insertDispute(dispute1)

        // 7. Seed Chat Messages
        val chatsTrx1 = listOf(
            ChatMessageEntity(
                transactionId = "TRX-BD-89214",
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "🔒 Escrow Protection Active: Buyer payment of ৳69,670 has been verified and securely locked with Nirapod. Seller has been authorized to dispatch.",
                timestamp = now - 28 * oneHour
            ),
            ChatMessageEntity(
                transactionId = "TRX-BD-89214",
                senderId = "seller_techmatrix",
                senderName = "TechMatrix BD (Seller)",
                isSystem = false,
                messageText = "Assalamu Alaikum Rahim bhai! We have verified the serial number (SN: 90281-RTX-BD) and packed it with antistatic bubble padding. Handing over to Steadfast now.",
                timestamp = now - 24 * oneHour
            ),
            ChatMessageEntity(
                transactionId = "TRX-BD-89214",
                senderId = "buyer_current",
                senderName = "Rahim Chowdhury (Buyer)",
                isSystem = false,
                messageText = "Walaikum Assalam. Thanks! Please ensure Steadfast marks it as high-value Fragile cargo so it is handled carefully.",
                timestamp = now - 23 * oneHour
            ),
            ChatMessageEntity(
                transactionId = "TRX-BD-89214",
                senderId = "SYSTEM",
                senderName = "Nirapod Escrow Bot",
                isSystem = true,
                messageText = "🚚 Shipment Update: Consignment #SFC-DH-9018274 picked up by Steadfast Courier Dhaka Hub. Destination: Chittagong.",
                timestamp = now - 8 * oneHour
            )
        )
        dao.insertChatMessages(chatsTrx1)

        // 8. Seed Seller Wallet & Withdrawals
        val wallet = WalletAccountEntity(
            userId = "seller_techmatrix",
            heldFunds = 68500.0,
            availableBalance = 142800.0,
            pendingPayout = 25000.0,
            totalEarnings = 482500.0
        )
        dao.insertOrUpdateWallet(wallet)

        val buyerWallet = WalletAccountEntity(
            userId = "buyer_current",
            heldFunds = 84460.0,
            availableBalance = 5200.0,
            pendingPayout = 0.0,
            totalEarnings = 0.0
        )
        dao.insertOrUpdateWallet(buyerWallet)

        val withdrawals = listOf(
            WithdrawalEntity(
                id = "WTH-9921",
                userId = "seller_techmatrix",
                amount = 45000.0,
                paymentMethod = "bKash Merchant (+8801711223344)",
                accountNumber = "+8801711223344",
                status = "COMPLETED",
                requestedAt = now - 3 * oneDay,
                processedAt = now - 3 * oneDay + 2 * oneHour
            ),
            WithdrawalEntity(
                id = "WTH-9922",
                userId = "seller_techmatrix",
                amount = 25000.0,
                paymentMethod = "City Bank Account (A/C: 110293819001)",
                accountNumber = "110293819001",
                status = "PROCESSING",
                requestedAt = now - 5 * oneHour,
                processedAt = 0L
            )
        )
        for (w in withdrawals) {
            dao.insertWithdrawal(w)
        }

        // 9. Seed Audit Logs
        val auditLogs = listOf(
            AuditLogEntity(
                transactionId = "TRX-BD-89214",
                action = "PAYMENT_SECURED",
                performedBy = "GATEWAY_WEBHOOK_BKASH",
                details = "Authorized ৳69,670. Funds segregated into Escrow Holding Account #BD-ESC-01.",
                timestamp = now - 28 * oneHour
            ),
            AuditLogEntity(
                transactionId = "TRX-BD-89214",
                action = "COURIER_CONSIGNMENT_LINKED",
                performedBy = "SELLER_TECHMATRIX",
                details = "Courier: STEADFAST, Tracking: SFC-DH-9018274",
                timestamp = now - 8 * oneHour
            ),
            AuditLogEntity(
                transactionId = "TRX-BD-77301",
                action = "DISPUTE_FILED_PAYMENT_FROZEN",
                performedBy = "BUYER_SADIA",
                details = "Reason: DAMAGED. Automatic freeze placed on seller payout pending resolution.",
                timestamp = now - 6 * oneHour
            )
        )
        for (log in auditLogs) {
            dao.insertAuditLog(log)
        }
    }
}
