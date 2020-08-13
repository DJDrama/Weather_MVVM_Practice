package com.dj.weather_mvvm.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WeatherListViewModel internal constructor(
    val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = _weatherInfo

    fun fetchWeatherInfo(location: Location) {
        viewModelScope.launch(IO) {
            val weatherInfo = weatherRepository.getWeatherData(
                location.latitude,
                location.longitude
            )
            weatherRepository.insertWeatherData(weatherInfo)
            _weatherInfo.postValue(weatherInfo)
        }
    }
}