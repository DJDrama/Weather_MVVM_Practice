package com.dj.weather_mvvm.ui.main.today

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodayViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _dailyItem: MutableLiveData<Daily> = MutableLiveData()
    val dailyItem: LiveData<Daily>
        get() = _dailyItem

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    init {

    }

     fun getTodayDailyItemFromDatabaseIfNotNull() {
        viewModelScope.launch(IO) {
            val weatherInfo = weatherRepository.getWeatherDataFromCache()
            weatherInfo?.let {
                // Index 0 in order to fetch "Today"'s daily item
                val dailyItem = it.dailyList[0]
                _dailyItem.postValue(dailyItem)
            } ?: withContext(Main) {
                fetchWeatherInfo()
            }
        }
    }

    fun fetchWeatherInfo() {
        _isRefreshing.value = true
        viewModelScope.launch(IO) {
            //1. First fetch latitude and longitude from database
            val locationLatLng = weatherRepository.getLocationLatLng()

            //2. Fetch WeatherInfo according to latitude and longitude
            val weatherInfo = weatherRepository.getWeatherDataFromApi(
                locationLatLng.lat.toDouble(),
                locationLatLng.lon.toDouble()
            )
            weatherRepository.deleteAllWeatherData()
            weatherRepository.insertWeatherData(weatherInfo)

            // Zero index --> Today
            _dailyItem.postValue(weatherInfo.dailyList[0])
            _isRefreshing.postValue(false)
        }
    }
}