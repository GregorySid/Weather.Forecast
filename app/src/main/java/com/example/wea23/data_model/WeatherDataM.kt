package com.example.wea23.data_model

import com.google.gson.annotations.SerializedName

data class WeatherDataM(
    val location: LocModel,
    val current: CurrModel,
    val forecast: ForecastDataM
)

data class LocModel(
    val name: String,
    val localtime: String
)

data class CurrModel(
    @SerializedName("last_updated")
    val data: String,
    @SerializedName("wind_kph")
    val wind: Float,
    val humidity: Int,
    @SerializedName("precip_mm")
    val rain: Float,
    val temp_c: Float,
    val condition: CondiModel
)

data class CondiModel(
    val text: String,
    val icon: String
)