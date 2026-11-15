package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionState
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.theme.AmberTrust
import com.example.ui.theme.AmberTrustBorder
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
import com.example.ui.theme.EmeraldContainerLight
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBorder
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.TextDisabledLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VaultBlue
import com.example.ui.theme.VaultBlueBorder
import com.example.ui.theme.VaultBlueContainer
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ==========================================
// UTILITY HELPERS
// ==========================================

fun formatBdt(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "৳" + formatter.format(amount.toLong())
}

fun formatTimestamp(timestamp: Long): String {
    if (timestamp <= 0) return "--"
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
    return sdf.format(Date(timestamp))
}

// ==========================================
// DECORATIVE WARM AMBIENCE & GRAPHICS
// ==========================================

@Composable
fun WarmDecorativeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasLight)
    ) {
        // Soft glowing radial sky-blue aura in top right & mid-left
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = this.size.width
            val canvasHeight = this.size.height

            // Top-right soft sky-blue sunburst aura (inspired by reference image)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x55BAE0FD),
                        Color(0x2893C5FD),
                        Color(0x00EBF5FE)
                    ),
                    center = androidx.compose.ui.geometry.Offset(canvasWidth * 0.9f, canvasHeight * 0.08f),
                    radius = canvasWidth * 0.75f
                ),
                center = androidx.compose.ui.geometry.Offset(canvasWidth * 0.9f, canvasHeight * 0.08f),
                radius = canvasWidth * 0.75f
            )

            // Mid-left subtle ice-blue accent glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x357DD3FC),
                        Color(0x15BAE0FD),
                        Color(0x00EBF5FE)
                    ),
                    center = androidx.compose.ui.geometry.Offset(canvasWidth * 0.05f, canvasHeight * 0.45f),
                    radius = canvasWidth * 0.6f
                ),
                center = androidx.compose.ui.geometry.Offset(canvasWidth * 0.05f, canvasHeight * 0.45f),
                radius = canvasWidth * 0.6f
            )
        }

        content()
    }
}

@Composable
fun SparkleBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BlueContainerLight,
    textColor: Color = BlueDark,
    borderColor: Color = BlueBorderLight
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✦",
                fontSize = 11.sp,
                color = BluePrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                fontFamily = AnekBangla,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

// ==========================================
// BRAND LOGO COMPONENT
// ==========================================

@Composable
fun NirapodBrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    showText: Boolean = true,
    lang: Language = Language.BANGLA
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BlueBorderLight),
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF0284C7),
                                Color(0xFF0EA5E9),
                                Color(0xFF38BDF8)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GppGood,
                    contentDescription = AppStrings.appName(lang),
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.65f)
                )
            }
        }
        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = AppStrings.appName(lang),
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        lineHeight = 23.sp,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "✦",
                        fontSize = 11.sp,
                        color = BluePrimary
                    )
                }
                Text(
                    text = if (lang == Language.BANGLA) "স্মার্ট এসক্রো সুরক্ষা" else "SMART ESCROW PROTECTION",
                    fontFamily = AnekBangla,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 0.9.sp,
                    color = BluePrimary
                )
            }
        }
    }
}

// ==========================================
// UNCLIPPED RESPONSIVE LANGUAGE SWITCHER
// ==========================================

@Composable
fun NirapodLanguageSwitcher(
    currentLanguage: Language,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "language_toggle_button"
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(14.dp),
        color = BlueContainerLight,
        border = BorderStroke(1.2.dp, BlueBorderLight),
        shadowElevation = 0.dp,
        modifier = modifier
            .defaultMinSize(minWidth = 78.dp, minHeight = 40.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Switch language between বাংলা and English",
                tint = BluePrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "বাং",
                    fontFamily = AnekBangla,
                    fontSize = 12.5.sp,
                    fontWeight = if (currentLanguage == Language.BANGLA) FontWeight.ExtraBold else FontWeight.Medium,
                    color = if (currentLanguage == Language.BANGLA) BlueDark else TextMutedLight
                )
                Text(
                    text = " | ",
                    fontFamily = AnekBangla,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueBorderLight
                )
                Text(
                    text = "EN",
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    fontWeight = if (currentLanguage == Language.ENGLISH) FontWeight.ExtraBold else FontWeight.Medium,
                    color = if (currentLanguage == Language.ENGLISH) BlueDark else TextMutedLight
                )
            }
        }
    }
}

// ==========================================
// PREMIUM BUTTON SYSTEM
// ==========================================

