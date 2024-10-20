@file:Suppress("MagicNumber")

package com.example.uikit

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val GradientCard = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3D4052),
        Color(0xFF282a36),
        Color(0xFF09090C),
    )
)
val ColorNotActive = Color.Transparent.copy(alpha = 0.04f)
val MainBlueColor = Color(0xFF0288D1)
