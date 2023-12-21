package com.example.wea23.ViewST

import com.example.wea23.data_model.WeatherDataM

sealed interface MainVSt

data object Loading: MainVSt
data class Data(val weatherDataM: WeatherDataM): MainVSt
data object Error: MainVSt