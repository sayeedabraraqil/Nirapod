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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.data.model.TransactionState
import com.example.ui.components.NirapodCard
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatBdt
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CanvasLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel

@Composable
fun SellerDashboardScreen(
    viewModel: EscrowViewModel,
    onTransactionClick: (String) -> Unit,
    onCreateListingClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val wallet by viewModel.currentWallet.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()
    val sellerTransactions = allTransactions.filter { it.sellerId == viewModel.currentSellerId }

    val pendingOrders = sellerTransactions.filter {
        it.state == TransactionState.FUNDS_HELD ||
                it.state == TransactionState.SELLER_ACCEPTED ||
                it.state == TransactionState.SHIPPED
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateListingClick,
                containerColor = BluePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .padding(bottom = 84.dp)
                    .testTag("seller_add_listing_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (lang == Language.BANGLA) "নতুন পণ্য যোগ" else "Add Listing",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        WarmDecorativeBackground(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 110.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Seller Header
                item {
                    SellerHeaderCard(lang = lang)
                }

                // Wallet Quick Summary Card
                item {
                    SellerFinancialCard(
                        heldInEscrow = wallet?.heldFunds ?: 69670.0,
                        availableBalance = wallet?.availableBalance ?: 182500.0,
                        lang = lang,
                        onWithdrawClick = onWithdrawClick
                    )
                }

                // Section Header: Action Needed Orders
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "অ্যাকশন প্রয়োজন (${pendingOrders.size})" else "Orders Requiring Action (${pendingOrders.size})",
                            fontFamily = AnekBangla,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                if (pendingOrders.isEmpty()) {
                    item {
                        NirapodCard(
                            backgroundColor = Color.White,
                            borderColor = BlueBorderLight
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (lang == Language.BANGLA) "কোনো মুলতুবি অর্ডার নেই" else "No orders currently pending action.",
                                    fontFamily = AnekBangla,
                                    fontSize = 14.sp,
                                    color = TextMutedLight
                                )
                            }
                        }
                    }
                } else {
                    items(pendingOrders, key = { it.id }) { order ->
                        SellerOrderItem(
                            order = order,
                            lang = lang,
                            onClick = { onTransactionClick(order.id) }
                        )
                    }
                }

                // Section Header: All Seller Orders
                item {
                    Text(
                        text = if (lang == Language.BANGLA) "সকল বিক্রেতা লেনদেন (${sellerTransactions.size})" else "All Seller Orders (${sellerTransactions.size})",
                        fontFamily = AnekBangla,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                }

                items(sellerTransactions, key = { "all_" + it.id }) { order ->
                    SellerOrderItem(
                        order = order,
                        lang = lang,
                        onClick = { onTransactionClick(order.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
private fun SellerHeaderCard(lang: Language) {
    NirapodCard(
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
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "টেকম্যাট্রিক্স বিডি (TechMatrix BD)",
                        fontFamily = AnekBangla,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = SuccessGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = if (lang == Language.BANGLA) "ভেরিফায়েড মার্চেন্ট • ৩৪২ সফল ডেলিভারি • ১০০% পেমেন্ট সুরক্ষা" else "Verified Merchant • 342 Successful Deliveries • 100% Escrow Protected",
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    color = TextMutedLight
                )
            }
        }
    }
}

@Composable
private fun SellerFinancialCard(
    heldInEscrow: Double,
    availableBalance: Double,
    lang: Language,
    onWithdrawClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.2.dp, BlueBorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = AmberTrust, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.heldInEscrow(lang),
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            color = TextSecondaryLight
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = formatBdt(heldInEscrow),
                        fontFamily = AnekBangla,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberTrust
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.availableBalance(lang),
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            color = TextSecondaryLight
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = formatBdt(availableBalance),
                        fontFamily = AnekBangla,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BluePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onWithdrawClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("seller_withdraw_button")
            ) {
                Icon(Icons.Default.LocalAtm, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.withdrawFunds(lang),
                    fontFamily = AnekBangla,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SellerOrderItem(
    order: TransactionEntity,
    lang: Language,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("seller_order_item_${order.id}"),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.2.dp, BlueBorderLight)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${order.id}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueDark
                    )
                    StatusBadge(state = order.state, lang = lang)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (lang == Language.BANGLA) order.productTitleBn else order.productTitle,
                    fontFamily = AnekBangla,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryLight
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${if (lang == Language.BANGLA) "ক্রেতা: " else "Buyer: "}${order.buyerName}",
                        fontFamily = AnekBangla,
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )
                    Text(
                        text = formatBdt(order.totalAmount),
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BluePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextMutedLight,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
