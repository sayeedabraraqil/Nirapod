package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourierProvider
import com.example.data.model.PaymentProvider
import com.example.data.model.ProductType
import com.example.ui.components.FinancialBreakdownCard
import com.example.ui.components.NirapodCard
import com.example.ui.components.NirapodTextField
import com.example.ui.components.TrustShieldBanner
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatBdt
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    listingId: String,
    viewModel: EscrowViewModel,
    onBackClick: () -> Unit,
    onOrderComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val listings by viewModel.allListings.collectAsState()
    val listing = listings.find { it.id == listingId }

    if (listing == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F8FE)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Listing not found",
                fontFamily = AnekBangla,
                fontSize = 16.sp
            )
        }
        return
    }

    var buyerName by remember { mutableStateOf("আব্দুর রহিম চৌধুরী") }
    var buyerPhone by remember { mutableStateOf("+8801712987654") }
    var deliveryAddress by remember {
        mutableStateOf(
            if (listing.productType == ProductType.PHYSICAL)
                "ফ্ল্যাট ৪বি, গ্রীন হেরিটেজ, জিইসি মোড়, চট্টগ্রাম"
            else "rahim.chowdhury.dev@gmail.com"
        )
    }
    var selectedCourier by remember { mutableStateOf(CourierProvider.STEADFAST) }
    var selectedProvider by remember { mutableStateOf(PaymentProvider.BKASH) }

    val deliveryFee = if (listing.productType == ProductType.PHYSICAL) selectedCourier.defaultCharge else 0.0
    val totalAmount = listing.price + deliveryFee + listing.protectionFee

    var isSubmitting by remember { mutableStateOf(false) }
    var showPaymentSimulatorDialog by remember { mutableStateOf(false) }
    var activeCreatedTrxId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (lang == Language.BANGLA) "সুরক্ষিত এসক্রো চেকআউট" else "Protected Escrow Checkout",
                        fontFamily = AnekBangla,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimaryLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 12.dp,
                color = Color.White,
                border = BorderStroke(1.dp, BlueBorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = AppStrings.totalPayable(lang),
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            color = TextMutedLight
                        )
                        Text(
                            text = formatBdt(totalAmount),
                            fontFamily = AnekBangla,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BluePrimary
                        )
                    }

                    Button(
                        onClick = {
                            isSubmitting = true
                            viewModel.createProtectedOrder(
                                listingId = listing.id,
                                buyerName = buyerName,
                                buyerPhone = buyerPhone,
                                deliveryAddress = deliveryAddress,
                                courier = if (listing.productType == ProductType.PHYSICAL) selectedCourier else CourierProvider.DIGITAL_INSTANT,
                                paymentProvider = selectedProvider
                            ) { newTrxId ->
                                isSubmitting = false
                                activeCreatedTrxId = newTrxId
                                showPaymentSimulatorDialog = true
                            }
                        },
                        enabled = !isSubmitting && buyerName.isNotBlank() && deliveryAddress.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("pay_into_escrow_button")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (lang == Language.BANGLA) "নিরাপদ সুরক্ষায় পেমেন্ট করুন" else "Pay with Nirapod Protection",
                                fontFamily = AnekBangla,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        WarmDecorativeBackground(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Trust Shield
                TrustShieldBanner(
                    title = if (lang == Language.BANGLA) "আপনার অর্থ নিরাপদ সুরক্ষায় থাকবে" else "Nirapod Escrow Protection",
                    subtitle = if (lang == Language.BANGLA)
                        "আপনার পেমেন্ট নিরাপদ প্ল্যাটফর্মের ট্রাস্ট অ্যাকাউন্টে সাময়িকভাবে জমা থাকবে। পণ্য রিসিভ করে অনুমোদন না দেওয়া পর্যন্ত বিক্রেতা কোনো টাকা পাবে না।"
                        else "Your funds are held in trust. The seller will NOT receive money until you approve after delivery."
                )

                // Buyer Information Card
                NirapodCard(
                    backgroundColor = Color.White,
                    borderColor = BlueBorderLight
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "ক্রেতার তথ্য ও গন্তব্য" else "Buyer & Destination Details",
                        fontFamily = AnekBangla,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = buyerName,
                        onValueChange = { buyerName = it },
                        label = { Text(if (lang == Language.BANGLA) "ক্রেতার পূর্ণ নাম" else "Buyer Full Name", fontFamily = AnekBangla) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = buyerPhone,
                        onValueChange = { buyerPhone = it },
                        label = { Text(if (lang == Language.BANGLA) "মোবাইল নম্বর" else "Contact Phone Number", fontFamily = AnekBangla) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        label = {
                            Text(
                                text = if (listing.productType == ProductType.PHYSICAL)
                                    (if (lang == Language.BANGLA) "ডেলিভারি ঠিকানা (বাংলাদেশ)" else "Shipping Address in Bangladesh")
                                else (if (lang == Language.BANGLA) "ডিজিটাল ডেলিভারি ইমেইল" else "Digital Delivery Email"),
                                fontFamily = AnekBangla
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )
                }

                // Courier Selection (Physical only)
                if (listing.productType == ProductType.PHYSICAL) {
                    NirapodCard(
                        backgroundColor = Color.White,
                        borderColor = BlueBorderLight
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "কুরিয়ার পার্টনার নির্বাচন করুন" else "Select Courier Partner",
                            fontFamily = AnekBangla,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        listOf(
                            CourierProvider.STEADFAST to ("Steadfast Courier (৳৭০) - দ্রুততম" to "সারা দেশে হোম ডেলিভারি"),
                            CourierProvider.PATHAO to ("Pathao Express (৳৮০) - নির্ভরযোগ্য" to "ঢাকা ও বিভাগীয় শহর"),
                            CourierProvider.REDX to ("RedX Logistics (৳৭৫) - বিস্তৃত নেটওয়ার্ক" to "দেশব্যাপী কভারেজ"),
                            CourierProvider.ECOURIER to ("eCourier BD (৳৮৫)" to "প্রিমিয়াম হ্যান্ডলিং")
                        ).forEach { (courier, labels) ->
                            val (title, sub) = labels
                            val isSelected = selectedCourier == courier

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) BlueContainerLight else Color.White,
                                border = BorderStroke(1.2.dp, if (isSelected) BluePrimary else BlueBorderLight),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedCourier = courier }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedCourier = courier },
                                        colors = RadioButtonDefaults.colors(selectedColor = BluePrimary)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = title,
                                            fontFamily = AnekBangla,
                                            fontSize = 13.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = TextPrimaryLight
                                        )
                                        Text(
                                            text = sub,
                                            fontFamily = AnekBangla,
                                            fontSize = 11.5.sp,
                                            color = TextMutedLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Payment Provider Selection
                NirapodCard(
                    backgroundColor = Color.White,
                    borderColor = BlueBorderLight
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "পেমেন্ট মাধ্যম (বাংলাদেশ গেটওয়ে)" else "Payment Method (Bangladesh Gateways)",
                        fontFamily = AnekBangla,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    listOf(
                        PaymentProvider.BKASH to ("বিকাশ পেমেন্ট গেটওয়ে (bKash Direct)" to Color(0xFFD82265)),
                        PaymentProvider.NAGAD to ("নগদ ডিজিটাল পেমেন্ট (Nagad MFS)" to Color(0xFFE24B25)),
                        PaymentProvider.ROCKET to ("রকেট (DBBL Rocket Mobile Banking)" to Color(0xFF8C3494)),
                        PaymentProvider.CARD_GATEWAY to ("ভিসা / মাস্টারকার্ড / অ্যামেক্স কার্ড" to Color(0xFF1E3A8A)),
                        PaymentProvider.BANK_TRANSFER to ("ব্যাংক ট্রান্সফার (সিটি / ব্র্যাক / ডাচ-বাংলা)" to Color(0xFF0F766E))
                    ).forEach { (provider, info) ->
                        val (title, brandColor) = info
                        val isSelected = selectedProvider == provider

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) BlueContainerLight else Color.White,
                            border = BorderStroke(1.2.dp, if (isSelected) BluePrimary else BlueBorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedProvider = provider }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedProvider = provider },
                                    colors = RadioButtonDefaults.colors(selectedColor = BluePrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(brandColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = title,
                                    fontFamily = AnekBangla,
                                    fontSize = 13.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = TextPrimaryLight
                                )
                            }
                        }
                    }
                }

                // Financial Breakdown Card
                FinancialBreakdownCard(
                    price = listing.price,
                    deliveryFee = deliveryFee,
                    protectionFee = listing.protectionFee,
                    total = totalAmount,
                    lang = lang
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Payment Gateway & Server-Side Verification Simulator Dialog
    if (showPaymentSimulatorDialog) {
        PaymentGatewaySimulationDialog(
            provider = selectedProvider,
            totalAmount = totalAmount,
            trxId = activeCreatedTrxId,
            lang = lang,
            onConfirmed = {
                val fakeTxId = when (selectedProvider) {
                    PaymentProvider.BKASH -> "BKH" + (10000000..99999999).random()
                    PaymentProvider.NAGAD -> "NGD" + (10000000..99999999).random()
                    PaymentProvider.ROCKET -> "RKT" + (10000000..99999999).random()
                    else -> "SSL" + (10000000..99999999).random()
                }
                viewModel.verifyAndLockPayment(
                    trxId = activeCreatedTrxId,
                    provider = selectedProvider,
                    gatewayTxId = fakeTxId
                ) {
                    showPaymentSimulatorDialog = false
                    onOrderComplete(activeCreatedTrxId)
                }
            },
            onDismiss = {
                showPaymentSimulatorDialog = false
                onOrderComplete(activeCreatedTrxId)
            }
        )
    }
}

@Composable
private fun PaymentGatewaySimulationDialog(
    provider: PaymentProvider,
    totalAmount: Double,
    trxId: String,
    lang: Language,
    onConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var accountNumber by remember { mutableStateOf("01712987654") }
    var pinNumber by remember { mutableStateOf("12345") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = BlueContainerLight,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (lang == Language.BANGLA) "${provider.providerName} গেটওয়ে ভেরিফিকেশন" else "${provider.providerName} Secure Gateway",
                    fontFamily = AnekBangla,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextPrimaryLight
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (step == 1) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BlueContainerLight,
                        border = BorderStroke(1.dp, BlueBorderLight)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Transaction Ref: #$trxId",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlueDark
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = if (lang == Language.BANGLA) "এসক্রোতে জমা হবে: ${formatBdt(totalAmount)}" else "Amount to Lock in Escrow: ${formatBdt(totalAmount)}",
                                fontFamily = AnekBangla,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = BluePrimary
                            )
                        }
                    }

                    Text(
                        text = if (lang == Language.BANGLA)
                            "সার্ভার-সাইড পেমেন্ট প্রোভাইডার যাচাইকরণ (প্রাতিষ্ঠানিক নিরাপদ এসক্রো):"
                            else "Simulated Server-Verified Payment Integration (Institutional Escrow):",
                        fontFamily = AnekBangla,
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )

                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = { Text("MFS Account / Card Number", fontFamily = AnekBangla) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )

                    OutlinedTextField(
                        value = pinNumber,
                        onValueChange = { pinNumber = it },
                        label = { Text("Security PIN / OTP", fontFamily = AnekBangla) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = BluePrimary)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (lang == Language.BANGLA)
                                "সার্ভার-টু-সার্ভার ওয়েবহুক যাচাইকরণ চলছে..."
                                else "Verifying server-to-server webhook callback...",
                            fontFamily = AnekBangla,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (lang == Language.BANGLA) "নিরাপদ এসক্রোতে ফান্ড লক করা হচ্ছে..." else "Locking funds in Nirapod Escrow...",
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            color = TextMutedLight
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (step == 1) {
                Button(
                    onClick = {
                        step = 2
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
                            onConfirmed()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("confirm_payment_sim_button")
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "পেমেন্ট নিশ্চিত ও নিরাপদ লক" else "Authorize & Lock Funds",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            if (step == 1) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = AppStrings.cancel(lang),
                        fontFamily = AnekBangla,
                        color = TextSecondaryLight
                    )
                }
            }
        }
    )
}
