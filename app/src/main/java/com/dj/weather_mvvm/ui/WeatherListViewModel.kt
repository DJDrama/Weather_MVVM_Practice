package com.dj.weather_mvvm.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WeatherListViewModel : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = _weatherInfo

    fun fetchWeatherInfo() {
        viewModelScope.launch(IO) {
            _weatherInfo.postValue(WeatherRepository.getInstance().getWeatherData())
        }
    }
}