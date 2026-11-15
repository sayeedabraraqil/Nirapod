package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ListingEntity
import com.example.data.model.ProductType
import com.example.ui.components.NirapodCard
import com.example.ui.components.NirapodLanguageSwitcher
import com.example.ui.components.SparkleBadge
import com.example.ui.components.formatBdt
import com.example.ui.localization.Language
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AmberTrustContainer
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.CanvasLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBorder
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SurfaceLight
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel

@Composable
fun HomeScreen(
    viewModel: EscrowViewModel,
    onListingClick: (String) -> Unit,
    onStartTransactionClick: () -> Unit,
    onBecomeSellerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val listings by viewModel.filteredListings.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val categories = listOf(
        "All" to (if (lang == Language.BANGLA) "সব ক্যাটাগরি" else "All Categories"),
        "Electronics & PC" to (if (lang == Language.BANGLA) "ইলেকট্রনিক্স ও গ্যাজেট" else "Electronics"),
        "Software & Licenses" to (if (lang == Language.BANGLA) "সফটওয়্যার ও লাইসেন্স" else "Software"),
        "Fashion & Handloom" to (if (lang == Language.BANGLA) "ফ্যাশন ও জামদানি" else "Fashion"),
        "Design Assets" to (if (lang == Language.BANGLA) "ডিজিটাল আর্ট ও ডিজাইন" else "Design"),
        "Audio & Electronics" to (if (lang == Language.BANGLA) "অডিও ও সাউন্ড" else "Audio")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasLight)
    ) {
        // Decorative tranquil light-blue gradient canvas for ambient depth
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            // Top right soft glowing azure sky sunburst
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x3570BCF8),
                        Color(0x18BAE0FD),
                        Color(0x00F0F7FD)
                    ),
                    center = Offset(canvasW * 0.92f, canvasH * 0.08f),
                    radius = canvasW * 0.75f
                ),
                center = Offset(canvasW * 0.92f, canvasH * 0.08f),
                radius = canvasW * 0.75f
            )

            // Mid-left soft tranquil ice-blue glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x2038BDF8),
                        Color(0x0EBAE0FD),
                        Color(0x00F0F7FD)
                    ),
                    center = Offset(canvasW * 0.04f, canvasH * 0.5f),
                    radius = canvasW * 0.6f
                ),
                center = Offset(canvasW * 0.04f, canvasH * 0.5f),
                radius = canvasW * 0.6f
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_screen_list"),
            contentPadding = PaddingValues(bottom = 118.dp)
        ) {
            // 1. Warm App Header (Avatar, Greeting, Language & Notifications)
            item {
                HomeHeaderBar(
                    lang = lang,
                    onToggleLang = {
                        viewModel.setLanguage(if (lang == Language.BANGLA) Language.ENGLISH else Language.BANGLA)
                    }
                )
            }

            // 2. High-Impact Escrow Visual Hero Banner (Graphic Escrow Flow)
            item {
                HeroEscrowVisualSection(
                    lang = lang,
                    onStartClick = onStartTransactionClick,
                    onSellerClick = onBecomeSellerClick
                )
            }

            // 3. Quick Actions Hub (4 rounded squircle cards)
            item {
                HomeQuickActions(
                    lang = lang,
                    onBuyClick = onStartTransactionClick,
                    onSellClick = onBecomeSellerClick
                )
            }

            // 4. Trust Statistics Strip
            item {
                HomeTrustStats(lang = lang)
            }

            // 5. Visual "How Escrow Protects You" Explainer
            item {
                HomeEscrowHowItWorks(lang = lang)
            }

            // 6. Search Bar & Horizontal Category Chips
            item {
                HomeSearchAndFilter(
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.searchQuery.value = it },
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelect = { viewModel.selectedCategory.value = it },
                    lang = lang
                )
            }

            // 7. Verified Listings Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = BluePrimary,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BANGLA) "যাচাইকৃত সুরক্ষিত পণ্যসমূহ" else "Verified Escrow Listings",
                            fontFamily = AnekBangla,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BlueContainerLight,
                        border = BorderStroke(1.dp, BlueBorderLight)
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "${listings.size} টি পণ্য প্রস্তুত" else "${listings.size} available",
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 8. Listings Cards
            items(listings, key = { it.id }) { item ->
                ListingCard(
                    listing = item,
                    lang = lang,
                    onClick = { onListingClick(item.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                )
            }
        }
    }
}

