package com.example.wea23.ViewM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wea23.ViewST.Data
import com.example.wea23.ViewST.Error
import com.example.wea23.ViewST.Loading
import com.example.wea23.ViewST.MainVSt
import com.example.wea23.network.WeatherApiCreator
import com.example.wea23.network.MainApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val API_KEY = "9432eca1dec64dbfb1a154703231308"
private const val CITY_NAME = "Moscow"

class MainVM(
    private val api: MainApi = WeatherApiCreator.getApi()) : ViewModel() {
    private val _viewState = MutableStateFlow<MainVSt>(Loading)
    val viewState: Flow<MainVSt> get() = _viewState

    init {
        loadWeather(CITY_NAME)
    }

    fun loadWeather(city: String) {
        _viewState.value = Loading
        viewModelScope.launch {
            try {
                val weather = api.getWeatherData(API_KEY, city,"3", "no", "no")
                _viewState.value = Data(weather)
            } catch (e: Exception) {
                _viewState.value = Error
                Log.e("MainVM", "loading failed", e)
            }
        }
    }
}