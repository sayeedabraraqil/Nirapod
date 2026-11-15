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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.entity.AuditLogEntity
import com.example.data.entity.DisputeEntity
import com.example.ui.components.EmptyDisputesGraphic
import com.example.ui.components.NirapodCard
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatTimestamp
import com.example.ui.localization.Language
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
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CrimsonContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceLight
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel

@Composable
fun AdminOperationsScreen(
    viewModel: EscrowViewModel,
    onOpenDispute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val disputes by viewModel.allDisputes.collectAsState()
    val auditLogs by viewModel.allAuditLogs.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        if (lang == Language.BANGLA) "নিরাপদ ওভারভিউ" else "Nirapod Overview",
        if (lang == Language.BANGLA) "সক্রিয় বিতর্ক (${disputes.size})" else "Disputes (${disputes.size})",
        if (lang == Language.BANGLA) "অডিট লগ" else "Audit Log"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasLight)
            .testTag("admin_operations_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = BluePrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = BluePrimary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontFamily = AnekBangla,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) BluePrimary else TextSecondaryLight
                        )
                    }
                )
            }
        }

        WarmDecorativeBackground(modifier = Modifier.fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> EscrowMetricsTab(
                    txCount = allTransactions.size,
                    disputeCount = disputes.size,
                    lang = lang
                )
                1 -> DisputesListTab(
                    disputes = disputes,
                    lang = lang,
                    onDisputeClick = { onOpenDispute(it.transactionId) }
                )
                2 -> AuditLogsListTab(
                    logs = auditLogs,
                    lang = lang
                )
            }
        }
    }
}

@Composable
private fun EscrowMetricsTab(
    txCount: Int,
    disputeCount: Int,
    lang: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BlueContainerLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (lang == Language.BANGLA) "নিরাপদ এসক্রো স্বাস্থ্য ও মূলধন সুরক্ষা" else "Nirapod Escrow Health & Solvency",
                                fontFamily = AnekBangla,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                            Text(
                                text = if (lang == Language.BANGLA) "বাংলাদেশ ব্যাংক নির্দেশিত এসক্রো হিসাব" else "Segregated trust accounts compliant with BB guidelines",
                                fontFamily = AnekBangla,
                                fontSize = 11.sp,
                                color = TextMutedLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "৳২,৪৮,৫০০",
                                fontFamily = AnekBangla,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BluePrimary
                            )
                            Text(
                                text = if (lang == Language.BANGLA) "নিরাপদে রক্ষিত মূলধন" else "Total in Nirapod Escrow",
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                color = TextSecondaryLight
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$txCount",
                                fontFamily = AnekBangla,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimaryLight
                            )
                            Text(
                                text = if (lang == Language.BANGLA) "মোট লেনদেন" else "Total Orders",
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                color = TextSecondaryLight
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$disputeCount",
                                fontFamily = AnekBangla,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CrimsonAlert
                            )
                            Text(
                                text = if (lang == Language.BANGLA) "বিতর্ক সংখ্যা" else "Disputes",
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                color = TextSecondaryLight
                            )
                        }
                    }
                }
            }
        }

        item {
            NirapodCard(backgroundColor = SurfaceLight) {
                Text(
                    text = if (lang == Language.BANGLA) "আরবিট্রেশন প্রোটোকল ও সম্মতি বিধিমালা" else "Arbitration & Compliance Rules",
                    fontFamily = AnekBangla,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (lang == Language.BANGLA)
                        "• ১০০% পৃথক ব্যাংক ও এমএফএস এসক্রো অ্যাকাউন্ট (ক্লায়েন্ট মানি প্রোটেকশন নিয়মাবলী)।\n" +
                        "• ক্ষতিগ্রস্ত পণ্যের দাবির ক্ষেত্রে বাধ্যতামূলক আনবক্সিং ভিডিও প্রমাণ যাচাই।\n" +
                        "• কুরিয়ার এপিআই ওয়েবহুক স্ট্যাটাস এবং ক্রেতার পণ্য গ্রহণের ওটিপি সমন্বয়।\n" +
                        "• ডিজিটাল লাইসেন্স ডিক্রিপশন টাইমস্ট্যাম্প ও অডিট ট্রেইল সংরক্ষণ।"
                        else "• 100% segregated bank & MFS escrow accounts (Client Money Protection Rules).\n" +
                        "• Unboxing video verification strictly required for physical defect claims.\n" +
                        "• Courier API webhook verification cross-referenced with buyer delivery OTP acknowledgment.\n" +
                        "• Digital license decryption timestamps permanently logged in cryptographic audit trail.",
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextSecondaryLight
                )
            }
        }
    }
}

@Composable
private fun DisputesListTab(
    disputes: List<DisputeEntity>,
    lang: Language,
    onDisputeClick: (DisputeEntity) -> Unit
) {
    if (disputes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyDisputesGraphic(lang = lang)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(disputes, key = { it.id }) { d ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDisputeClick(d) },
                shape = RoundedCornerShape(12.dp),
                color = SurfaceLight,
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dispute #${d.id} • Order #${d.transactionId}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueDark
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CrimsonContainer
                        ) {
                            Text(
                                text = if (lang == Language.BANGLA) d.status.titleBn else d.status.titleEn,
                                fontFamily = AnekBangla,
                                color = CrimsonAlert,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (lang == Language.BANGLA) d.reason.titleBn else d.reason.titleEn,
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextPrimaryLight
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = d.description,
                        fontFamily = AnekBangla,
                        fontSize = 12.sp,
                        color = TextSecondaryLight,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Claimant: ${d.openedByName}",
                            fontFamily = AnekBangla,
                            fontSize = 11.sp,
                            color = TextMutedLight
                        )
                        Text(
                            text = formatTimestamp(d.createdAt),
                            fontFamily = AnekBangla,
                            fontSize = 10.sp,
                            color = TextMutedLight
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditLogsListTab(
    logs: List<AuditLogEntity>,
    lang: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(logs, key = { it.id }) { log ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = SurfaceLight,
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BlueContainerLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = log.action,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlueDark
                            )
                            Text(
                                text = formatTimestamp(log.timestamp),
                                fontFamily = AnekBangla,
                                fontSize = 10.sp,
                                color = TextMutedLight
                            )
                        }
                        Text(
                            text = log.details,
                            fontFamily = AnekBangla,
                            fontSize = 12.sp,
                            color = TextPrimaryLight,
                            lineHeight = 16.sp
                        )
                        Text(
                            text = "Actor: ${log.performedBy} • Order: #${log.transactionId}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMutedLight
                        )
                    }
                }
            }
        }
    }
}
