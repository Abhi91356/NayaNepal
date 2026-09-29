package com.example.nayanepal.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object NayaNepalTypography {
    val h1 = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Default
    )
    
    val h2 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Default
    )
    
    val h3 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.Default
    )
    
    val bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.Default
    )
    
    val bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.Default
    )
    
    val bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.Default
    )
}

// Convert to Material3 Typography
val Typography = Typography(
    displayLarge = NayaNepalTypography.h1,
    displayMedium = NayaNepalTypography.h2,
    displaySmall = NayaNepalTypography.h3,
    bodyLarge = NayaNepalTypography.bodyLarge,
    bodyMedium = NayaNepalTypography.bodyMedium,
    bodySmall = NayaNepalTypography.bodySmall
)