// ==========================================
// 1. HOME HEADER BAR
// ==========================================

@Composable
private fun HomeHeaderBar(
    lang: Language,
    onToggleLang: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Profile & Welcome Message (Constrained so it doesn't push action items)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(2.dp, BluePrimary),
                shadowElevation = 3.dp,
                modifier = Modifier.size(42.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0284C7),
                                    Color(0xFF38BDF8)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "আ",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (lang == Language.BANGLA) "শুভ দিন, আহমেদ" else "Welcome, Ahmed",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.5.sp,
                        color = TextPrimaryLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "✦",
                        fontSize = 11.sp,
                        color = BluePrimary
                    )
                }
                Text(
                    text = if (lang == Language.BANGLA) "নিরাপদ সুরক্ষায়" else "Protected by Nirapod",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Action Controls (Unclipped Language Switcher + Notification Bell)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Consistent, responsive Language Toggle
            NirapodLanguageSwitcher(
                currentLanguage = lang,
                onToggle = onToggleLang,
                testTag = "home_language_toggle_button"
            )

            // Notification Bell with Blue Dot
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, BlueBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier.size(38.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TextSecondaryLight,
                        modifier = Modifier.size(19.dp)
                    )
                    // Unread blue badge
                    Surface(
                        shape = CircleShape,
                        color = BluePrimary,
                        border = BorderStroke(1.5.dp, Color.White),
                        modifier = Modifier
                            .size(9.dp)
                            .align(Alignment.TopEnd)
                    ) {}
                }
            }
        }
    }
}

// ==========================================
// 2. HERO ESCROW VISUAL SECTION
// Striking abstract transaction-protection graphic
// ==========================================

