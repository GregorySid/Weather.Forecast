package com.example.wea23.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.weatherapi.com/v1/"

object WeatherApiCreator {
    private val retrofit: Retrofit = creareRetrofut()

    private fun creareRetrofut(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getApi(): MainApi = retrofit.create(MainApi::class.java)
}