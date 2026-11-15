package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DisputeReason
import com.example.data.model.DisputeStatus
import com.example.ui.components.NirapodCard
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatTimestamp
import com.example.ui.localization.Language
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CanvasLight
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CrimsonContainer
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisputeScreen(
    transactionId: String,
    viewModel: EscrowViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val dispute by viewModel.selectedDispute.collectAsState()

    var selectedReason by remember { mutableStateOf(DisputeReason.DEFECTIVE) }
    var reasonExpanded by remember { mutableStateOf(false) }
    var descriptionText by remember {
        mutableStateOf(
            if (lang == Language.BANGLA)
                "পার্সেল খুলে দেখা গেছে প্রডাক্টটির মডেল ভুল এবং প্যাকেজের সিল খোলা ছিল।"
                else "Package opened; serial number does not match listing specifications."
        )
    }
    var evidenceText by remember {
        mutableStateOf(
            "Unboxing Video Link: drive.google.com/nirapod_evidence_01\nCourier Receipt #: STD-889102"
        )
    }

    var sellerResponseText by remember { mutableStateOf("") }
    var adminResolutionNotes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (lang == Language.BANGLA) "বিরোধ নিষ্পত্তি ও সালিশি কেন্দ্র" else "Dispute & Arbitration Desk",
                            fontFamily = AnekBangla,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Text(
                            text = "Order #$transactionId",
                            fontFamily = FontFamily.Monospace,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
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
                // Escrow Freeze Notice
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF1F1),
                    border = BorderStroke(1.2.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = CrimsonAlert,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (lang == Language.BANGLA) "নিরাপদ ফান্ড তাৎক্ষণিক স্থগিত ও সুরক্ষিত" else "Nirapod Funds Strictly Frozen",
                                fontFamily = AnekBangla,
                                fontWeight = FontWeight.Bold,
                                color = CrimsonAlert,
                                fontSize = 14.5.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = if (lang == Language.BANGLA)
                                    "বিরোধ চলাকালীন সময়ে কোনো অর্থ কাউকে দেওয়া হবে না। উভয় পক্ষের প্রমাণ নিরীক্ষা করে নিরাপদ ট্রাস্টি সালিশকারী সিদ্ধান্ত গ্রহণ করবে।"
                                    else "During an active dispute, funds remain safely locked in Nirapod Escrow until official arbitration resolution.",
                                fontFamily = AnekBangla,
                                fontSize = 12.sp,
                                color = Color(0xFF7F1D1D),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                if (dispute == null) {
                    // Form to File Dispute
                    NirapodCard(
                        backgroundColor = Color.White,
                        borderColor = BlueBorderLight
                    ) {
                        Text(
                            text = if (lang == Language.BANGLA) "নতুন বিতর্ক দায়ের করুন" else "Open Structured Dispute",
                            fontFamily = AnekBangla,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (lang == Language.BANGLA) "বিতর্কের কারণ নির্বাচন করুন:" else "Select Dispute Category:",
                            fontFamily = AnekBangla,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { reasonExpanded = true },
                                shape = RoundedCornerShape(12.dp),
                                color = BlueContainerLight.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, BlueBorderLight)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (lang == Language.BANGLA) selectedReason.titleBn else selectedReason.titleEn,
                                        fontFamily = AnekBangla,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryLight
                                    )
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = BluePrimary)
                                }
                            }

                            DropdownMenu(
                                expanded = reasonExpanded,
                                onDismissRequest = { reasonExpanded = false }
                            ) {
                                DisputeReason.values().forEach { reason ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = if (lang == Language.BANGLA) reason.titleBn else reason.titleEn,
                                                fontFamily = AnekBangla
                                            )
                                        },
                                        onClick = {
                                            selectedReason = reason
                                            reasonExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = descriptionText,
                            onValueChange = { descriptionText = it },
                            label = { Text(if (lang == Language.BANGLA) "সমস্যার বিস্তারিত বিবরণ" else "Detailed Problem Statement", fontFamily = AnekBangla) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BlueBorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = evidenceText,
                            onValueChange = { evidenceText = it },
                            label = { Text(if (lang == Language.BANGLA) "প্রমাণাদি / আনবক্সিং ভিডিও লিংক" else "Evidence / Unboxing Video Links", fontFamily = AnekBangla) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BlueBorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                viewModel.fileDispute(
                                    trxId = transactionId,
                                    reason = selectedReason,
                                    description = descriptionText,
                                    evidence = evidenceText
                                ) {}
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .testTag("submit_dispute_button")
                        ) {
                            Icon(Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == Language.BANGLA) "বিতর্ক সাবমিট করুন" else "File Formal Dispute",
                                fontFamily = AnekBangla,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    val d = dispute!!
                    NirapodCard(
                        backgroundColor = Color.White,
                        borderColor = BlueBorderLight
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dispute #${d.id}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (d.status == DisputeStatus.RESOLVED_BUYER_REFUND || d.status == DisputeStatus.RESOLVED_SELLER_RELEASE)
                                    SuccessGreen.copy(alpha = 0.15f) else Color(0xFFFFF1F1)
                            ) {
                                Text(
                                    text = if (lang == Language.BANGLA) d.status.titleBn else d.status.titleEn,
                                    fontFamily = AnekBangla,
                                    color = if (d.status == DisputeStatus.RESOLVED_BUYER_REFUND || d.status == DisputeStatus.RESOLVED_SELLER_RELEASE)
                                        SuccessGreen else CrimsonAlert,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${if (lang == Language.BANGLA) "কারণ: " else "Reason: "}${if (lang == Language.BANGLA) d.reason.titleBn else d.reason.titleEn}",
                            fontFamily = AnekBangla,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimaryLight
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Opened by ${d.openedByName} at ${formatTimestamp(d.createdAt)}",
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            color = TextMutedLight
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = d.description,
                            fontFamily = AnekBangla,
                            fontSize = 13.sp,
                            color = TextSecondaryLight,
                            lineHeight = 20.sp
                        )

                        if (d.evidenceNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = BlueContainerLight.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, BlueBorderLight),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Evidence:\n${d.evidenceNotes}",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.5.sp,
                                    color = TextSecondaryLight,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        if (d.sellerResponse.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BlueBorderLight)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (lang == Language.BANGLA) "বিক্রেতার আনুষ্ঠানিক বক্তব্য:" else "Seller Counter-Response:",
                                fontFamily = AnekBangla,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = TextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = d.sellerResponse,
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                color = TextSecondaryLight
                            )
                        } else if (role == UserRole.SELLER) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BlueBorderLight)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (lang == Language.BANGLA) "বিক্রেতার আনুষ্ঠানিক উত্তর দিন:" else "Submit Seller Counter-Evidence:",
                                fontFamily = AnekBangla,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = TextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = sellerResponseText,
                                onValueChange = { sellerResponseText = it },
                                label = { Text("Response & Dispatch Proof", fontFamily = AnekBangla) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BlueBorderLight
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    viewModel.respondToDispute(transactionId, sellerResponseText, "Consignment Dispatch Slip verified")
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                            ) {
                                Text("Submit Response", fontFamily = AnekBangla, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (d.resolutionNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BlueBorderLight)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Gavel, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Arbitrator Resolution (${formatTimestamp(d.resolvedAt)}):",
                                    fontFamily = AnekBangla,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = BlueDark
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = d.resolutionNotes,
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                color = TextSecondaryLight,
                                lineHeight = 18.sp
                            )
                        } else if (role == UserRole.ADMIN) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BlueBorderLight)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Admin Arbitration Tools:",
                                fontFamily = AnekBangla,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = TextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = adminResolutionNotes,
                                onValueChange = { adminResolutionNotes = it },
                                label = { Text("Findings & Decision Reason", fontFamily = AnekBangla) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BlueBorderLight
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.adminResolveDispute(
                                            d.id,
                                            DisputeStatus.RESOLVED_BUYER_REFUND,
                                            adminResolutionNotes.ifBlank { "Evidence confirmed item defect. 100% refund ordered to buyer." }
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                                    modifier = Modifier
                                        .weight(1f)
                                        .defaultMinSize(minHeight = 44.dp)
                                ) {
                                    Text("Refund Buyer", fontFamily = AnekBangla, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = {
                                        viewModel.adminResolveDispute(
                                            d.id,
                                            DisputeStatus.RESOLVED_SELLER_RELEASE,
                                            adminResolutionNotes.ifBlank { "Delivery verified by courier geolocation. Payment released to seller." }
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    modifier = Modifier
                                        .weight(1f)
                                        .defaultMinSize(minHeight = 44.dp)
                                ) {
                                    Text("Release to Seller", fontFamily = AnekBangla, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