@Composable
fun NirapodPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    testTag: String = "primary_button"
) {
    Surface(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (enabled && !isLoading) 4.dp else 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 50.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .background(
                    if (enabled && !isLoading) {
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0284C7),
                                Color(0xFF0EA5E9),
                                Color(0xFF38BDF8)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFCFE0EE),
                                Color(0xFFCFE0EE)
                            )
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = text,
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NirapodSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    testTag: String = "secondary_button"
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF0F7FD),
        border = BorderStroke(1.5.dp, if (enabled) BlueBorderLight else Color(0xFFD3E4F2)),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = AnekBangla,
                fontWeight = FontWeight.Bold,
                fontSize = 14.5.sp,
                color = if (enabled) BlueDark else TextDisabledLight,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NirapodDestructiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = Icons.Default.Warning,
    testTag: String = "destructive_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CrimsonAlert,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = AnekBangla,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==========================================
// PREMIUM CARDS SYSTEM
// ==========================================

@Composable
fun NirapodCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    backgroundColor: Color = Color.White,
    borderColor: Color = BorderLight,
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}

// ==========================================
// PREMIUM TEXT INPUT FIELD
// ==========================================

@Composable
fun NirapodTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    testTag: String = "text_field"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = AnekBangla,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = TextSecondaryLight,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            placeholder = {
                if (placeholder != null) {
                    Text(
                        text = placeholder,
                        fontFamily = AnekBangla,
                        fontSize = 14.sp,
                        color = TextMutedLight
                    )
                }
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) CrimsonAlert else BluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null,
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(16.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = AnekBangla,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = TextPrimaryLight
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFF5FAFE),
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = BorderLight,
                errorBorderColor = CrimsonAlert,
                errorContainerColor = Color(0xFFFFEEEE)
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                fontFamily = AnekBangla,
                fontSize = 11.sp,
                color = CrimsonAlert,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

// ==========================================
// REDESIGNED STATUS BADGE SYSTEM
// ==========================================

