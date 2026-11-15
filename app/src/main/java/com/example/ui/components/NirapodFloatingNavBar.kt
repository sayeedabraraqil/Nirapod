package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Screen
import com.example.ui.localization.Language
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextSecondaryLight
import kotlin.math.cos
import kotlin.math.roundToInt

data class NirapodNavItem(
    val route: String,
    val icon: ImageVector,
    val labelBn: String,
    val labelEn: String,
    val testTag: String
)

val defaultNirapodNavItems = listOf(
    NirapodNavItem(
        route = Screen.Explore.route,
        icon = Icons.Default.Home,
        labelBn = "হোম",
        labelEn = "Home",
        testTag = "nav_tab_explore"
    ),
    NirapodNavItem(
        route = Screen.Transactions.route,
        icon = Icons.Default.ReceiptLong,
        labelBn = "লেনদেন",
        labelEn = "Orders",
        testTag = "nav_tab_transactions"
    ),
    NirapodNavItem(
        route = Screen.SellerHub.route,
        icon = Icons.Default.Storefront,
        labelBn = "সেলার",
        labelEn = "Seller",
        testTag = "nav_tab_seller_hub"
    ),
    NirapodNavItem(
        route = Screen.Wallet.route,
        icon = Icons.Default.AccountBalanceWallet,
        labelBn = "ওয়ালেট",
        labelEn = "Wallet",
        testTag = "nav_tab_wallet"
    ),
    NirapodNavItem(
        route = Screen.Admin.route,
        icon = Icons.Default.AdminPanelSettings,
        labelBn = "সুরক্ষা",
        labelEn = "Security",
        testTag = "nav_tab_admin_ops"
    )
)

/**
 * Premium Raised Floating Bottom Navigation Bar.
 *
 * Implements a floating pill structure with an organic curved crest that smoothly glides
 * to the active tab's position, accompanied by a prominent elevated circular badge
 * that partially overlaps and rises above the top edge of the navigation container.
 */