@Composable
private fun HeroEscrowVisualSection(
    lang: Language,
    onStartClick: () -> Unit,
    onSellerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.2.dp, BlueBorderLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE0F2FE),
                            Color(0xFFF0F8FE),
                            Color(0xFFF8FAFC)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                // Top Sparkle Assurance Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SparkleBadge(
                        text = if (lang == Language.BANGLA) "১০০% নিরাপদ সুরক্ষিত লেনদেন" else "100% Nirapod Escrow Guaranteed"
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.8f),
                        border = BorderStroke(0.8.dp, BlueBorderLight)
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "লাইভ সিকিউরিটি" else "Active Shield",
                            fontFamily = AnekBangla,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlueDeep,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Big Impact Visual Display Typography
                Text(
                    text = if (lang == Language.BANGLA)
                        "নিরাপদে কিনুন।\nনিশ্চিন্তে বিক্রি করুন।"
                    else "Buy Safely.\nTrade Confidently.",
                    fontFamily = AnekBangla,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimaryLight,
                    lineHeight = 31.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (lang == Language.BANGLA)
                        "আপনার টাকা নিরাপদ সুরক্ষায় জমা থাকে; পণ্য হাতে পেয়ে সন্তুষ্ট হলে তবেই বিক্রেতা টাকা পান।"
                    else "Funds are locked in Nirapod secure escrow. Released only after courier inspection.",
                    fontFamily = AnekBangla,
                    fontSize = 13.sp,
                    color = TextSecondaryLight,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Striking Abstract Escrow Protection Graphic:
                // [ ক্রেতা ] ── ( 🔒 সুরক্ষিত নিরাপদ এসক্রো ✦ ) ── [ বিক্রেতা ]
                EscrowFlowVisualGraphic(lang = lang)

                Spacer(modifier = Modifier.height(18.dp))

                // High-Impact CTA Buttons (Responsive, balanced, no text clipping)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Primary CTA (Sky Azure Gradient with Arrow)
                    Surface(
                        onClick = onStartClick,
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .weight(1.15f)
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("start_protected_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFF0284C7),
                                            Color(0xFF0EA5E9),
                                            Color(0xFF38BDF8)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCartCheckout,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (lang == Language.BANGLA) "সুরক্ষিত কেনা" else "Buy Protected",
                                    fontFamily = AnekBangla,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "→",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Secondary CTA (Ice-blue bordered clean pill)
                    Surface(
                        onClick = onSellerClick,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = BorderStroke(1.2.dp, BlueBorderLight),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("become_seller_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddBusiness,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (lang == Language.BANGLA) "নিরাপদ বিক্রি" else "Sell Safely",
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlueDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Visual Escrow Protection graphic connecting Buyer, Central Locked Vault, and Seller
 */
@Composable
private fun EscrowFlowVisualGraphic(lang: Language) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.92f),
        border = BorderStroke(1.dp, BlueBorderLight),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Buyer Node
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = BlueContainerLight,
                    border = BorderStroke(1.dp, BlueBorderLight),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCartCheckout,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (lang == Language.BANGLA) "১. ক্রেতা" else "1. Buyer",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = if (lang == Language.BANGLA) "পেমেন্ট লক" else "Payment Locked",
                    fontFamily = AnekBangla,
                    fontSize = 9.5.sp,
                    color = TextMutedLight
                )
            }

            // Connecting Arrow
            Text(
                text = "➔",
                fontSize = 14.sp,
                color = BluePrimary.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )

            // Central Glowing Vault Sphere
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1.3f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = BluePrimary,
                    shadowElevation = 6.dp,
                    border = BorderStroke(2.dp, Color.White),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFF7DD3FC),
                                        Color(0xFF0EA5E9),
                                        Color(0xFF0369A1)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (lang == Language.BANGLA) "নিরাপদ সুরক্ষা" else "Nirapod Escrow",
                        fontFamily = AnekBangla,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BlueDeep
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "✦", fontSize = 9.sp, color = BluePrimary)
                }
                Text(
                    text = if (lang == Language.BANGLA) "১০০% নিরাপদ" else "100% Protected",
                    fontFamily = AnekBangla,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueDark
                )
            }

            // Connecting Arrow
            Text(
                text = "➔",
                fontSize = 14.sp,
                color = BluePrimary.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )

            // Seller Node
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SuccessGreenContainer,
                    border = BorderStroke(1.dp, SuccessGreenBorder),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (lang == Language.BANGLA) "৩. বিক্রেতা" else "3. Seller",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = if (lang == Language.BANGLA) "নিশ্চিত পেআউট" else "Safe Payout",
                    fontFamily = AnekBangla,
                    fontSize = 9.5.sp,
                    color = TextMutedLight
                )
            }
        }
    }
}

// ==========================================
// 3. QUICK ACTIONS HUB (4 Warm Squircles)
// ==========================================

@Composable
private fun HomeQuickActions(
    lang: Language,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickActionSquircle(
            icon = Icons.Default.Security,
            title = if (lang == Language.BANGLA) "এসক্রো শপ" else "Escrow Shop",
            subtitle = if (lang == Language.BANGLA) "নিরাপদ সুরক্ষা" else "Nirapod Protected",
            accentColor = BluePrimary,
            onClick = onBuyClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionSquircle(
            icon = Icons.Default.AddBusiness,
            title = if (lang == Language.BANGLA) "সেলার হাব" else "Seller Hub",
            subtitle = if (lang == Language.BANGLA) "পণ্য তালিকা" else "Create Listing",
            accentColor = Color(0xFF0284C7),
            onClick = onSellClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionSquircle(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BlueBorderLight),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                BlueContainerLight,
                                Color(0xFFD6EAF8)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontFamily = AnekBangla,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = subtitle,
                    fontFamily = AnekBangla,
                    fontSize = 11.sp,
                    color = TextMutedLight
                )
            }
        }
    }
}

