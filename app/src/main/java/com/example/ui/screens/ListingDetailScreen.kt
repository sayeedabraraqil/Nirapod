package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ListingEntity
import com.example.ui.components.FinancialBreakdownCard
import com.example.ui.components.NirapodCard
import com.example.ui.components.NirapodPrimaryButton
import com.example.ui.components.SparkleBadge
import com.example.ui.components.TrustShieldBanner
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatBdt
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBorder
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listingId: String,
    viewModel: EscrowViewModel,
    onBackClick: () -> Unit,
    onProceedToCheckout: (String) -> Unit,
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
                text = if (lang == Language.BANGLA) "পণ্যটি পাওয়া যায়নি" else "Listing not found",
                fontFamily = AnekBangla,
                fontSize = 16.sp
            )
        }
        return
    }

    val totalAmount = listing.price + listing.deliveryFee + listing.protectionFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (lang == Language.BANGLA) "পণ্যের বিবরণ ও সুরক্ষা" else "Listing Details",
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
                        onClick = { onProceedToCheckout(listing.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("buy_with_protection_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.buyWithProtection(lang),
                            fontFamily = AnekBangla,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
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
                // Hero Visual Card
                item {
                    ProductHeroHeader(listing = listing, lang = lang)
                }

                // Trust Guarantee Banner
                item {
                    TrustShieldBanner(
                        title = if (lang == Language.BANGLA) "১০০% নিরাপদ এসক্রো গ্যারান্টি" else "100% Escrow Protection Guaranteed",
                        subtitle = AppStrings.escrowNotice(lang)
                    )
                }

                // Seller Reputation & Trust Card
                item {
                    SellerTrustCard(listing = listing, lang = lang)
                }

                // Description
                item {
                    NirapodCard(
                        backgroundColor = Color.White,
                        borderColor = BlueBorderLight
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "পণ্যের বিস্তারিত বিবরণ" else "Product Description",
                            fontFamily = AnekBangla,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (lang == Language.BANGLA) listing.descriptionBn else listing.description,
                            fontFamily = AnekBangla,
                            fontSize = 14.sp,
                            color = TextSecondaryLight,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = BlueBorderLight)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (lang == Language.BANGLA) "পণ্য ধরন:" else "Product Type:",
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                color = TextMutedLight
                            )
                            Text(
                                text = if (listing.productType.name == "PHYSICAL")
                                    (if (lang == Language.BANGLA) "ভৌত পণ্য (কুরিয়ার)" else "Physical Good")
                                else (if (lang == Language.BANGLA) "ডিজিটাল সম্পদ" else "Digital Asset"),
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlueDark
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (lang == Language.BANGLA) "পরিদর্শন সময়কাল:" else "Inspection Window:",
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                color = TextMutedLight
                            )
                            Text(
                                text = if (lang == Language.BANGLA) "${listing.returnWindowDays} দিন (${listing.returnWindowDays * 24} ঘণ্টা)" else "${listing.returnWindowDays} Days (${listing.returnWindowDays * 24}h)",
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                        }
                    }
                }

                // Financial Breakdown
                item {
                    FinancialBreakdownCard(
                        price = listing.price,
                        deliveryFee = listing.deliveryFee,
                        protectionFee = listing.protectionFee,
                        total = totalAmount,
                        lang = lang
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductHeroHeader(listing: ListingEntity, lang: Language) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.2.dp, BlueBorderLight)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Top badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BlueContainerLight,
                    border = BorderStroke(0.8.dp, BlueBorderLight)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GppGood,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lang == Language.BANGLA) "এসক্রো ভেরিফায়েড" else "Escrow Verified",
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueDeep
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BlueSoft.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) listing.categoryBn else listing.category,
                        fontFamily = AnekBangla,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondaryLight,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (lang == Language.BANGLA) listing.titleBn else listing.title,
                fontFamily = AnekBangla,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimaryLight,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = formatBdt(listing.price),
                    fontFamily = AnekBangla,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BluePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (lang == Language.BANGLA) "(নিরাপদ সুরক্ষিত)" else "(Nirapod Protected)",
                    fontFamily = AnekBangla,
                    fontSize = 12.5.sp,
                    color = TextMutedLight,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun SellerTrustCard(listing: ListingEntity, lang: Language) {
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
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = listing.sellerName,
                            fontFamily = AnekBangla,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        if (listing.sellerVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = SuccessGreen,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    Text(
                        text = AppStrings.verifiedSeller(lang),
                        fontFamily = AnekBangla,
                        fontSize = 11.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AmberTrust,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${listing.sellerRating} / 5.0",
                    fontFamily = AnekBangla,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = BlueBorderLight)
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SellerStatColumn(
                value = "${listing.sellerCompletedOrders}",
                label = if (lang == Language.BANGLA) "সম্পন্ন অর্ডার" else "Completed"
            )
            SellerStatColumn(
                value = "${listing.sellerDisputeRate}%",
                label = if (lang == Language.BANGLA) "বিরোধের হার" else "Dispute Rate"
            )
            SellerStatColumn(
                value = "২৪-৪৮ ঘণ্টা",
                label = if (lang == Language.BANGLA) "গড় ডেলিভারি" else "Avg Delivery"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = TextMutedLight,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = listing.location,
                fontFamily = AnekBangla,
                fontSize = 12.sp,
                color = TextSecondaryLight
            )
        }
    }
}

@Composable
private fun SellerStatColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = AnekBangla,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryLight
        )
        Text(
            text = label,
            fontFamily = AnekBangla,
            fontSize = 11.sp,
            color = TextMutedLight
        )
    }
}
