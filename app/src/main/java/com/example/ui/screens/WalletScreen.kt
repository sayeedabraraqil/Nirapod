package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.WithdrawalEntity
import com.example.ui.components.NirapodCard
import com.example.ui.components.NirapodPrimaryButton
import com.example.ui.components.SparkleBadge
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatBdt
import com.example.ui.components.formatTimestamp
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBorder
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VaultBlue
import com.example.ui.theme.VaultBlueBorder
import com.example.ui.theme.VaultBlueContainer
import com.example.ui.viewmodel.EscrowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: EscrowViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val wallet by viewModel.currentWallet.collectAsState()
    val withdrawals by viewModel.currentWithdrawals.collectAsState()
    val context = LocalContext.current

    var showWithdrawDialog by remember { mutableStateOf(false) }
    var withdrawAmountText by remember { mutableStateOf("10000") }
    var withdrawMethod by remember { mutableStateOf("bKash Merchant / Personal") }
    var withdrawAccount by remember { mutableStateOf("01711223344") }

    val held = wallet?.heldFunds ?: 69670.0
    val available = wallet?.availableBalance ?: 182500.0
    val pending = wallet?.pendingPayout ?: 0.0
    val totalEarnings = wallet?.totalEarnings ?: 524000.0

    WarmDecorativeBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("wallet_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Title
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "নিরাপদ ওয়ালেট" else "Nirapod Wallet",
                            fontFamily = AnekBangla,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryLight
                        )
                        SparkleBadge(text = if (lang == Language.BANGLA) "নিরাপদ ট্রাস্ট" else "Nirapod Trust")
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (lang == Language.BANGLA) "বাংলাদেশ ব্যাংক নির্দেশিত এসক্রো ট্রাস্ট ব্যালেন্স" else "Institutional escrow compliant trust account balance",
                        fontFamily = AnekBangla,
                        fontSize = 12.5.sp,
                        color = TextMutedLight
                    )
                }
            }

            // 1. Primary Available Balance Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
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
                                        Color.White
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = SuccessGreenContainer,
                                        border = BorderStroke(1.dp, SuccessGreenBorder),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = SuccessGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (lang == Language.BANGLA) "উত্তোলনযোগ্য ব্যালেন্স" else "Available to Withdraw",
                                            fontFamily = AnekBangla,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryLight
                                        )
                                        Text(
                                            text = if (lang == Language.BANGLA) "তাৎক্ষণিক উত্তোলনযোগ্য" else "Ready for instant payout",
                                            fontFamily = AnekBangla,
                                            fontSize = 11.sp,
                                            color = SuccessGreen,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SuccessGreenContainer,
                                    border = BorderStroke(0.8.dp, SuccessGreenBorder)
                                ) {
                                    Text(
                                        text = if (lang == Language.BANGLA) "উত্তোলনযোগ্য" else "Withdrawable",
                                        fontFamily = AnekBangla,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen,
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = formatBdt(available),
                                fontFamily = AnekBangla,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BluePrimary,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            NirapodPrimaryButton(
                                text = AppStrings.withdrawFunds(lang),
                                onClick = { showWithdrawDialog = true },
                                leadingIcon = Icons.Default.LocalAtm,
                                testTag = "request_payout_button"
                            )
                        }
                    }
                }
            }

            // 2 & 3. Distinct Visual Cards: "Being Protected" & "Pending Payout"
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Being Protected Card (Tranquil Blue Vault Treatment)
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BlueContainerLight),
                        border = BorderStroke(1.2.dp, BlueBorderLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = BluePrimary,
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.8f)
                                ) {
                                    Text(
                                        text = if (lang == Language.BANGLA) "সুরক্ষিত" else "Protected",
                                        fontFamily = AnekBangla,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlueDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (lang == Language.BANGLA) "নিরাপদ ফান্ডে সুরক্ষিত" else "Held in Escrow",
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondaryLight
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = formatBdt(held),
                                fontFamily = AnekBangla,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BlueDeep
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (lang == Language.BANGLA) "ডেলিভারি যাচাই চলছে" else "Pending inspection",
                                fontFamily = AnekBangla,
                                fontSize = 10.5.sp,
                                color = TextMutedLight
                            )
                        }
                    }

                    // Pending Payout Card (Warm Golden Amber Treatment)
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AmberContainer),
                        border = BorderStroke(1.2.dp, AmberTrust.copy(alpha = 0.35f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = AmberTrust,
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.HourglassTop,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.8f)
                                ) {
                                    Text(
                                        text = if (lang == Language.BANGLA) "প্রক্রিয়াকরণ" else "Clearing",
                                        fontFamily = AnekBangla,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberTrust,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (lang == Language.BANGLA) "পেন্ডিং পেআউট" else "Pending Payout",
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondaryLight
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = formatBdt(pending),
                                fontFamily = AnekBangla,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AmberTrust
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (lang == Language.BANGLA) "MFS ট্রানস্ফার প্রক্রিয়াধীন" else "Processing to MFS",
                                fontFamily = AnekBangla,
                                fontSize = 10.5.sp,
                                color = TextMutedLight
                            )
                        }
                    }
                }
            }

            // Trust note
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = BlueContainerLight,
                    border = BorderStroke(1.dp, BlueBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BluePrimary,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (lang == Language.BANGLA)
                                "নিরাপদ এসক্রো নীতিমালা অনুযায়ী ক্রেতার পণ্য গ্রহণ ও সন্তুষ্টি নিশ্চিতকরণের পর ফান্ড স্বয়ংক্রিয়ভাবে উত্তোলনযোগ্য ব্যালেন্সে যোগ হয়।"
                            else "Under Nirapod escrow protocol, funds become instantly withdrawable the moment the buyer inspects and approves delivery.",
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            color = BlueDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Withdrawal History Header
            item {
                Text(
                    text = if (lang == Language.BANGLA) "সাম্প্রতিক পেআউট ও উত্তোলন ইতিহাস" else "Recent Payout History",
                    fontFamily = AnekBangla,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
            }

            if (withdrawals.isEmpty()) {
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
                                text = if (lang == Language.BANGLA) "কোনো পেআউট ইতিহাস নেই" else "No past withdrawals on record.",
                                fontFamily = AnekBangla,
                                fontSize = 13.5.sp,
                                color = TextMutedLight
                            )
                        }
                    }
                }
            } else {
                items(withdrawals, key = { it.id }) { item ->
                    WithdrawalHistoryCard(withdrawal = item, lang = lang)
                }
            }
        }

        // Payout Request Dialog
        if (showWithdrawDialog) {
            AlertDialog(
                onDismissRequest = { showWithdrawDialog = false },
                title = {
                    Text(
                        text = if (lang == Language.BANGLA) "এসক্রো ফান্ড উত্তোলন" else "Withdraw Escrow Funds",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = if (lang == Language.BANGLA)
                                "উপলব্ধ ব্যালেন্স: ${formatBdt(available)}"
                            else "Available: ${formatBdt(available)}",
                            fontFamily = AnekBangla,
                            fontSize = 13.sp,
                            color = BlueDark,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = withdrawAmountText,
                            onValueChange = { withdrawAmountText = it },
                            label = { Text(if (lang == Language.BANGLA) "পরিমাণ (টাকা)" else "Amount (BDT)", fontFamily = AnekBangla) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BlueBorderLight
                            )
                        )

                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val methods = listOf("bKash Merchant / Personal", "Nagad Business", "Rocket / DBBL", "City Bank BEFTN / NPSB", "BRAC Bank Account")

                        Box {
                            OutlinedTextField(
                                value = withdrawMethod,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(if (lang == Language.BANGLA) "পেমেন্ট মাধ্যম" else "Payment Channel", fontFamily = AnekBangla) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { dropdownExpanded = true },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BlueBorderLight
                                )
                            )
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                methods.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m, fontFamily = AnekBangla) },
                                        onClick = {
                                            withdrawMethod = m
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = withdrawAccount,
                            onValueChange = { withdrawAccount = it },
                            label = { Text(if (lang == Language.BANGLA) "হিসাব নম্বর / মোবাইল" else "Account / Phone Number", fontFamily = AnekBangla) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BlueBorderLight
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = withdrawAmountText.toDoubleOrNull() ?: 0.0
                            if (amt <= 0 || amt > available) {
                                Toast.makeText(context, "Invalid amount or exceeds available balance", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.requestWithdrawal(amt, withdrawMethod, withdrawAccount) { ok ->
                                    if (ok) {
                                        Toast.makeText(context, "Payout request submitted via BEFTN/MFS", Toast.LENGTH_SHORT).show()
                                        showWithdrawDialog = false
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("submit_payout_dialog_button")
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "উত্তোলন নিশ্চিত করুন" else "Confirm Withdrawal",
                            fontFamily = AnekBangla,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showWithdrawDialog = false }) {
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
}

@Composable
private fun WithdrawalHistoryCard(withdrawal: WithdrawalEntity, lang: Language) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BlueBorderLight)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (withdrawal.status == "COMPLETED") SuccessGreenContainer
                        else AmberContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (withdrawal.status == "COMPLETED") Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = if (withdrawal.status == "COMPLETED") SuccessGreen else AmberTrust,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = withdrawal.paymentMethod,
                        fontFamily = AnekBangla,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Text(
                        text = formatBdt(withdrawal.amount),
                        fontFamily = AnekBangla,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BluePrimary
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "A/C: ${withdrawal.accountNumber}",
                        fontFamily = AnekBangla,
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )
                    Text(
                        text = formatTimestamp(withdrawal.requestedAt),
                        fontFamily = AnekBangla,
                        fontSize = 11.sp,
                        color = TextMutedLight
                    )
                }
            }
        }
    }
}
