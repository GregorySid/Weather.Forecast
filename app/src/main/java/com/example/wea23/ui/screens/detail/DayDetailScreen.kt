package com.example.wea23.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wea23.R
import com.example.wea23.ViewM.MainVM
import com.example.wea23.data_model.AsroM
import com.example.wea23.data_model.ForecastOneDay
import com.example.wea23.ui.screens.formatDayDate
import com.example.wea23.ui.theme.CardPurple
import com.example.wea23.ui.theme.GoldAccent
import com.example.wea23.ui.theme.ScreenBackground
import com.example.wea23.ui.theme.TextWhite

@Composable
fun DayDetailScreen(viewModel: MainVM, onBack: () -> Unit) {
    val day by viewModel.m.observeAsState()
    Box(
        Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        day?.let { selected ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                BackButton(onBack)
                SummaryCard(selected)
                SunCard(selected.astro)
                MoonCard(selected.astro)
                Text(
                    "Product by Gregory",
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldAccent,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(15.dp)
            .size(35.dp)
            .clickable(onClick = onBack)
    ) {
        Image(
            painter = painterResource(android.R.drawable.ic_menu_revert),
            contentDescription = "Back"
        )
    }
}

@Composable
private fun SummaryCard(day: ForecastOneDay) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardPurple)
    ) {
        Row(Modifier.padding(16.dp)) {
            AsyncImage(
                model = "https:" + day.day.condition.icon,
                contentDescription = day.day.condition.text,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(16.dp)
                    .height(120.dp)
            )
            Column(
                Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    formatDayDate(day.date),
                    style = TextStyle(fontSize = 20.sp),
                    color = TextWhite
                )
                Row(Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                    Text(
                        String.format("%.0f℃", day.day.maxtemp_c),
                        style = TextStyle(fontSize = 25.sp),
                        color = TextWhite,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        String.format("%.0f℃", day.day.mintemp_c),
                        style = TextStyle(fontSize = 25.sp),
                        color = TextWhite
                    )
                }
                Text(
                    day.day.condition.text,
                    style = TextStyle(fontSize = 20.sp),
                    color = TextWhite
                )
            }
        }
        HorizontalDivider(
            color = TextWhite.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
        )
        MetricsRow(
            rain = day.day.rain,
            wind = day.day.wind,
            humidityText = "${day.day.wind}%"
        )
    }
}

@Composable
private fun MetricsRow(rain: Float, wind: Float, humidityText: String) {
    Row(Modifier.padding(16.dp)) {
        MetricItem(Modifier.weight(1f), R.drawable.protection, stringResource(R.string.rain_p, rain), "Rain")
        MetricItem(Modifier.weight(1f), R.drawable.wind, stringResource(R.string.wind_p, wind), "Wind speed")
        MetricItem(Modifier.weight(1f), R.drawable.humidity, humidityText, "Humidity")
    }
}

@Composable
private fun MetricItem(modifier: Modifier, iconRes: Int, value: String, label: String) {
    Column(modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
        )
        Text(value, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
        Text(label, style = TextStyle(fontSize = 14.sp), color = TextWhite)
    }
}

@Composable
private fun SunCard(astro: AsroM) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardPurple)
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(start = 16.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.sunrise),
                contentDescription = "Sunrise",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            )
            Text("Восход", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
            Text(astro.sunrise, style = TextStyle(fontSize = 14.sp), color = TextWhite)
        }
        Column(
            Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.sun),
                contentDescription = "Sun",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Text("Sun", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
        }
        Column(
            Modifier
                .weight(1f)
                .padding(end = 16.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.sunset),
                contentDescription = "Sunset",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            )
            Text("Закат", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
            Text(astro.sunset, style = TextStyle(fontSize = 14.sp), color = TextWhite)
        }
    }
}

@Composable
private fun MoonCard(astro: AsroM) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardPurple)
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(start = 16.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.moonrise),
                contentDescription = "Moonrise",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            )
            Text("Восход", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
            Text(astro.moonrise, style = TextStyle(fontSize = 14.sp), color = TextWhite)
        }
        Column(
            Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.moonset),
                contentDescription = "Moon phase",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Text(astro.moon_phase, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
        }
        Column(
            Modifier
                .weight(1f)
                .padding(end = 16.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.moon),
                contentDescription = "Moonset",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            )
            Text("Заход", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold), color = TextWhite)
            Text(astro.moonset, style = TextStyle(fontSize = 14.sp), color = TextWhite)
        }
    }
}
