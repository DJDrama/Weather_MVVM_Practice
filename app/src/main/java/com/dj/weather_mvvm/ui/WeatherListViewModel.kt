package com.dj.weather_mvvm.ui

import android.location.Location
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherListViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = _weatherInfo

    private val _isNetworkAvailable = MutableLiveData<Boolean>()

    init {
        _isNetworkAvailable.value = false
    }

    fun setNetworkAvailability(value: Boolean) {
        _isNetworkAvailable.value = value
    }

    fun fetchWeatherInfo(location: Location) {
        if (!_isNetworkAvailable.value!!) return
        viewModelScope.launch(IO) {
            val weatherInfo = weatherRepository.getWeatherDataFromApi(
                location.latitude,
                location.longitude
            )
            weatherRepository.insertWeatherData(weatherInfo)
            _weatherInfo.postValue(weatherInfo)
        }
    }

    fun getWeatherDataFromDatabaseIfNotNull() {
        viewModelScope.launch {
            withContext(IO) {
                val weatherInfo = weatherRepository.getWeatherDataFromCache()
                weatherInfo?.let {
                    _weatherInfo.postValue(it)
                }
            }
        }
    }
}