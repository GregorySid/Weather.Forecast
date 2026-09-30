package com.example.wea23.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Размеры перенесены из fragment_one.xml / fragment_two.xml */
val WeaTypography = Typography(
    displayLarge = TextStyle(fontSize = 63.sp, fontWeight = FontWeight.Bold),   // температура
    headlineMedium = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold), // название города
    titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),     // описание погоды
    titleMedium = TextStyle(fontSize = 19.sp),                                  // дата и время
    bodyLarge = TextStyle(fontSize = 18.sp),                                    // температура в часах
    bodyMedium = TextStyle(fontSize = 16.sp),                                   // метрики, H/L
    labelLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),     // заголовки секций
    labelMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),    // футер
    labelSmall = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)      // часы, "Next 24 hours"
)
