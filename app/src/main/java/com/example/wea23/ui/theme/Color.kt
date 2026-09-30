package com.example.wea23.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** colorPrimaryDark из themes.xml и начало градиента background.xml */
val PurpleTop = Color(0xFF59469D)

/** Конец градиента background.xml и цвет "back" из colors.xml */
val DeepPurple = Color(0xFF643D67)

/** background2.xml — фон карточек */
val CardPurple = Color(0xFF331866)

/** Золотой акцент из layouts (заголовки секций, футер) */
val GoldAccent = Color(0xFFDCA900)

val TextWhite = Color(0xFFFFFFFF)

/** background.xml — градиент под -45°, заменяет drawable для всего экрана */
val ScreenBackground = Brush.linearGradient(listOf(PurpleTop, DeepPurple))