// ==========================================
// 4. TRUST STATISTICS STRIP
// ==========================================

@Composable
private fun HomeTrustStats(lang: Language) {
    NirapodCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        borderColor = BlueBorderLight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                value = if (lang == Language.BANGLA) "৳ ৪.২ কোটি+" else "৳ 42M+",
                label = if (lang == Language.BANGLA) "সুরক্ষিত লেনদেন" else "Protected Volume",
                color = BluePrimary,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(34.dp)
                    .background(BorderLight)
            )
            StatItem(
                value = "০%",
                label = if (lang == Language.BANGLA) "প্রতারণার ঝুঁকি" else "Scam Loss Rate",
                color = SuccessGreen,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(34.dp)
                    .background(BorderLight)
            )
            StatItem(
                value = "১,২৪০+",
                label = if (lang == Language.BANGLA) "সফল ডেলিভারি" else "Completed Orders",
                color = AmberTrust,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = value,
            fontFamily = AnekBangla,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontFamily = AnekBangla,
            fontSize = 10.5.sp,
            color = TextMutedLight,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

// ==========================================
// 5. HOW ESCROW PROTECTS YOU EXPLAINER
// ==========================================

@Composable
private fun HomeEscrowHowItWorks(lang: Language) {
    NirapodCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = Color.White,
        borderColor = BlueBorderLight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = BlueContainerLight,
                border = BorderStroke(1.dp, BlueBorderLight),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (lang == Language.BANGLA) "কীভাবে নিরাপদ এসক্রো কাজ করে?" else "How Nirapod Escrow Works",
                        fontFamily = AnekBangla,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "✦", fontSize = 10.sp, color = BluePrimary)
                }
                Text(
                    text = if (lang == Language.BANGLA) "৩টি সহজ ধাপে সম্পূর্ণ আর্থিক নিশ্চয়তা" else "3 Simple Steps to Zero Scam Risk",
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    color = TextMutedLight
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HowStepRow(
            stepNumber = "১",
            icon = Icons.Default.Lock,
            iconTint = BluePrimary,
            iconBg = BlueContainerLight,
            title = if (lang == Language.BANGLA) "১. অর্থ নিরাপদে জমা" else "1. Funds Secured by Nirapod",
            desc = if (lang == Language.BANGLA)
                "বিকাশ/নগদ দিয়ে পেমেন্ট করলে টাকা সরাসরি বিক্রেতার কাছে যায় না; নিরাপদ প্রাতিষ্ঠানিক এসক্রোতে সংরক্ষিত থাকে।"
            else "Buyer funds are deposited directly into Nirapod secure escrow."
        )

        Spacer(modifier = Modifier.height(8.dp))

        HowStepRow(
            stepNumber = "২",
            icon = Icons.Default.LocalShipping,
            iconTint = AmberTrust,
            iconBg = AmberTrustContainer,
            title = if (lang == Language.BANGLA) "২. কুরিয়ার ট্র্যাকিং ও ডেলিভারি" else "2. Tracked Courier Delivery",
            desc = if (lang == Language.BANGLA)
                "বিক্রেতা স্টিডফাস্ট/রেডেক্স দিয়ে পার্সেল পাঠান। লাইভ ট্র্যাকিং আইডি সংযুক্ত থাকে।"
            else "Seller dispatches goods with integrated API tracking across Bangladesh."
        )

        Spacer(modifier = Modifier.height(8.dp))

        HowStepRow(
            stepNumber = "৩",
            icon = Icons.Default.CheckCircle,
            iconTint = SuccessGreen,
            iconBg = SuccessGreenContainer,
            title = if (lang == Language.BANGLA) "৩. পণ্য দেখে অনুমোদন ও পে-আউট" else "3. Inspect & Fund Release",
            desc = if (lang == Language.BANGLA)
                "পার্সেল খুলে যাচাই করুন। সন্তুষ্ট হলে টাকা বিক্রেতা পাবেন; কোনো গড়মিল থাকলে ১০০% রিফান্ড।"
            else "Inspect item within 24 hours. Release payment only if fully satisfied."
        )
    }
}

