package com.example.uikit

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme

private val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = androidx.compose.ui.graphics.Color.Companion.Black,
    onBackground = androidx.compose.ui.graphics.Color.Companion.White
)
private val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = androidx.compose.ui.graphics.Color.Companion.White,
    onBackground = androidx.compose.ui.graphics.Color.Companion.Black,
)

@androidx.compose.runtime.Composable
fun InfoFlowTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @androidx.compose.runtime.Composable () -> Unit
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = androidx.compose.ui.platform.LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
