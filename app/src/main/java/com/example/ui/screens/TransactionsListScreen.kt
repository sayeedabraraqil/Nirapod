package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.entity.TransactionEntity
import com.example.data.model.TransactionState
import com.example.ui.components.EmptyTransactionsGraphic
import com.example.ui.components.NirapodCard
import com.example.ui.components.SparkleBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatBdt
import com.example.ui.components.formatTimestamp
import com.example.ui.localization.Language
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel

@Composable
fun TransactionsListScreen(
    viewModel: EscrowViewModel,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()

    var filterState by remember { mutableStateOf("ALL") }

    val filterOptions = listOf(
        "ALL" to (if (lang == Language.BANGLA) "সকল (${allTransactions.size})" else "All (${allTransactions.size})"),
        "ESCROW" to (if (lang == Language.BANGLA) "এসক্রো ফান্ড" else "In Escrow"),
        "SHIPPED" to (if (lang == Language.BANGLA) "ডেলিভারি প্রক্রিয়ায়" else "Shipped"),
        "COMPLETED" to (if (lang == Language.BANGLA) "সম্পন্ন" else "Completed"),
        "DISPUTED" to (if (lang == Language.BANGLA) "বিরোধ" else "Disputed")
    )

    val filtered = allTransactions.filter { item ->
        when (filterState) {
            "ESCROW" -> item.state == TransactionState.FUNDS_HELD || item.state == TransactionState.PAYMENT_VERIFIED
            "SHIPPED" -> item.state == TransactionState.SHIPPED || item.state == TransactionState.OUT_FOR_DELIVERY
            "COMPLETED" -> item.state == TransactionState.COMPLETED || item.state == TransactionState.SELLER_PAID
            "DISPUTED" -> item.state == TransactionState.DISPUTED || item.state == TransactionState.UNDER_REVIEW
            else -> true
        }
    }

    WarmDecorativeBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("transactions_list_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "আমার লেনদেন ও অর্ডার" else "My Orders & Escrow",
                            fontFamily = AnekBangla,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryLight
                        )
                        SparkleBadge(text = if (lang == Language.BANGLA) "সক্রিয়" else "Active")
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (lang == Language.BANGLA) "নিরাপদ এসক্রো প্রোটোকলের মাধ্যমে প্রতিটি ট্রানজ্যাকশন সুরক্ষিত" else "All orders secured under Nirapod smart escrow contracts",
                        fontFamily = AnekBangla,
                        fontSize = 12.5.sp,
                        color = TextMutedLight
                    )
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { (key, label) ->
                        val isSelected = filterState == key
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isSelected) BluePrimary else Color.White,
                            border = BorderStroke(1.dp, if (isSelected) BluePrimary else BlueBorderLight),
                            shadowElevation = if (isSelected) 2.dp else 1.dp,
                            modifier = Modifier.clickable { filterState = key }
                        ) {
                            Text(
                                text = label,
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondaryLight,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }

            if (filtered.isEmpty()) {
                item {
                    EmptyTransactionsGraphic(
                        lang = lang,
                        actionText = if (lang == Language.BANGLA) "সকল লেনদেন দেখুন" else "Reset Filter",
                        onActionClick = { filterState = "ALL" }
                    )
                }
            } else {
                items(filtered, key = { it.id }) { item ->
                    TransactionRowCard(
                        tx = item,
                        lang = lang,
                        onClick = { onTransactionClick(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionRowCard(
    tx: TransactionEntity,
    lang: Language,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("transaction_row_${tx.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BlueBorderLight)
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
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BlueContainerLight
                    ) {
                        Text(
                            text = "#${tx.id}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueDeep,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                    StatusBadge(state = tx.state, lang = lang)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (lang == Language.BANGLA) tx.productTitleBn else tx.productTitle,
                    fontFamily = AnekBangla,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${if (lang == Language.BANGLA) "মোট প্রদেয়:" else "Total:"} ${formatBdt(tx.totalAmount)}",
                        fontFamily = AnekBangla,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BluePrimary
                    )
                    Text(
                        text = formatTimestamp(tx.createdAt),
                        fontFamily = AnekBangla,
                        fontSize = 11.sp,
                        color = TextMutedLight
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
                shape = CircleShape,
                color = BlueContainerLight,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