@Composable
private fun HowStepRow(
    stepNumber: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    desc: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF9FBFE),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(19.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = AnekBangla,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    fontFamily = AnekBangla,
                    fontSize = 11.sp,
                    color = TextSecondaryLight,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// ==========================================
// 6. SEARCH & FILTER SECTION
// ==========================================

@Composable
private fun HomeSearchAndFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categories: List<Pair<String, String>>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    lang: Language
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text(
                    text = if (lang == Language.BANGLA) "পণ্য, বিক্রেতা বা ব্র্যান্ড অনুসন্ধান..." else "Search products, sellers, brands...",
                    fontFamily = AnekBangla,
                    fontSize = 13.5.sp,
                    color = TextMutedLight
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input"),
            shape = RoundedCornerShape(16.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = AnekBangla,
                fontSize = 14.sp,
                color = TextPrimaryLight
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = BlueBorderLight
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Category Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { (catKey, catLabel) ->
                val isSelected = selectedCategory == catKey
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(catKey) },
                    label = {
                        Text(
                            text = catLabel,
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BluePrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = TextSecondaryLight
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = BlueBorderLight,
                        selectedBorderColor = BluePrimary
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }
    }
}

// ==========================================
// 7. REDESIGNED LISTING CARD
// ==========================================

@Composable
fun ListingCard(
    listing: ListingEntity,
    lang: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("listing_card_${listing.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BlueBorderLight)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Product Graphic Placeholder with tranquil light-blue gradient
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    BlueContainerLight,
                                    Color(0xFFD8EEFE)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val icon: ImageVector = when (listing.productType) {
                        ProductType.PHYSICAL -> Icons.Default.Inventory2
                        ProductType.DIGITAL_KEY -> Icons.Default.VpnKey
                        ProductType.DIGITAL_FILE -> Icons.Default.Palette
                        ProductType.SERVICE -> Icons.Default.Code
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Category & Verification Tag Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BlueContainerLight
                        ) {
                            Text(
                                text = if (lang == Language.BANGLA) listing.categoryBn else listing.category,
                                fontFamily = AnekBangla,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlueDark,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        // Escrow Shield Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SuccessGreenContainer,
                            border = BorderStroke(0.6.dp, SuccessGreenBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GppGood,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (lang == Language.BANGLA) "এসক্রো গার্ড" else "Escrow Guard",
                                    fontFamily = AnekBangla,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // Title
                    Text(
                        text = if (lang == Language.BANGLA) listing.titleBn else listing.title,
                        fontFamily = AnekBangla,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight,
                        lineHeight = 21.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Seller info with Trust Star
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = listing.sellerName,
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            color = TextSecondaryLight,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AmberTrust,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${listing.sellerRating} (${listing.sellerCompletedOrders})",
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            // Price & Action Button Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (lang == Language.BANGLA) "নির্ধারিত মূল্য" else "Escrow Price",
                        fontFamily = AnekBangla,
                        fontSize = 10.5.sp,
                        color = TextMutedLight
                    )
                    Text(
                        text = formatBdt(listing.price),
                        fontFamily = AnekBangla,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BluePrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlueContainerLight,
                    border = BorderStroke(1.dp, BlueBorderLight),
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "সুরক্ষিতভাবে কিনুন" else "Buy Protected",
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = BlueDark,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