@Composable
fun NirapodFloatingNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    lang: Language,
    modifier: Modifier = Modifier,
    items: List<NirapodNavItem> = defaultNirapodNavItems
) {
    val selectedIndex = remember(currentRoute, items) {
        val idx = items.indexOfFirst { it.route == currentRoute }
        if (idx >= 0) idx else 0
    }

    val density = LocalDensity.current

    // Physical dimensions
    val totalHeightDp = 82.dp
    val baseTopDp = 18.dp
    val crestHeightDp = 16.dp
    val crestWidthDp = 68.dp
    val cornerRadiusDp = 26.dp
    val badgeSizeDp = 50.dp

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeightDp)
            .testTag("floating_bottom_nav_bar")
    ) {
        val totalWidthPx = with(density) { maxWidth.toPx() }
        val totalHeightPx = with(density) { totalHeightDp.toPx() }
        val baseTopPx = with(density) { baseTopDp.toPx() }
        val crestHeightPx = with(density) { crestHeightDp.toPx() }
        val crestWidthPx = with(density) { crestWidthDp.toPx() }
        val cornerRadiusPx = with(density) { cornerRadiusDp.toPx() }
        val badgeSizePx = with(density) { badgeSizeDp.toPx() }

        val itemCount = items.size
        val tabWidthPx = if (itemCount > 0) totalWidthPx / itemCount else 0f

        val targetCenterX = if (itemCount > 0) {
            (selectedIndex + 0.5f) * tabWidthPx
        } else {
            0f
        }

        // Smooth physical gliding animation for the crest & elevated badge
        val animatedCenterX by animateFloatAsState(
            targetValue = targetCenterX,
            animationSpec = spring(
                dampingRatio = 0.82f,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "animated_nav_crest_x"
        )

        // 1. Organic Curved Pill Background Canvas with Ambient Shadow and Crisp Border
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = buildCurvedNavBarPath(
                width = totalWidthPx,
                height = totalHeightPx,
                crestCenterX = animatedCenterX,
                crestWidth = crestWidthPx,
                crestHeight = crestHeightPx,
                baseTop = baseTopPx,
                cornerRadius = cornerRadiusPx
            )

            // Multi-layered soft ambient drop shadow with light-blue tint
            drawIntoCanvas { canvas ->
                val shadowPaint = Paint().apply {
                    color = Color(0x1802385C)
                    asFrameworkPaint().apply {
                        setShadowLayer(
                            20f,
                            0f,
                            6f,
                            android.graphics.Color.argb(28, 2, 56, 92)
                        )
                    }
                }
                canvas.drawPath(path, shadowPaint)
            }

            // Clean white surface
            drawPath(
                path = path,
                color = Color.White
            )

            // Subtle light-blue perimeter border for soft depth
            drawPath(
                path = path,
                color = BlueBorderLight,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx())
            )
        }

        // 2. Sliding Elevated Active Circular Badge (Rises above the navigation bar in vibrant sky azure)
        val badgeOffsetX = (animatedCenterX - (badgeSizePx / 2f)).coerceAtLeast(0f)
        val badgeOffsetY = with(density) { 2.dp.toPx() }

        Box(
            modifier = Modifier
                .offset { IntOffset(badgeOffsetX.roundToInt(), badgeOffsetY.roundToInt()) }
                .size(badgeSizeDp)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    spotColor = BluePrimary.copy(alpha = 0.5f),
                    ambientColor = Color(0x25034670)
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(2.5.dp, Color.White),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0284C7),
                                    Color(0xFF0EA5E9),
                                    Color(0xFF38BDF8)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(
                        targetState = items.getOrNull(selectedIndex)?.icon ?: Icons.Default.Home,
                        animationSpec = tween(220),
                        label = "active_badge_icon"
                    ) { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = items.getOrNull(selectedIndex)?.let {
                                if (lang == Language.BANGLA) it.labelBn else it.labelEn
                            },
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // 3. Tab Touch Columns (Generous click targets, inactive icons & labels)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = baseTopDp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val labelText = if (lang == Language.BANGLA) item.labelBn else item.labelEn

                // Animate inactive icon fade when active badge arrives over it
                val inactiveAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 0f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "inactive_alpha_$index"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (currentRoute != item.route) {
                                onNavigate(item.route)
                            }
                        }
                        .testTag(item.testTag),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 6.dp)
                    ) {
                        // Inactive icon slot
                        Box(
                            modifier = Modifier.size(22.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (inactiveAlpha > 0.05f) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = labelText,
                                    tint = TextMutedLight.copy(alpha = inactiveAlpha),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        // Tab Label with Anek Bangla typography
                        Text(
                            text = labelText,
                            fontFamily = AnekBangla,
                            fontSize = if (isSelected) 11.5.sp else 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) BlueDark else TextMutedLight,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * Builds a continuous geometric path for the navigation pill with a smooth,
 * organic cosine-curved crest centered at [crestCenterX].
 */
private fun buildCurvedNavBarPath(
    width: Float,
    height: Float,
    crestCenterX: Float,
    crestWidth: Float,
    crestHeight: Float,
    baseTop: Float,
    cornerRadius: Float
): Path {
    val path = Path()
    val clampedCorner = cornerRadius.coerceAtMost(width / 4f)

    // 1. Bottom-Left Corner
    path.moveTo(0f, height - clampedCorner)
    path.arcTo(
        rect = Rect(0f, height - 2 * clampedCorner, 2 * clampedCorner, height),
        startAngleDegrees = 180f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    // 2. Bottom Line
    path.lineTo(width - clampedCorner, height)

    // 3. Bottom-Right Corner
    path.arcTo(
        rect = Rect(width - 2 * clampedCorner, height - 2 * clampedCorner, width, height),
        startAngleDegrees = 90f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    // 4. Right Vertical Edge
    path.lineTo(width, baseTop + clampedCorner)

    // 5. Top-Right Corner
    path.arcTo(
        rect = Rect(width - 2 * clampedCorner, baseTop, width, baseTop + 2 * clampedCorner),
        startAngleDegrees = 0f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    // 6. Top Edge with Cosine Crest Curve (right-to-left traversal)
    val crestRight = (crestCenterX + crestWidth / 2f).coerceAtMost(width - clampedCorner)
    val crestLeft = (crestCenterX - crestWidth / 2f).coerceAtLeast(clampedCorner)

    path.lineTo(crestRight, baseTop)

    // Smooth organic crest interpolation using cosine curve
    val samples = 24
    for (i in 1..samples) {
        val t = i.toFloat() / samples
        val x = crestRight - t * (crestRight - crestLeft)
        val normDist = ((x - crestCenterX) / (crestWidth / 2f)).coerceIn(-1f, 1f)
        val hump = 0.5f * (1f + cos(Math.PI * normDist).toFloat())
        val y = baseTop - (crestHeight * hump)
        path.lineTo(x, y)
    }

    // 7. Top-Left Line & Corner
    path.lineTo(clampedCorner, baseTop)
    path.arcTo(
        rect = Rect(0f, baseTop, 2 * clampedCorner, baseTop + 2 * clampedCorner),
        startAngleDegrees = 270f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    path.close()
    return path
}
