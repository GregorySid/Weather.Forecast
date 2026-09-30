package com.example.wea23.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val WeaColorScheme = darkColorScheme(
    primary = PurpleTop,
    secondary = GoldAccent,
    background = DeepPurple,
    surface = CardPurple,
    onPrimary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun Wea23Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WeaColorScheme,
        typography = WeaTypography,
        content = content
    )
}
