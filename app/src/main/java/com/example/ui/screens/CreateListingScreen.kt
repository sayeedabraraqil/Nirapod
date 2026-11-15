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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductType
import com.example.ui.components.NirapodCard
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.localization.Language
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CanvasLight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    viewModel: EscrowViewModel,
    onBackClick: () -> Unit,
    onListingCreated: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()

    var title by remember { mutableStateOf("") }
    var titleBn by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Electronics & PC") }
    var priceText by remember { mutableStateOf("") }
    var deliveryFeeText by remember { mutableStateOf("100") }
    var description by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf(ProductType.PHYSICAL) }
    var digitalSecret by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (lang == Language.BANGLA) "নতুন এসক্রো লিস্টিং তৈরি করুন" else "Create Escrow Listing",
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
                // Product Type Picker
                NirapodCard(
                    backgroundColor = Color.White,
                    borderColor = BlueBorderLight
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "পণ্যের ধরন নির্বাচন করুন:" else "Select Product Type:",
                        fontFamily = AnekBangla,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = productType == ProductType.PHYSICAL,
                            onClick = { productType = ProductType.PHYSICAL },
                            shape = RoundedCornerShape(12.dp),
                            label = {
                                Text(
                                    text = if (lang == Language.BANGLA) "ভৌত পণ্য (কুরিয়ার)" else "Physical Goods (Courier)",
                                    fontFamily = AnekBangla,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (productType == ProductType.PHYSICAL) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BluePrimary,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White,
                                containerColor = BlueContainerLight.copy(alpha = 0.5f),
                                labelColor = TextSecondaryLight
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = productType == ProductType.PHYSICAL,
                                borderColor = if (productType == ProductType.PHYSICAL) BluePrimary else BlueBorderLight
                            )
                        )
                        FilterChip(
                            selected = productType == ProductType.DIGITAL_KEY,
                            onClick = { productType = ProductType.DIGITAL_KEY },
                            shape = RoundedCornerShape(12.dp),
                            label = {
                                Text(
                                    text = if (lang == Language.BANGLA) "ডিজিটাল কি (লাইসেন্স)" else "Digital Key / License",
                                    fontFamily = AnekBangla,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (productType == ProductType.DIGITAL_KEY) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BluePrimary,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White,
                                containerColor = BlueContainerLight.copy(alpha = 0.5f),
                                labelColor = TextSecondaryLight
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = productType == ProductType.DIGITAL_KEY,
                                borderColor = if (productType == ProductType.DIGITAL_KEY) BluePrimary else BlueBorderLight
                            )
                        )
                    }
                }

                // Product Details Inputs
                NirapodCard(
                    backgroundColor = Color.White,
                    borderColor = BlueBorderLight
                ) {
                    Text(
                        text = if (lang == Language.BANGLA) "পণ্যের মৌলিক তথ্য" else "Product Information",
                        fontFamily = AnekBangla,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Product Title (English)", fontFamily = AnekBangla) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("create_title_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = titleBn,
                        onValueChange = { titleBn = it },
                        label = { Text("পণ্যের নাম (বাংলা)", fontFamily = AnekBangla) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Price (৳ BDT)", fontFamily = AnekBangla) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("create_price_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BlueBorderLight
                            )
                        )

                        if (productType == ProductType.PHYSICAL) {
                            OutlinedTextField(
                                value = deliveryFeeText,
                                onValueChange = { deliveryFeeText = it },
                                label = { Text("Delivery (৳)", fontFamily = AnekBangla) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BlueBorderLight
                                )
                            )
                        }
                    }

                    if (productType == ProductType.DIGITAL_KEY) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = digitalSecret,
                            onValueChange = { digitalSecret = it },
                            label = { Text("Secret License Key / Download Link (Encrypted Delivery)", fontFamily = AnekBangla) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BlueBorderLight
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(if (lang == Language.BANGLA) "পণ্যের বিস্তারিত বিবরণ" else "Product Description", fontFamily = AnekBangla) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        )
                    )
                }

                Button(
                    onClick = {
                        val price = priceText.toDoubleOrNull() ?: 500.0
                        val delFee = if (productType == ProductType.PHYSICAL) (deliveryFeeText.toDoubleOrNull() ?: 100.0) else 0.0
                        viewModel.createListing(
                            title = title.ifBlank { "Custom Nirapod Listing" },
                            titleBn = titleBn.ifBlank { title.ifBlank { "কাস্টম নিরাপদ পণ্য" } },
                            description = description.ifBlank { "Standard verified listing protected by Nirapod protocol." },
                            category = category,
                            price = price,
                            deliveryFee = delFee,
                            productType = productType,
                            digitalSecret = digitalSecret.ifBlank { "KEY-LIC-" + (10000..99999).random() }
                        ) { newId ->
                            onListingCreated(newId)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp)
                        .testTag("submit_create_listing_button")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lang == Language.BANGLA) "তালিকা প্রকাশ ও নিরাপদ সুরক্ষা সক্রিয় করুন" else "Publish with Nirapod Protection",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
