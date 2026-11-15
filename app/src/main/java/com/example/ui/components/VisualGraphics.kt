package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.data.model.TransactionState
import com.example.ui.localization.Language
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AmberTrustContainer
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CrimsonAlertContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBorder
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight

// =========================================================================
// 1. PAYMENT PROTECTED HERO VISUAL COMPOSITION (User Requirement #7)
// =========================================================================

@Composable
fun PaymentProtectedHeroGraphic(
    tx: TransactionEntity,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val (statusLabelBn, statusLabelEn, statusColor, statusBg, statusIcon) = when (tx.state) {
        TransactionState.COMPLETED,
        TransactionState.SELLER_PAID -> StateVisual(
            "পেমেন্ট সফল ও মুক্তিপ্রাপ্ত",
            "Payment Released & Completed",
            SuccessGreen,
            SuccessGreenContainer,
            Icons.Default.CheckCircle
        )

        TransactionState.DISPUTED,
        TransactionState.UNDER_REVIEW -> StateVisual(
            "বিতর্ক স্থগিত: এসক্রো ফ্রিজ",
            "Disputed: Escrow Funds Frozen",
            CrimsonAlert,
            CrimsonAlertContainer,
            Icons.Default.Gavel
        )

        TransactionState.REFUNDED -> StateVisual(
            "১০০% রিফান্ড সম্পন্ন",
            "100% Refunded to Buyer",
            Color(0xFFBE185D),
            Color(0xFFFCE7F3),
            Icons.Default.Check
        )

        TransactionState.SHIPPED,
        TransactionState.OUT_FOR_DELIVERY -> StateVisual(
            "কুরিয়ার ট্রানজিটে — ফান্ড সুরক্ষিত",
            "In Courier Transit — Funds Protected",
            AmberTrust,
            AmberTrustContainer,
            Icons.Default.LocalShipping
        )

        TransactionState.BUYER_CONFIRMATION_PENDING -> StateVisual(
            "পণ্য যাচাইকরণ চলছে",
            "Inspection Pending",
            AmberTrust,
            AmberTrustContainer,
            Icons.Default.HourglassTop
        )

        else -> StateVisual(
            "পেমেন্ট সুরক্ষিত (নিরাপদ)",
            "PAYMENT PROTECTED BY NIRAPOD",
            BluePrimary,
            BlueContainerLight,
            Icons.Default.Lock
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.2.dp, BlueBorderLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0F8FE),
                            Color(0xFFF7FBFE),
                            Color.White
                        )
                    )
                )
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Tag: PAYMENT PROTECTED
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusBg,
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == Language.BANGLA) statusLabelBn else statusLabelEn,
                            fontFamily = AnekBangla,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = statusColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Large Secure Transaction Graphic with layered concentric glowing aura
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer Soft Glowing Aura in soft sky blue
                    Canvas(modifier = Modifier.size(120.dp).scale(pulseScale)) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    BlueSoft.copy(alpha = 0.6f),
                                    BlueContainerLight.copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            ),
                            radius = size.minDimension / 2f
                        )
                        // Decorative orbit
                        drawCircle(
                            color = BluePrimary.copy(alpha = 0.3f),
                            radius = size.minDimension / 2.3f,
                            style = Stroke(
                                width = 1.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }

                    // Middle elevated container
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 6.dp,
                        border = BorderStroke(2.dp, BlueBorderLight),
                        modifier = Modifier.size(76.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFF0F8FE),
                                        Color(0xFFE0F2FE)
                                    )
                                )
                            )
                        ) {
                            Icon(
                                imageVector = when (tx.state) {
                                    TransactionState.COMPLETED,
                                    TransactionState.SELLER_PAID -> Icons.Default.GppGood
                                    TransactionState.DISPUTED,
                                    TransactionState.UNDER_REVIEW -> Icons.Default.Gavel
                                    TransactionState.SHIPPED,
                                    TransactionState.OUT_FOR_DELIVERY -> Icons.Default.LocalShipping
                                    else -> Icons.Default.Security
                                },
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }

                    // Floating Mini Lock Badge in BluePrimary
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BluePrimary,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Hero Protected Amount Display (e.g., ৳ 1,250)
                Text(
                    text = formatBdt(tx.totalAmount),
                    fontFamily = AnekBangla,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimaryLight,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Product Title
                Text(
                    text = if (lang == Language.BANGLA) tx.productTitleBn else tx.productTitle,
                    fontFamily = AnekBangla,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondaryLight,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Dynamic Status Explanation Message
                val statusMessage = when (tx.state) {
                    TransactionState.FUNDS_HELD -> if (lang == Language.BANGLA)
                        "বিক্রেতা অর্ডার প্রস্তুত করছেন • অর্থ নিরাপদ এসক্রোতে রয়েছে"
                    else "Seller is preparing your order • Funds held securely by Nirapod"

                    TransactionState.SELLER_ACCEPTED -> if (lang == Language.BANGLA)
                        "বিক্রেতা অর্ডার গ্রহণ করেছেন • কুরিয়ারে হস্তান্তরের অপেক্ষায়"
                    else "Seller accepted order • Awaiting courier dispatch"

                    TransactionState.SHIPPED -> if (lang == Language.BANGLA)
                        "পার্সেল পথে রয়েছে • ট্র্যাকিং: ${if (tx.trackingNumber.isNotBlank()) tx.trackingNumber else "সক্রিয়"}"
                    else "Parcel on the way • Tracking: ${if (tx.trackingNumber.isNotBlank()) tx.trackingNumber else "Active"}"

                    TransactionState.OUT_FOR_DELIVERY -> if (lang == Language.BANGLA)
                        "আজ ডেলিভারি হবে • ডেলিভারিম্যান থেকে পার্সেল সংগ্রহ করুন"
                    else "Out for delivery today • Prepare to inspect on arrival"

                    TransactionState.DELIVERED,
                    TransactionState.BUYER_CONFIRMATION_PENDING -> if (lang == Language.BANGLA)
                        "পণ্য বুঝে পেয়ে যাচাই করুন • নিশ্চিত করলে বিক্রেতা অর্থ পাবেন"
                    else "Delivered • Inspect goods and confirm release of funds"

                    TransactionState.COMPLETED -> if (lang == Language.BANGLA)
                        "লেনদেন সফলভাবে সম্পন্ন • উভয় পক্ষ সন্তুষ্ট"
                    else "Transaction completed • Escrow successfully settled"

                    TransactionState.DISPUTED -> if (lang == Language.BANGLA)
                        "সালিশি ট্রাইব্যুনাল তথ্য যাচাই করছে • কোনো ফান্ড অপচয় হবে না"
                    else "Arbitration active • Evidence under cryptographic audit"

                    TransactionState.REFUNDED -> if (lang == Language.BANGLA)
                        "ক্রেতার ওয়ালেটে অর্থ ফেরত দেওয়া হয়েছে"
                    else "Full escrow refund returned to buyer"

                    else -> if (lang == Language.BANGLA)
                        "এসক্রো চুক্তি সক্রিয়"
                    else "Escrow protocol active"
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, BorderLight)
                ) {
                    Text(
                        text = statusMessage,
                        fontFamily = AnekBangla,
                        fontSize = 12.5.sp,
                        color = TextSecondaryLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }
}

private data class StateVisual(
    val titleBn: String,
    val titleEn: String,
    val color: Color,
    val bgColor: Color,
    val icon: ImageVector
)

// =========================================================================
// 2. CONNECTED PARTIES ESCROW BRIDGE ILLUSTRATION
// =========================================================================

@Composable
fun ConnectedPartiesIllustration(
    buyerName: String,
    sellerName: String,
    isSecured: Boolean = true,
    lang: Language,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueBorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = if (lang == Language.BANGLA) "দ্বিপাক্ষিক নিরাপদ সংযোগ" else "Connected Escrow Channel",
                fontFamily = AnekBangla,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Buyer Node
                PartyNode(
                    icon = Icons.Default.ShoppingBag,
                    role = if (lang == Language.BANGLA) "ক্রেতা (Buyer)" else "Buyer",
                    name = buyerName,
                    accentColor = BluePrimary
                )

                // Central Connected Vault Bridge
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Line with center badge
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                            drawLine(
                                color = BlueBorderLight,
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = BluePrimary,
                            border = BorderStroke(2.dp, Color.White),
                            shadowElevation = 3.dp,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isSecured) Icons.Default.Lock else Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (lang == Language.BANGLA) "নিরাপদ এসক্রো" else "Nirapod Escrow",
                        fontFamily = AnekBangla,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueDark
                    )
                }

                // Seller Node
                PartyNode(
                    icon = Icons.Default.Store,
                    role = if (lang == Language.BANGLA) "বিক্রেতা (Seller)" else "Seller",
                    name = sellerName,
                    accentColor = SuccessGreen
                )
            }
        }
    }
}

