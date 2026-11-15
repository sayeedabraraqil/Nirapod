package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.data.model.ProductType
import com.example.data.model.TransactionState
import com.example.ui.components.ConnectedPartiesIllustration
import com.example.ui.components.EscrowFlowVisualizer
import com.example.ui.components.FinancialBreakdownCard
import com.example.ui.components.NirapodCard
import com.example.ui.components.NirapodPrimaryButton
import com.example.ui.components.NirapodSecondaryButton
import com.example.ui.components.PaymentProtectedHeroGraphic
import com.example.ui.components.RiskBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.TimelineStepItem
import com.example.ui.components.TrustShieldBanner
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatBdt
import com.example.ui.components.formatTimestamp
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AmberTrustContainer
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CanvasLight
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CrimsonAlertBorder
import com.example.ui.theme.CrimsonAlertContainer
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBorder
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transactionId: String,
    viewModel: EscrowViewModel,
    onBackClick: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenDispute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val tx by viewModel.selectedTransaction.collectAsState()
    val timeline by viewModel.selectedTimeline.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showDispatchDialog by remember { mutableStateOf(false) }
    var trackingInput by remember { mutableStateOf("") }
    var showConfirmReceiptDialog by remember { mutableStateOf(false) }

    if (tx == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F8FE)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (lang == Language.BANGLA) "লেনদেন পাওয়া যায়নি" else "Transaction not found",
                fontFamily = AnekBangla,
                fontSize = 16.sp
            )
        }
        return
    }

    val item = tx!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (lang == Language.BANGLA) "লেনদেন #${item.id}" else "Escrow #${item.id}",
                            fontFamily = AnekBangla,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = TextPrimaryLight
                        )
                        Text(
                            text = formatTimestamp(item.createdAt),
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            color = TextMutedLight
                        )
                    }
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
                actions = {
                    IconButton(
                        onClick = { onOpenChat(item.id) },
                        modifier = Modifier.testTag("open_chat_appbar_button")
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BlueContainerLight,
                            border = BorderStroke(1.dp, BlueBorderLight),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = "Chat",
                                    tint = BluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 10.dp,
                color = Color.White,
                border = BorderStroke(1.dp, BlueBorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Role-specific primary actions (given full width for accessibility & clear focus)
                    if (role == UserRole.BUYER) {
                        if (item.state == TransactionState.SHIPPED ||
                            item.state == TransactionState.OUT_FOR_DELIVERY ||
                            item.state == TransactionState.DELIVERED ||
                            item.state == TransactionState.BUYER_CONFIRMATION_PENDING
                        ) {
                            Button(
                                onClick = { showConfirmReceiptDialog = true },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("confirm_receipt_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (lang == Language.BANGLA) "পণ্য বুঝে পেয়েছি ও অর্থ ছাড় করুন" else "Confirm Receipt & Release Funds",
                                    fontFamily = AnekBangla,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else if (role == UserRole.SELLER) {
                        if (item.state == TransactionState.FUNDS_HELD) {
                            Button(
                                onClick = { viewModel.sellerAccept(item.id) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("seller_accept_button")
                            ) {
                                Text(
                                    text = if (lang == Language.BANGLA) "অর্ডার গ্রহণ করুন" else "Accept Order",
                                    fontFamily = AnekBangla,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else if (item.state == TransactionState.SELLER_ACCEPTED && item.productType == ProductType.PHYSICAL) {
                            Button(
                                onClick = {
                                    trackingInput = "SFC-DH-" + (1000000..9999999).random()
                                    showDispatchDialog = true
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("seller_dispatch_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (lang == Language.BANGLA) "কুরিয়ারে পার্সেল পাঠান" else "Dispatch Parcel via Courier",
                                    fontFamily = AnekBangla,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else if (item.state == TransactionState.SHIPPED) {
                            Button(
                                onClick = { viewModel.markCourierDelivered(item.id) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                            ) {
                                Text(
                                    text = if (lang == Language.BANGLA) "ডেলিভারি সম্পন্ন চিহ্নিত করুন" else "Mark Delivered",
                                    fontFamily = AnekBangla,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Secondary actions row: Chat & Dispute (Balanced 50-50 width, no clipping)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Open Chat button (always available)
                        OutlinedButton(
                            onClick = { onOpenChat(item.id) },
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BlueBorderLight),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryLight),
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = BluePrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.openChat(lang),
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Dispute button (available during active transaction before completion)
                        if (item.state != TransactionState.COMPLETED &&
                            item.state != TransactionState.REFUNDED &&
                            item.state != TransactionState.DISPUTED
                        ) {
                            OutlinedButton(
                                onClick = { onOpenDispute(item.id) },
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, CrimsonAlertBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = CrimsonAlertContainer.copy(alpha = 0.5f),
                                    contentColor = CrimsonAlert
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 44.dp)
                                    .testTag("open_dispute_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReportProblem,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = CrimsonAlert
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (lang == Language.BANGLA) "সমস্যা জানান" else "Raise Dispute",
                                    fontFamily = AnekBangla,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        WarmDecorativeBackground(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Transaction Overview Header Card (Visual Hero Graphic)
                item {
                    PaymentProtectedHeroGraphic(tx = item, lang = lang)
                }

                // 2. Interactive Escrow Flow Visualizer (Heart of Escrow Experience)
                item {
                    EscrowFlowVisualizer(state = item.state, lang = lang)
                }

                // 3. Security Certificate / Vault Assurance Banner
                item {
                    TrustShieldBanner(
                        title = when (item.state) {
                            TransactionState.COMPLETED -> if (lang == Language.BANGLA) "লেনদেন সফলভাবে সম্পন্ন ও পরিশোধিত" else "Transaction Complete & Payout Released"
                            TransactionState.DISPUTED -> if (lang == Language.BANGLA) "বিতর্ক সক্রিয়: ফান্ড সাময়িক স্থগিত" else "Dispute Active: Funds Strictly Frozen"
                            TransactionState.REFUNDED -> if (lang == Language.BANGLA) "ক্রেতাকে পূর্ণ রিফান্ড প্রদান করা হয়েছে" else "100% Refund Processed to Buyer"
                            else -> if (lang == Language.BANGLA) "পেমেন্ট সুরক্ষিত — আপনার অর্থ নিরাপদে সংরক্ষিত" else "PAYMENT PROTECTED — Funds Secured by Nirapod"
                        },
                        subtitle = when (item.state) {
                            TransactionState.COMPLETED -> if (lang == Language.BANGLA) "ক্রেতার অনুমোদনের পর বিক্রেতার ওয়ালেটে অর্থ ছাড় করা হয়েছে।" else "Funds transferred to seller wallet following buyer verification."
                            TransactionState.DISPUTED -> if (lang == Language.BANGLA) "প্ল্যাটফর্ম আরবিট্রেটর উভয় পক্ষের প্রমাণ পর্যালোচনা না করা পর্যন্ত অর্থ সুরক্ষিতভাবে লক থাকবে।" else "Arbitration active. Funds locked securely pending dispute resolution."
                            TransactionState.REFUNDED -> if (lang == Language.BANGLA) "অর্থ মূল পেমেন্ট মেথডে (বিকাশ/নগদ) ফেরত পাঠানো হয়েছে।" else "Payment returned safely to buyer account."
                            else -> if (lang == Language.BANGLA) "পণ্য হাতে পেয়ে যাচাই করার আগ পর্যন্ত টাকা বিক্রেতার কাছে যায় না। আপনার অর্থ ১০০% ঝুঁকিমুক্ত।" else "Money stays in escrow until you verify delivery. Seller cannot access funds early."
                        }
                    )
                }

                // 4. Digital Vault Delivery Section (if applicable)
                if (item.productType == ProductType.DIGITAL_KEY || item.productType == ProductType.DIGITAL_FILE) {
                    item {
                        DigitalVaultCard(
                            tx = item,
                            lang = lang,
                            onReveal = { viewModel.revealKey(item.id) },
                            onCopy = { secret ->
                                clipboardManager.setText(AnnotatedString(secret))
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                // 5. Physical Courier Consignment Section (if applicable)
                if (item.productType == ProductType.PHYSICAL) {
                    item {
                        CourierShipmentCard(tx = item, lang = lang)
                    }
                }

                // 6. Parties Info Card (Connected Escrow Bridge)
                item {
                    ConnectedPartiesIllustration(
                        buyerName = item.buyerName,
                        sellerName = item.sellerName,
                        lang = lang
                    )
                }

                // 7. Transparent Financial Breakdown Card
                item {
                    FinancialBreakdownCard(
                        price = item.price,
                        deliveryFee = item.deliveryFee,
                        protectionFee = item.protectionFee,
                        total = item.totalAmount,
                        lang = lang
                    )
                }

                // 8. Immutable Audit Timeline Section
                item {
                    NirapodCard(
                        backgroundColor = Color.White,
                        borderColor = BlueBorderLight
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = BlueContainerLight,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = null,
                                            tint = BluePrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (lang == Language.BANGLA) "অপরিবর্তনীয় লেনদেন অডিট টাইমলাইন" else "Immutable Audit Timeline",
                                    fontFamily = AnekBangla,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimaryLight
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BlueContainerLight
                            ) {
                                Text(
                                    text = "CRYPTOGRAPHIC LOG",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlueDeep,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        timeline.forEachIndexed { index, event ->
                            TimelineStepItem(
                                title = if (lang == Language.BANGLA) event.eventTitleBn else event.eventTitle,
                                description = if (lang == Language.BANGLA) event.descriptionBn else event.description,
                                timestamp = event.timestamp,
                                isCompleted = event.isCompleted,
                                isCurrent = event.isCurrent,
                                isLast = index == timeline.lastIndex
                            )
                        }
                    }
                }
            }
        }
    }

    // Courier Dispatch Dialog for Seller
    if (showDispatchDialog) {
        AlertDialog(
            onDismissRequest = { showDispatchDialog = false },
            title = {
                Text(
                    text = if (lang == Language.BANGLA) "কুরিয়ার পার্সেল প্রেরণ" else "Dispatch Parcel via Courier",
                    fontFamily = AnekBangla,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
            },
            text = {
                Column {
                    Text(
                        text = if (lang == Language.BANGLA)
                            "পার্সেল হস্তান্তর করে কুরিয়ার ট্র্যাকিং / কনসাইনমেন্ট নম্বর দিন:"
                            else "Enter courier consignment / tracking ID:",
                        fontFamily = AnekBangla,
                        fontSize = 13.sp,
                        color = TextSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = trackingInput,
                        onValueChange = { trackingInput = it },
                        label = { Text("Tracking Number", fontFamily = AnekBangla) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sellerDispatch(item.id, item.courier, trackingInput)
                        showDispatchDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "নিশ্চিত করুন" else "Confirm Dispatch",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDispatchDialog = false }) {
                    Text(
                        text = AppStrings.cancel(lang),
                        fontFamily = AnekBangla,
                        color = TextSecondaryLight
                    )
                }
            }
        )
    }

    // Confirm Receipt Dialog for Buyer
    if (showConfirmReceiptDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmReceiptDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lang == Language.BANGLA) "পণ্য নিশ্চিতকরণ ও অর্থ ছাড়" else "Confirm Receipt & Release Funds",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Text(
                    text = if (lang == Language.BANGLA)
                        "আপনি কি পণ্যটি সঠিক অবস্থায় বুঝে পেয়েছেন? নিশ্চিত করলে বিক্রেতার কাছে ${formatBdt(item.price)} ছাড় করা হবে এবং লেনদেন সম্পন্ন হবে।"
                        else "Are you satisfied with the received product? Approving will immediately credit ${formatBdt(item.price)} to the seller's wallet.",
                    fontFamily = AnekBangla,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = TextSecondaryLight
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.buyerConfirmReceipt(item.id)
                        showConfirmReceiptDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "হ্যাঁ, ফান্ড রিলিজ করুন" else "Yes, Release Funds",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmReceiptDialog = false }) {
                    Text(
                        text = AppStrings.cancel(lang),
                        fontFamily = AnekBangla,
                        color = TextSecondaryLight
                    )
                }
            }
        )
    }
}

// ==========================================
// STATUS & HERO OVERVIEW
// ==========================================

@Composable
private fun TransactionStatusBanner(tx: TransactionEntity, lang: Language) {
    NirapodCard(
        backgroundColor = Color.White,
        borderColor = BlueBorderLight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBadge(state = tx.state, lang = lang)
            RiskBadge(risk = tx.fraudRiskScore)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (lang == Language.BANGLA) tx.productTitleBn else tx.productTitle,
            fontFamily = AnekBangla,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryLight,
            lineHeight = 25.sp
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = BlueBorderLight)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ID: ${tx.paymentTxId}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp,
                    color = TextMutedLight
                )
                Text(
                    text = if (lang == Language.BANGLA) "এমএফএস মেথড: ${tx.paymentProvider.providerName}" else "Via ${tx.paymentProvider.providerName}",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    color = TextSecondaryLight
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (lang == Language.BANGLA) "মোট পরিশোধিত অর্থ" else "Total Escrowed",
                    fontFamily = AnekBangla,
                    fontSize = 11.sp,
                    color = TextMutedLight
                )
                Text(
                    text = formatBdt(tx.totalAmount),
                    fontFamily = AnekBangla,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BluePrimary
                )
            }
        }
    }
}

// ==========================================
// DIGITAL ENCRYPTED VAULT CARD
// ==========================================

@Composable
private fun DigitalVaultCard(
    tx: TransactionEntity,
    lang: Language,
    onReveal: () -> Unit,
    onCopy: (String) -> Unit
) {
    var revealed by remember { mutableStateOf(tx.digitalRevealedAt > 0L) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2238)),
        border = BorderStroke(1.2.dp, Color(0xFF1E3A5F))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = null,
                        tint = Color(0xFF90CAF9),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lang == Language.BANGLA) "নিরাপদ ডিজিটাল ডেলিভারি" else "Nirapod Digital Delivery",
                        fontFamily = AnekBangla,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF163252)
                ) {
                    Text(
                        text = "ENCRYPTED",
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF90CAF9),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (lang == Language.BANGLA)
                    "লাইসেন্স কি বা ফাইল নিরাপদ এনক্রিপশনে সংরক্ষিত আছে। প্রকাশের সাথে সাথে সিস্টেম অডিট লগ তৈরি হবে।"
                    else "License key or delivery assets are locked securely. Decryption is logged with an immutable timestamp.",
                fontFamily = AnekBangla,
                fontSize = 12.5.sp,
                color = Color(0xFFD6E4FF).copy(alpha = 0.9f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (!revealed) {
                Button(
                    onClick = {
                        revealed = true
                        onReveal()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.revealKey(lang),
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF132B47),
                    border = BorderStroke(1.dp, Color(0xFF264A73)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tx.digitalPayload.ifBlank { "JB-2026-BD982-LIC-X77Q-K981-AUTHENTIC" },
                            color = Color(0xFFE1EDFE),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onCopy(tx.digitalPayload) }) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PHYSICAL COURIER TRACKING
// ==========================================

@Composable
private fun CourierShipmentCard(tx: TransactionEntity, lang: Language) {
    NirapodCard(
        backgroundColor = Color.White,
        borderColor = BlueBorderLight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = BlueContainerLight,
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (lang == Language.BANGLA) "কুরিয়ার ও পার্সেল শিপমেন্ট" else "Courier & Shipment Tracking",
                    fontFamily = AnekBangla,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = BlueContainerLight,
                border = BorderStroke(1.dp, BlueBorderLight)
            ) {
                Text(
                    text = tx.courier.providerName,
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueDark,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (lang == Language.BANGLA) "ট্র্যাকিং আইডি:" else "Consignment ID:",
                fontFamily = AnekBangla,
                fontSize = 12.sp,
                color = TextMutedLight
            )
            Text(
                text = tx.trackingNumber,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (lang == Language.BANGLA) "গন্তব্য ঠিকানা:" else "Destination:",
                fontFamily = AnekBangla,
                fontSize = 12.sp,
                color = TextMutedLight
            )
            Text(
                text = tx.deliveryAddress,
                fontFamily = AnekBangla,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimaryLight,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

// ==========================================
// PARTIES INFORMATION
// ==========================================

@Composable
private fun PartiesInfoCard(tx: TransactionEntity, lang: Language) {
    NirapodCard(
        backgroundColor = Color.White,
        borderColor = BlueBorderLight
    ) {
        Text(
            text = if (lang == Language.BANGLA) "লেনদেনের পক্ষসমূহ" else "Transaction Counterparties",
            fontFamily = AnekBangla,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryLight
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (lang == Language.BANGLA) "ক্রেতা (Buyer)" else "Buyer",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    color = TextMutedLight
                )
                Text(
                    text = tx.buyerName,
                    fontFamily = AnekBangla,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = tx.buyerPhone,
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (lang == Language.BANGLA) "বিক্রেতা (Seller)" else "Seller",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    color = TextMutedLight
                )
                Text(
                    text = tx.sellerName,
                    fontFamily = AnekBangla,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = tx.sellerPhone,
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )
            }
        }
    }
}
