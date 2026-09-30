package com.example.wea23.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wea23.data_model.ForecastForHour
import com.example.wea23.data_model.ForecastOneDay
import com.example.wea23.ui.screens.formatDayDate
import com.example.wea23.ui.screens.formatHour
import com.example.wea23.ui.theme.CardPurple
import com.example.wea23.ui.theme.TextWhite

/** Замена WeaAdapter + view_holder.xml */
@Composable
fun HourlyForecastRow(hours: List<ForecastForHour>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(hours) { hour ->
            HourCard(hour)
        }
    }
}

@Composable
private fun HourCard(hour: ForecastForHour) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(90.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardPurple)
            .padding(8.dp)
    ) {
        Text(formatHour(hour.time).orEmpty(), style = TextStyle(fontSize = 14.sp), color = TextWhite)
        AsyncImage(
            model = "https:" + hour.condition.icon,
            contentDescription = null,
            modifier = Modifier.size(45.dp)
        )
        Text(String.format("%.0f℃", hour.temp_c), style = TextStyle(fontSize = 18.sp), color = TextWhite)
    }
}

/** Замена FutureAdapter + view_holder_day.xml */
@Composable
fun DailyForecastList(days: List<ForecastOneDay>, onDayClick: (ForecastOneDay) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        days.forEach { day ->
            DayRow(day, onDayClick)
        }
    }
}

@Composable
private fun DayRow(day: ForecastOneDay, onDayClick: (ForecastOneDay) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onDayClick(day) }
            .padding(horizontal = 2.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(formatDayDate(day.date), style = TextStyle(fontSize = 14.sp), color = TextWhite)
            Text(day.day.condition.text, style = TextStyle(fontSize = 14.sp), color = TextWhite)
        }
        AsyncImage(
            model = "https:" + day.day.condition.icon,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 32.dp)
                .size(45.dp)
        )
        Text(
            String.format("%.0f℃", day.day.maxtemp_c),
            style = TextStyle(fontSize = 20.sp),
            color = TextWhite,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            String.format("%.0f℃", day.day.mintemp_c),
            style = TextStyle(fontSize = 20.sp),
            color = TextWhite,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp)
        )
    }
}