@Composable
fun StatusBadge(state: TransactionState, lang: Language, modifier: Modifier = Modifier) {
    val (bgColor, borderColor, textColor, icon) = when (state) {
        TransactionState.FUNDS_HELD,
        TransactionState.PAYMENT_VERIFIED -> Quad(VaultBlueContainer, VaultBlueBorder, VaultBlue, Icons.Default.Lock)

        TransactionState.COMPLETED,
        TransactionState.SELLER_PAID -> Quad(SuccessGreenContainer, SuccessGreenBorder, SuccessGreen, Icons.Default.CheckCircle)

        TransactionState.SHIPPED,
        TransactionState.OUT_FOR_DELIVERY -> Quad(Color(0xFFEDE9FE), Color(0xFFDDD6FE), Color(0xFF6D28D9), Icons.Default.LocalShipping)

        TransactionState.BUYER_CONFIRMATION_PENDING,
        TransactionState.PROCESSING,
        TransactionState.SELLER_ACCEPTED -> Quad(AmberTrustContainer, AmberTrustBorder, AmberTrust, Icons.Default.HourglassTop)

        TransactionState.DISPUTED,
        TransactionState.UNDER_REVIEW,
        TransactionState.DELIVERY_FAILED -> Quad(CrimsonAlertContainer, CrimsonAlertBorder, CrimsonAlert, Icons.Default.Warning)

        TransactionState.REFUNDED -> Quad(Color(0xFFFCE7F3), Color(0xFFFBCFE8), Color(0xFFBE185D), Icons.Default.Check)

        else -> Quad(Color(0xFFF1F5F9), Color(0xFFE2E8F0), Color(0xFF475569), Icons.Default.Info)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = if (lang == Language.BANGLA) state.displayNameBn else state.displayNameEn,
                color = textColor,
                fontFamily = AnekBangla,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp
            )
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun RiskBadge(risk: RiskLevel) {
    val (bg, border, fg) = when (risk) {
        RiskLevel.LOW -> Triple(SuccessGreenContainer, SuccessGreenBorder, SuccessGreen)
        RiskLevel.MEDIUM -> Triple(AmberTrustContainer, AmberTrustBorder, AmberTrust)
        RiskLevel.HIGH, RiskLevel.CRITICAL -> Triple(CrimsonAlertContainer, CrimsonAlertBorder, CrimsonAlert)
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg,
        border = BorderStroke(1.dp, border)
    ) {
        Text(
            text = risk.title,
            color = fg,
            fontFamily = AnekBangla,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

// ==========================================
// REDESIGNED TRANSACTION ESCROW FLOW VISUALIZER
// ==========================================

@Composable
fun EscrowFlowVisualizer(
    state: TransactionState,
    lang: Language,
    modifier: Modifier = Modifier
) {
    // 6 Canonical Steps:
    // 1. Buyer Paid
    // 2. Escrow Locked
    // 3. Seller Dispatched
    // 4. Delivery Courier
    // 5. Buyer Inspection
    // 6. Seller Released
    val steps = listOf(
        FlowStep("ক্রেতা", "Buyer", Icons.Default.ShoppingBag, 1),
        FlowStep("এসক্রো লক", "Escrow Lock", Icons.Default.Lock, 2),
        FlowStep("বিক্রেতা", "Seller", Icons.Default.Store, 3),
        FlowStep("কুরিয়ার", "Courier", Icons.Default.LocalShipping, 4),
        FlowStep("যাচাই", "Inspect", Icons.Default.VerifiedUser, 5),
        FlowStep("ফান্ড রিলিজ", "Released", Icons.Default.AccountBalance, 6)
    )

    val currentStepNumber = when (state) {
        TransactionState.ORDER_CREATED,
        TransactionState.PAYMENT_PENDING -> 1

        TransactionState.PAYMENT_VERIFIED,
        TransactionState.FUNDS_HELD,
        TransactionState.SELLER_NOTIFIED -> 2

        TransactionState.SELLER_ACCEPTED,
        TransactionState.PROCESSING -> 3

        TransactionState.SHIPPED,
        TransactionState.OUT_FOR_DELIVERY,
        TransactionState.DELIVERED -> 4

        TransactionState.BUYER_CONFIRMATION_PENDING,
        TransactionState.DISPUTED,
        TransactionState.UNDER_REVIEW -> 5

        TransactionState.COMPLETED,
        TransactionState.SELLER_PAYOUT_PENDING,
        TransactionState.SELLER_PAID,
        TransactionState.REFUNDED -> 6

        else -> 2
    }

    NirapodCard(
        modifier = modifier,
        backgroundColor = Color.White
    ) {
        // Top Assurance Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BlueContainerLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (lang == Language.BANGLA) "নিরাপদ এসক্রো প্রবাহ" else "Nirapod Escrow Lifecycle",
                        fontFamily = AnekBangla,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimaryLight
                    )
                    Text(
                        text = if (lang == Language.BANGLA) "১০০% অর্থ নিরাপদ সুরক্ষায় সংরক্ষিত" else "100% Funds Secured by Nirapod",
                        fontFamily = AnekBangla,
                        fontSize = 11.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            StatusBadge(state = state, lang = lang)
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = BorderLight)
        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Connected Node Stepper
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = step.stepNumber < currentStepNumber
                val isCurrent = step.stepNumber == currentStepNumber
                val isFuture = step.stepNumber > currentStepNumber

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> SuccessGreenContainer
                                    isCurrent -> BluePrimary
                                    else -> BlueContainerLight
                                }
                            )
                            .border(
                                width = if (isCurrent) 2.5.dp else 1.dp,
                                color = when {
                                    isCompleted -> SuccessGreen
                                    isCurrent -> BlueDark
                                    else -> BlueBorderLight
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.Check else step.icon,
                            contentDescription = null,
                            tint = when {
                                isCompleted -> SuccessGreen
                                isCurrent -> Color.White
                                else -> TextMutedLight
                            },
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (lang == Language.BANGLA) step.titleBn else step.titleEn,
                        fontFamily = AnekBangla,
                        fontSize = 10.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) BluePrimary else TextMutedLight,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Connecting Line between nodes
                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(2.dp)
                            .background(
                                if (step.stepNumber < currentStepNumber) SuccessGreen
                                else BorderLight
                            )
                    )
                }
            }
        }
    }
}

private data class FlowStep(
    val titleBn: String,
    val titleEn: String,
    val icon: ImageVector,
    val stepNumber: Int
)

// ==========================================
// TRUST BANNER
// ==========================================

@Composable
fun TrustShieldBanner(
    lang: Language,
    modifier: Modifier = Modifier
) {
    NirapodCard(
        modifier = modifier,
        backgroundColor = BlueContainerLight,
        borderColor = BlueBorderLight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GppGood,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = if (lang == Language.BANGLA) "নিরাপদ ক্রেতা ও বিক্রেতা সুরক্ষা" else "Nirapod 100% Escrow Guarantee",
                    fontFamily = AnekBangla,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BlueDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (lang == Language.BANGLA)
                        "পণ্য হাতে পেয়ে যাচাই না করা পর্যন্ত পেমেন্ট নিরাপদে সুরক্ষিত থাকে।"
                        else "Funds are locked with Nirapod and only released once the buyer inspects and approves delivery.",
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = TextSecondaryLight
                )
            }
        }
    }
}

