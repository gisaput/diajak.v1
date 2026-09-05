package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Outfit represents the modern, geometric "Instagram Sans" typeface
val fontOutfit = GoogleFont("Outfit")

val HeadlineFontFamily = FontFamily(
    Font(googleFont = fontOutfit, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = fontOutfit, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = fontOutfit, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = fontOutfit, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = fontOutfit, fontProvider = provider, weight = FontWeight.ExtraBold)
)

// Inter represents the ultra-clean system font used in Instagram's feed and interface
val fontInter = GoogleFont("Inter")

val AppFontFamily = FontFamily(
    Font(googleFont = fontInter, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = fontInter, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = fontInter, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = fontInter, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = fontInter, fontProvider = provider, weight = FontWeight.ExtraBold)
)

// Grand Hotel represents the classic, elegant script typography of the Instagram logo and stories
val fontGrandHotel = GoogleFont("Grand Hotel")

val ScriptFontFamily = FontFamily(
    Font(googleFont = fontGrandHotel, fontProvider = provider, weight = FontWeight.Normal)
)

val Typography: Typography
    get() = Typography(
        displayLarge = AppTypography.Display,
        displayMedium = AppTypography.Display.copy(fontSize = 22.sp, lineHeight = 28.sp),
        displaySmall = AppTypography.Headline,
        headlineLarge = AppTypography.Headline,
        headlineMedium = AppTypography.TitleBold,
        headlineSmall = AppTypography.TitleMedium,
        titleLarge = AppTypography.TitleBold,
        titleMedium = AppTypography.TitleMedium,
        titleSmall = AppTypography.Title,
        bodyLarge = AppTypography.BodyLarge,
        bodyMedium = AppTypography.Body,
        bodySmall = AppTypography.Caption,
        labelLarge = AppTypography.BodyBold,
        labelMedium = AppTypography.LabelBold,
        labelSmall = AppTypography.Label
    )