@Composable
private fun PartyNode(
    icon: ImageVector,
    role: String,
    name: String,
    accentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = accentColor.copy(alpha = 0.12f),
            border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.35f)),
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = role,
            fontFamily = AnekBangla,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
        Text(
            text = name,
            fontFamily = AnekBangla,
            fontSize = 11.5.sp,
            color = TextPrimaryLight,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

// =========================================================================
// 3. EMPTY STATE ILLUSTRATIONS (User Requirement #10, #22)
// =========================================================================

@Composable
fun EmptyTransactionsGraphic(
    lang: Language,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    WarmEmptyStateCard(
        title = if (lang == Language.BANGLA) "এখনো কোনো লেনদেন নেই" else "No Transactions Yet",
        subtitle = if (lang == Language.BANGLA)
            "আপনার শুরু করা সকল লেনদেন নিরাপদ এসক্রো সুরক্ষায় এখানে প্রদর্শিত হবে।"
        else "All your buyer and seller orders backed by Nirapod smart escrow will appear here.",
        illustration = {
            ArtisticVaultIllustration(icon = Icons.Default.ReceiptLong)
        },
        actionText = actionText,
        onActionClick = onActionClick,
        modifier = modifier
    )
}

@Composable
fun EmptyMarketplaceGraphic(
    lang: Language,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    WarmEmptyStateCard(
        title = if (lang == Language.BANGLA) "কোনো পণ্য পাওয়া যায়নি" else "No Products Listed",
        subtitle = if (lang == Language.BANGLA)
            "ভেরিফায়েড বিক্রেতাদের ১০০% সুরক্ষাযুক্ত নতুন পণ্য দেখতে পরে চেক করুন।"
        else "Discover new verified listings protected by our buyer-seller guarantee.",
        illustration = {
            ArtisticVaultIllustration(icon = Icons.Default.ShoppingBag)
        },
        actionText = actionText,
        onActionClick = onActionClick,
        modifier = modifier
    )
}

@Composable
fun EmptyDisputesGraphic(
    lang: Language,
    modifier: Modifier = Modifier
) {
    WarmEmptyStateCard(
        title = if (lang == Language.BANGLA) "কোনো সক্রিয় বিরোধ নেই" else "No Active Disputes",
        subtitle = if (lang == Language.BANGLA)
            "চমৎকার! আপনার সকল লেনদেন নিরাপদে এবং নির্বিঘ্নে সম্পন্ন হয়েছে।"
        else "Great! All transactions have progressed smoothly without requiring arbitration.",
        illustration = {
            ArtisticVaultIllustration(icon = Icons.Default.VerifiedUser, accentColor = SuccessGreen)
        },
        modifier = modifier
    )
}

@Composable
private fun WarmEmptyStateCard(
    title: String,
    subtitle: String,
    illustration: @Composable () -> Unit,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BlueBorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            illustration()

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = title,
                fontFamily = AnekBangla,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                fontFamily = AnekBangla,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = TextMutedLight,
                textAlign = TextAlign.Center
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(20.dp))
                NirapodPrimaryButton(
                    text = actionText,
                    onClick = onActionClick,
                    modifier = Modifier.width(210.dp)
                )
            }
        }
    }
}

@Composable
private fun ArtisticVaultIllustration(
    icon: ImageVector,
    accentColor: Color = BluePrimary
) {
    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        // Multi-layered soft glow rings
        Canvas(modifier = Modifier.size(100.dp)) {
            drawCircle(
                color = BlueSoft.copy(alpha = 0.45f),
                radius = size.minDimension / 2f
            )
            drawCircle(
                color = BlueBorderLight.copy(alpha = 0.3f),
                radius = size.minDimension / 2.6f
            )
        }

        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 5.dp,
            border = BorderStroke(1.5.dp, BlueBorderLight),
            modifier = Modifier.size(62.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.background(
                    Brush.radialGradient(
                        colors = listOf(
                            BlueContainerLight,
                            Color.White
                        )
                    )
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Mini accent floating sparkles in sky blue
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = BluePrimary,
                modifier = Modifier.size(8.dp)
            ) {}
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 12.dp, start = 8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = BlueLight,
                modifier = Modifier.size(10.dp)
            ) {}
        }
    }
}
