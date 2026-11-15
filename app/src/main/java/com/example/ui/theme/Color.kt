package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==========================================
// NIRAPOD TRANQUIL SKY & ICE BLUE ESCROW DESIGN TOKENS
// Inspired by the soft light-blue reference visual identity
// ==========================================

// Direct Reference Light Blue Match (#BAE0FD / #BEE3FC family)
val ReferenceSkyBlue = Color(0xFFBAE0FD)        // Exact soft light blue tone from reference
val CanvasLight = Color(0xFFEBF5FE)             // Soft, pleasant light-blue canvas background
val CanvasBlueGlow = Color(0xFFD3EBFC)          // Gentle ambient sky blue glow for layered depth
val CanvasWarmGlow = CanvasBlueGlow              // Alias for backward compatibility

// Neutral Canvas & Surfaces
val SurfaceLight = Color(0xFFFFFFFF)            // Clean white floating cards
val SurfaceElevatedLight = Color(0xFFF4F9FE)    // Ice blue tinted elevated cards
val SurfaceVariantLight = Color(0xFFDFEFFC)     // Soft light-blue container for inputs, pills & chips
val SurfaceSkyContainer = Color(0xFFE6F3FD)     // Tender light-blue container
val BorderLight = Color(0xFFC5E2F7)             // Crisp, elegant sky-blue perimeter border
val BorderFocused = Color(0xFF0284C7)           // Vivid focused accent blue
val SkyBorderLight = Color(0xFFBAE0FD)          // Exact reference tone border

// Primary Brand Palette (Elegant, Trustworthy Blues derived from the reference family)
val BluePrimary = Color(0xFF0284C7)             // Deep radiant sky azure - modern, confident, accessible
val BluePrimaryHover = Color(0xFF0369A1)        // Rich azure
val BlueDark = Color(0xFF075985)                // Deep ocean navy - bold headlines, contrast text
val BlueDeep = Color(0xFF0C4A6E)                // Dark slate azure
val BlueLight = Color(0xFF38BDF8)               // Radiant bright sky-blue highlight
val BlueSoft = Color(0xFFBAE0FD)                // Exact reference tone
val BlueContainerLight = Color(0xFFE0F2FE)      // Harmonious light blue surface container
val BlueContainerDark = Color(0xFF082F49)
val BlueBorderLight = Color(0xFFBAE0FD)         // Harmonious light blue border
val BlueSparkle = Color(0xFF0EA5E9)

// Reusable Gradient Brushes
val PrimaryBlueGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF38BDF8))
)
val BlueGlowGradient = Brush.radialGradient(
    colors = listOf(Color(0xFFBAE0FD), Color(0xFFE0F2FE), Color(0x00E0F2FE))
)
val BlueCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF4FAFE), Color(0xFFFFFFFF))
)

// Legacy Aliases mapped to new blue system so any screen smoothly inherits the new blue identity
val OrangePrimary = BluePrimary
val OrangePrimaryHover = BluePrimaryHover
val OrangeDark = BlueDark
val OrangeDeep = BlueDeep
val OrangeLight = BlueLight
val PeachWarm = BlueSoft
val PeachSoft = BlueSoft
val PeachContainerLight = BlueContainerLight
val PeachContainerDark = BlueContainerDark
val PeachBorderLight = BlueBorderLight
val WarmSparkle = BlueSparkle
val PrimaryOrangeGradient = PrimaryBlueGradient
val PeachGlowGradient = BlueGlowGradient
val WarmCardGradient = BlueCardGradient

val EmeraldPrimary = BluePrimary
val EmeraldPrimaryHover = BluePrimaryHover
val EmeraldDark = BlueDark
val EmeraldLight = BlueLight
val EmeraldContainerLight = BlueContainerLight
val EmeraldContainerDark = BlueContainerDark
val EmeraldBorderLight = BlueBorderLight

// Typography Scale (Deep slate navy/blue-gray for pristine contrast and readability)
val TextPrimaryLight = Color(0xFF091E33)        // Deep navy slate (ultra high contrast, crisp reading)
val TextSecondaryLight = Color(0xFF2B4764)      // Mid slate blue
val TextMutedLight = Color(0xFF5A7B9D)          // Gentle muted sky slate
val TextDisabledLight = Color(0xFF9CB8D4)       // Soft ice disabled text

// Dark Theme Variants (Deep Midnight Blue)
val CanvasDark = Color(0xFF08121E)
val SurfaceDark = Color(0xFF0F1E2E)
val SurfaceElevatedDark = Color(0xFF16293D)
val SurfaceVariantDark = Color(0xFF142436)
val BorderDark = Color(0xFF1D3B56)
val TextPrimaryDark = Color(0xFFF0F7FF)
val TextSecondaryDark = Color(0xFFBFD7ED)
val TextMutedDark = Color(0xFF86A8C7)

// Escrow & Transaction Status Tokens (Refined tones harmonized with Sky Blue)
// 1. Vault Held & Protected (Dominant Sky Azure)
val VaultBlue = Color(0xFF0284C7)
val VaultBlueContainer = Color(0xFFE0F2FE)
val VaultBlueBorder = Color(0xFFBAE0FD)
val VaultBlueDark = Color(0xFF0369A1)

// 2. Success, Verified & Released (Emerald / Teal harmonized with Blue)
val SuccessGreen = Color(0xFF059669)
val SuccessGreenContainer = Color(0xFFE6F7F2)
val SuccessGreenLight = Color(0xFFE6F7F2)
val SuccessGreenBorder = Color(0xFFA7F3D0)

// 3. Warning & Courier In-Transit (Warm Amber)
val AmberTrust = Color(0xFFD97706)
val AmberTrustLight = Color(0xFFF59E0B)
val AmberTrustContainer = Color(0xFFFEF3C7)
val AmberTrustBorder = Color(0xFFFDE68A)

// 4. Dispute & Caution (Vibrant Crimson)
val CrimsonAlert = Color(0xFFE11D48)
val CrimsonAlertContainer = Color(0xFFFFEDF2)
val CrimsonAlertBorder = Color(0xFFFECDD3)

// Supporting Brand Accents (Bangladeshi MFS Brands)
val BkashPink = Color(0xFFE2136E)
val NagadOrange = Color(0xFFF7941D)
val RocketViolet = Color(0xFF8C3494)

// Backward compatibility aliases
val GreenSuccess = SuccessGreen
val GreenSuccessContainer = SuccessGreenContainer
val AmberContainer = AmberTrustContainer
val BlueEscrow = VaultBlue
val BlueEscrowContainer = VaultBlueContainer
val CrimsonContainer = CrimsonAlertContainer
val SlateBorderDark = BorderDark
val SlateLightBackground = CanvasLight
val SlateLightSurface = SurfaceLight
val SlateCardDark = SurfaceDark
val SlateDarkNavy = CanvasDark
val SlateSurfaceDark = SurfaceDark

