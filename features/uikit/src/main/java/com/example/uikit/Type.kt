package com.example.uikit

import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = androidx.compose.material3.Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = androidx.compose.ui.text.font.FontFamily.Companion.Default,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
