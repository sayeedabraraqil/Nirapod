package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.AuditLogEntity
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.DisputeEntity
import com.example.data.entity.ListingEntity
import com.example.data.entity.TimelineEventEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.WalletAccountEntity
import com.example.data.entity.WithdrawalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EscrowDao {

    // Listings
    @Query("SELECT * FROM listings ORDER BY id DESC")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :id")
    suspend fun getListingById(id: String): ListingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<ListingEntity>)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: String): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionByIdDirect(id: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getTransactionsByBuyer(buyerId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getTransactionsBySeller(sellerId: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    // Timeline Events
    @Query("SELECT * FROM timeline_events WHERE transactionId = :transactionId ORDER BY timestamp ASC, id ASC")
    fun getTimelineForTransaction(transactionId: String): Flow<List<TimelineEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvent(event: TimelineEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvents(events: List<TimelineEventEntity>)

    // Chat Messages
    @Query("SELECT * FROM chat_messages WHERE transactionId = :transactionId ORDER BY timestamp ASC")
    fun getChatMessagesForTransaction(transactionId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(messages: List<ChatMessageEntity>)

    // Disputes
    @Query("SELECT * FROM disputes ORDER BY createdAt DESC")
    fun getAllDisputes(): Flow<List<DisputeEntity>>

    @Query("SELECT * FROM disputes WHERE transactionId = :transactionId LIMIT 1")
    fun getDisputeForTransaction(transactionId: String): Flow<DisputeEntity?>

    @Query("SELECT * FROM disputes WHERE transactionId = :transactionId LIMIT 1")
    suspend fun getDisputeForTransactionDirect(transactionId: String): DisputeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDispute(dispute: DisputeEntity)

    @Update
    suspend fun updateDispute(dispute: DisputeEntity)

    // Wallet
    @Query("SELECT * FROM wallet_accounts WHERE userId = :userId LIMIT 1")
    fun getWalletForUser(userId: String): Flow<WalletAccountEntity?>

    @Query("SELECT * FROM wallet_accounts WHERE userId = :userId LIMIT 1")
    suspend fun getWalletForUserDirect(userId: String): WalletAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWallet(wallet: WalletAccountEntity)

    // Withdrawals
    @Query("SELECT * FROM withdrawals WHERE userId = :userId ORDER BY requestedAt DESC")
    fun getWithdrawalsForUser(userId: String): Flow<List<WithdrawalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: WithdrawalEntity)

    // Audit Logs
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)
}
