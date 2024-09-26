package com.example.playlistmaker.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R


val ysDisplayFontFamily = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Light),
    Font(R.font.ys_display_medium, FontWeight.Medium)
)

val Typography = Typography(
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ysDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    titleSmall = TextStyle(
        fontFamily = ysDisplayFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    bodySmall = TextStyle(
        fontFamily = ysDisplayFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    displayMedium = TextStyle(
        fontFamily = ysDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    bodyMedium = TextStyle(
        fontFamily = ysDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    )
)