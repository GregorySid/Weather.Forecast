package com.example.wea23.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.wea23.R
import com.example.wea23.ViewM.MainVM
import com.example.wea23.ViewST.Data
import com.example.wea23.ViewST.Error
import com.example.wea23.ViewST.Loading
import com.example.wea23.data_model.ForecastOneDay
import com.example.wea23.data_model.WeatherDataM
import com.example.wea23.ui.screens.formatLocalTime
import com.example.wea23.ui.theme.CardPurple
import com.example.wea23.ui.theme.GoldAccent
import com.example.wea23.ui.theme.ScreenBackground
import com.example.wea23.ui.theme.TextWhite
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: MainVM,
    mapPosition: State<LatLng?>,
    onDayClick: (ForecastOneDay) -> Unit,
    onCitySearch: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle(initialValue = Loading)
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is Error) {
            Toast.makeText(context, R.string.error_text, Toast.LENGTH_SHORT).show()
        }
    }

    val pullState = rememberPullToRefreshState()
    if (pullState.isRefreshing) {
        LaunchedEffect(Unit) { onRefresh() }
    }
    // Как в исходной версии: индикатор убирается сразу после запуска обновления
    LaunchedEffect(state, pullState.isRefreshing) {
        if (pullState.isRefreshing && state !is Loading) pullState.endRefresh()
    }

    // На Error/Loading оставляем последние успешные данные на экране (как в исходной версии)
    val lastWeather = remember { mutableStateOf<WeatherDataM?>(null) }
    val freshWeather = (state as? Data)?.weatherDataM
    if (freshWeather != null) lastWeather.value = freshWeather

    Box(
        Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .nestedScroll(pullState.nestedScrollConnection)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CitySearchField(onCitySearch)
            WeatherMap(
                mapPosition = mapPosition,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
                    .width(380.dp)
                    .height(120.dp)
            )
            lastWeather.value?.let { weather ->
                CurrentWeatherBlock(weather)
                MetricsRow(
                    rain = weather.current.rain,
                    wind = weather.current.wind,
                    humidity = weather.current.humidity
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    Text(
                        "Today",
                        style = MaterialTheme.typography.labelLarge,
                        color = GoldAccent,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "Next 24 hours",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextWhite
                    )
                }
                HourlyForecastRow(weather.forecast.forecastday.firstOrNull()?.hour.orEmpty())
                Text(
                    "Next 3 days",
                    style = MaterialTheme.typography.labelLarge,
                    color = GoldAccent,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                DailyForecastList(weather.forecast.forecastday, onDayClick)
            }
            Text(
                "Product by Gregory",
                style = MaterialTheme.typography.labelMedium,
                color = GoldAccent,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
        PullToRefreshContainer(pullState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun CitySearchField(onSearch: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    TextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search city", color = TextWhite.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextWhite) },
        singleLine = true,
        textStyle = TextStyle(color = TextWhite),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (query.isNotBlank()) onSearch(query.trim())
        }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardPurple,
            unfocusedContainerColor = CardPurple,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            cursorColor = GoldAccent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun CurrentWeatherBlock(weather: WeatherDataM) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            weather.location.name,
            style = MaterialTheme.typography.headlineMedium,
            color = TextWhite,
            modifier = Modifier.padding(top = 48.dp)
        )
        AsyncImage(
            model = "https:" + weather.current.condition.icon,
            contentDescription = weather.current.condition.text,
            modifier = Modifier
                .padding(top = 16.dp)
                .size(120.dp)
        )
        Text(
            weather.current.condition.text,
            style = MaterialTheme.typography.titleLarge,
            color = TextWhite,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            formatLocalTime(weather.location.localtime).orEmpty(),
            style = MaterialTheme.typography.titleMedium,
            color = TextWhite,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            stringResource(R.string.temp, weather.current.temp_c),
            style = MaterialTheme.typography.displayLarge,
            color = TextWhite
        )
        val today = weather.forecast.forecastday.firstOrNull()?.day
        Text(
            "H: ${today?.maxtemp_c}  L: ${today?.mintemp_c}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextWhite
        )
    }
}

@Composable
private fun MetricsRow(rain: Float, wind: Float, humidity: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardPurple)
            .padding(16.dp)
    ) {
        MetricItem(Modifier.weight(1f), R.drawable.protection, stringResource(R.string.rain_p, rain), "Rain")
        MetricItem(Modifier.weight(1f), R.drawable.wind, stringResource(R.string.wind_p, wind), "Wind speed")
        MetricItem(Modifier.weight(1f), R.drawable.humidity, "$humidity%", "Humidity")
    }
}

@Composable
private fun MetricItem(modifier: Modifier, iconRes: Int, value: String, label: String) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
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
