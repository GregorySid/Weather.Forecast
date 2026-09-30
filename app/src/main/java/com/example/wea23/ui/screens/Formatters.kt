package com.example.wea23.ui.screens

import java.text.SimpleDateFormat

/** Форматирование дат — перенос логики из Frag_one/Frag_two/адаптеров без изменений */

fun formatLocalTime(localtime: String): String? {
    val parsed = SimpleDateFormat("yyyy-MM-dd hh:mm").parse(localtime)
    return parsed?.let { SimpleDateFormat("d MMMM '|' H:mm").format(it) }
}

fun formatDayDate(date: String): String {
    val parsed = SimpleDateFormat("yyyy-MM-dd").parse(date)
    return SimpleDateFormat("d MMMM").format(parsed)
}

fun formatHour(time: String): String? {
    val parsed = SimpleDateFormat("yyyy-MM-dd hh:mm").parse(time)
    return parsed?.let { SimpleDateFormat("H a").format(it) }
}
