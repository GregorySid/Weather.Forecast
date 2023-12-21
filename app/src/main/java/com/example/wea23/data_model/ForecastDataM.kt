package com.example.wea23.data_model

import com.google.gson.annotations.SerializedName

data class ForecastDataM(
    val forecastday: List<ForecastOneDay>
)

data class ForecastOneDay(
    val date: String,
    val day: DayM,
    val astro: AsroM,
    val hour: List<ForecastForHour>
)

data class DayM(
    val maxtemp_c: Float,
    val mintemp_c: Float,
    @SerializedName("maxwind_kph")
    val wind: Float,
    @SerializedName("avghumidity")
    val humidity: Float,
    @SerializedName("totalprecip_mm")
    val rain: Float,
    val condition: CondiModel
)

data class AsroM(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moon_phase: String
)

/** Погода за час */
data class ForecastForHour(
    val time: String,
    val temp_c: Float,
    val condition: CondiModel
)