// Backward compatibility overload
@Composable
fun TrustShieldBanner(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    NirapodCard(
        modifier = modifier,
        backgroundColor = BlueContainerLight,
        borderColor = BlueBorderLight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GppGood,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = AnekBangla,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BlueDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontFamily = AnekBangla,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = TextSecondaryLight
                )
            }
        }
    }
}

// ==========================================
// FINANCIAL BREAKDOWN CARD
// ==========================================

@Composable
fun FinancialBreakdownCard(
    price: Double,
    deliveryFee: Double,
    protectionFee: Double,
    total: Double,
    lang: Language,
    modifier: Modifier = Modifier
) {
    NirapodCard(modifier = modifier) {
        Text(
            text = if (lang == Language.BANGLA) "স্বচ্ছ আর্থিক হিসাব ও ফি বিবরণী" else "Transparent Fee Breakdown",
            fontFamily = AnekBangla,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = TextPrimaryLight
        )
        Spacer(modifier = Modifier.height(12.dp))

        FeeRow(
            label = AppStrings.productPrice(lang),
            amount = formatBdt(price)
        )
        if (deliveryFee > 0) {
            FeeRow(
                label = AppStrings.courierDeliveryFee(lang),
                amount = formatBdt(deliveryFee)
            )
        }
        FeeRow(
            label = (if (lang == Language.BANGLA) "এসক্রো প্ল্যাটফর্ম প্রটেকশন ফি (১.৫%)" else "Escrow Protection Fee (1.5%)"),
            amount = formatBdt(protectionFee),
            isHighlight = true
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = BorderLight
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.totalPayable(lang),
                fontFamily = AnekBangla,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimaryLight
            )
            Text(
                text = formatBdt(total),
                fontFamily = AnekBangla,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = BluePrimary
            )
        }
    }
}

@Composable
private fun FeeRow(label: String, amount: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = AnekBangla,
            fontSize = 13.sp,
            color = if (isHighlight) BluePrimary else TextSecondaryLight
        )
        Text(
            text = amount,
            fontFamily = AnekBangla,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) BluePrimary else TextPrimaryLight
        )
    }
}

// ==========================================
// TIMELINE STEP ITEM
// ==========================================

@Composable
fun TimelineStepItem(
    title: String,
    description: String,
    timestamp: Long,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> SuccessGreen
                            isCurrent -> AmberTrust
                            else -> Color(0xFFD6E4F0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(48.dp)
                        .background(
                            if (isCompleted) SuccessGreen.copy(alpha = 0.5f)
                            else BorderLight
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = AnekBangla,
                    fontSize = 14.sp,
                    fontWeight = if (isCurrent || isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isCurrent) BluePrimary else TextPrimaryLight
                )
                if (timestamp > 0) {
                    Text(
                        text = formatTimestamp(timestamp),
                        fontFamily = AnekBangla,
                        fontSize = 11.sp,
                        color = TextMutedLight
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontFamily = AnekBangla,
                fontSize = 12.sp,
                color = TextSecondaryLight,
                lineHeight = 17.sp
            )
        }
    }
}

// ==========================================
// EMPTY, LOADING & ERROR STATES
// ==========================================

@Composable
fun NirapodEmptyState(
    title: String,
    subtitle: String,
    icon: ImageVector = Icons.Default.ShoppingBag,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    NirapodCard(
        modifier = modifier.padding(vertical = 16.dp),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(BlueContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                fontFamily = AnekBangla,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimaryLight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontFamily = AnekBangla,
                fontSize = 13.sp,
                color = TextMutedLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(16.dp))
                NirapodPrimaryButton(
                    text = actionText,
                    onClick = onActionClick,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}

@Composable
fun NirapodErrorState(
    title: String,
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    retryText: String = "আবার চেষ্টা করুন"
) {
    NirapodCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = CrimsonAlert,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontFamily = AnekBangla,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                fontFamily = AnekBangla,
                fontSize = 13.sp,
                color = TextSecondaryLight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            NirapodSecondaryButton(
                text = retryText,
                onClick = onRetry,
                leadingIcon = Icons.Default.Refresh,
                modifier = Modifier.width(180.dp)
            )
        }
    }
}
