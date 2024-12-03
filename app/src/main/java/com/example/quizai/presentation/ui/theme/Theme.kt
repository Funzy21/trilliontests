package com.example.quizai.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    secondary = PrimaryGreen,
    tertiary = DarkGreen,
    surface = Color(0xFF1B1B1B),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = LightGreen,
    tertiary = DarkGreen,
    surface = SurfaceGreen,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onSurface = Color(0xFF1B1B1B)
)

@Composable
fun EducationAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
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