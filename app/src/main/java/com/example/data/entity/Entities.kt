package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CourierProvider
import com.example.data.model.DisputeReason
import com.example.data.model.DisputeStatus
import com.example.data.model.PaymentProvider
import com.example.data.model.ProductType
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionState

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey val id: String,
    val title: String,
    val titleBn: String,
    val description: String,
    val descriptionBn: String,
    val category: String,
    val categoryBn: String,
    val price: Double,
    val deliveryFee: Double,
    val protectionFee: Double,
    val productType: ProductType,
    val sellerId: String,
    val sellerName: String,
    val sellerPhone: String,
    val sellerRating: Double,
    val sellerCompletedOrders: Int,
    val sellerDisputeRate: Double,
    val sellerVerified: Boolean,
    val imageUrl: String,
    val digitalSecret: String = "",
    val returnWindowDays: Int = 3,
    val location: String = "Dhaka, Bangladesh"
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val listingId: String,
    val productTitle: String,
    val productTitleBn: String,
    val productType: ProductType,
    val buyerId: String,
    val buyerName: String,
    val buyerPhone: String,
    val sellerId: String,
    val sellerName: String,
    val sellerPhone: String,
    val price: Double,
    val deliveryFee: Double,
    val protectionFee: Double,
    val totalAmount: Double,
    val state: TransactionState,
    val paymentProvider: PaymentProvider,
    val paymentTxId: String,
    val paymentSecuredTime: Long,
    val courier: CourierProvider,
    val trackingNumber: String,
    val deliveryAddress: String,
    val digitalPayload: String = "",
    val digitalRevealedAt: Long = 0L,
    val buyerConfirmationDeadline: Long = 0L,
    val autoReleaseAt: Long = 0L,
    val fraudRiskScore: RiskLevel = RiskLevel.LOW,
    val returnShipmentCostLiability: String = "BUYER",
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "timeline_events")
data class TimelineEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: String,
    val eventTitle: String,
    val eventTitleBn: String,
    val description: String,
    val descriptionBn: String,
    val timestamp: Long,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: String,
    val senderId: String,
    val senderName: String,
    val isSystem: Boolean,
    val messageText: String,
    val timestamp: Long
)

@Entity(tableName = "disputes")
data class DisputeEntity(
    @PrimaryKey val id: String,
    val transactionId: String,
    val openedBy: String,
    val openedByName: String,
    val reason: DisputeReason,
    val description: String,
    val evidenceNotes: String,
    val sellerResponse: String = "",
    val sellerEvidenceNotes: String = "",
    val status: DisputeStatus,
    val resolutionNotes: String = "",
    val refundAmount: Double = 0.0,
    val createdAt: Long,
    val resolvedAt: Long = 0L
)

@Entity(tableName = "wallet_accounts")
data class WalletAccountEntity(
    @PrimaryKey val userId: String,
    val heldFunds: Double,
    val availableBalance: Double,
    val pendingPayout: Double,
    val totalEarnings: Double
)

@Entity(tableName = "withdrawals")
data class WithdrawalEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val amount: Double,
    val paymentMethod: String,
    val accountNumber: String,
    val status: String, // REQUESTED, PROCESSING, COMPLETED, FAILED
    val requestedAt: Long,
    val processedAt: Long = 0L
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: String,
    val action: String,
    val performedBy: String,
    val details: String,
    val timestamp: Long
)
