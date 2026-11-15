package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
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
import com.example.data.repository.EscrowRepository
import com.example.ui.localization.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole(val labelEn: String, val labelBn: String) {
    BUYER("Buyer Mode", "ক্রেতা মোড"),
    SELLER("Seller Mode", "বিক্রেতা মোড"),
    ADMIN("Platform Admin", "প্ল্যাটফর্ম অ্যাডমিন")
}

class EscrowViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EscrowRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = EscrowRepository(db.escrowDao())
    }

    // Language State
    private val _currentLanguage = MutableStateFlow(Language.BANGLA)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    // Role switcher for testing the complete ecosystem
    private val _currentRole = MutableStateFlow(UserRole.BUYER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    // Current User ID for Buyer / Seller
    val currentUserId = "buyer_current"
    val currentSellerId = "seller_techmatrix"

    // Listings Flow
    val allListings: StateFlow<List<ListingEntity>> = repository.allListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Transactions Flow
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Disputes Flow
    val allDisputes: StateFlow<List<DisputeEntity>> = repository.allDisputes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Audit Logs Flow
    val allAuditLogs: StateFlow<List<AuditLogEntity>> = repository.allAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wallet Flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentWallet: StateFlow<WalletAccountEntity?> = _currentRole.flatMapLatest { role ->
        val uid = if (role == UserRole.SELLER) currentSellerId else currentUserId
        repository.getWallet(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentWithdrawals: StateFlow<List<WithdrawalEntity>> = _currentRole.flatMapLatest { role ->
        val uid = if (role == UserRole.SELLER) currentSellerId else currentUserId
        repository.getWithdrawals(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Transaction Focus
    private val _selectedTransactionId = MutableStateFlow<String?>(null)
    val selectedTransactionId: StateFlow<String?> = _selectedTransactionId.asStateFlow()

    fun selectTransaction(id: String) {
        _selectedTransactionId.value = id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedTransaction: StateFlow<TransactionEntity?> = _selectedTransactionId.flatMapLatest { id ->
        if (id != null) repository.getTransaction(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedTimeline: StateFlow<List<TimelineEventEntity>> = _selectedTransactionId.flatMapLatest { id ->
        if (id != null) repository.getTimeline(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedChatMessages: StateFlow<List<ChatMessageEntity>> = _selectedTransactionId.flatMapLatest { id ->
        if (id != null) repository.getChatMessages(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedDispute: StateFlow<DisputeEntity?> = _selectedTransactionId.flatMapLatest { id ->
        if (id != null) repository.getDispute(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Revealed Digital Secret State
    private val _revealedKey = MutableStateFlow<String?>(null)
    val revealedKey: StateFlow<String?> = _revealedKey.asStateFlow()

    // Search and Category Filter
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")

    val filteredListings: StateFlow<List<ListingEntity>> = combine(
        allListings,
        searchQuery,
        selectedCategory
    ) { listings, query, category ->
        listings.filter { item ->
            val matchesCategory = category == "All" || item.category == category
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.titleBn.contains(query, ignoreCase = true) ||
                    item.sellerName.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Operations
    fun createProtectedOrder(
        listingId: String,
        buyerName: String,
        buyerPhone: String,
        deliveryAddress: String,
        courier: CourierProvider,
        paymentProvider: PaymentProvider,
        onCreated: (String) -> Unit
    ) {
        viewModelScope.launch {
            val tx = repository.createProtectedOrder(
                listingId = listingId,
                buyerId = currentUserId,
                buyerName = buyerName,
                buyerPhone = buyerPhone,
                deliveryAddress = deliveryAddress,
                courier = courier,
                paymentProvider = paymentProvider
            )
            _selectedTransactionId.value = tx.id
            onCreated(tx.id)
        }
    }

    fun verifyAndLockPayment(
        trxId: String,
        provider: PaymentProvider,
        gatewayTxId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.verifyAndLockPayment(trxId, provider, gatewayTxId)
            onSuccess()
        }
    }

    fun sellerAccept(trxId: String) {
        viewModelScope.launch {
            repository.sellerAcceptOrder(trxId)
        }
    }

    fun sellerDispatch(trxId: String, courier: CourierProvider, trackingNumber: String) {
        viewModelScope.launch {
            repository.sellerDispatchPhysical(trxId, courier, trackingNumber)
        }
    }

    fun markCourierDelivered(trxId: String) {
        viewModelScope.launch {
            repository.markCourierDelivered(trxId)
        }
    }

    fun buyerConfirmReceipt(trxId: String) {
        viewModelScope.launch {
            repository.buyerConfirmReceiptAndRelease(trxId)
        }
    }

    fun fileDispute(
        trxId: String,
        reason: DisputeReason,
        description: String,
        evidence: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val currentRoleVal = _currentRole.value
            val userName = if (currentRoleVal == UserRole.SELLER) "TechMatrix BD" else "Rahim Chowdhury"
            repository.openDispute(
                trxId = trxId,
                openedBy = currentUserId,
                openedByName = userName,
                reason = reason,
                description = description,
                evidenceNotes = evidence
            )
            onSuccess()
        }
    }

    fun respondToDispute(trxId: String, response: String, evidence: String) {
        viewModelScope.launch {
            repository.respondToDispute(trxId, response, evidence)
        }
    }

    fun adminResolveDispute(disputeId: String, resolutionStatus: DisputeStatus, notes: String) {
        viewModelScope.launch {
            repository.resolveDispute(disputeId, resolutionStatus, notes)
        }
    }

    fun revealKey(trxId: String) {
        viewModelScope.launch {
            val secret = repository.revealDigitalKey(trxId)
            _revealedKey.value = secret
        }
    }

    fun sendChatMessage(trxId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val role = _currentRole.value
            val (senderId, senderName) = when (role) {
                UserRole.BUYER -> Pair(currentUserId, "Rahim Chowdhury (Buyer)")
                UserRole.SELLER -> Pair(currentSellerId, "TechMatrix BD (Seller)")
                UserRole.ADMIN -> Pair("ADMIN", "Nirapod Dispute Desk (Staff)")
            }
            repository.sendChatMessage(trxId, senderId, senderName, text)
        }
    }

    fun createListing(
        title: String,
        titleBn: String,
        description: String,
        category: String,
        price: Double,
        deliveryFee: Double,
        productType: ProductType,
        digitalSecret: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val id = repository.createListing(
                title = title,
                titleBn = titleBn,
                description = description,
                descriptionBn = description,
                category = category,
                categoryBn = category,
                price = price,
                deliveryFee = deliveryFee,
                productType = productType,
                sellerId = currentSellerId,
                sellerName = "TechMatrix BD",
                sellerPhone = "+8801711223344",
                digitalSecret = digitalSecret,
                returnDays = 3,
                location = "Dhaka, Bangladesh"
            )
            onSuccess(id)
        }
    }

    fun requestWithdrawal(amount: Double, method: String, account: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = repository.requestWithdrawal(currentSellerId, amount, method, account)
            onResult(res)
        }
    }
}
