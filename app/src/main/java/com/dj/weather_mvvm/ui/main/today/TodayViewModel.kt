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
import kotlinx.coroutines.launch

class TodayViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _dailyItem: MutableLiveData<Daily> = MutableLiveData()
    val dailyItem: LiveData<Daily>
        get() = _dailyItem

    init {
        getTodayDailyItemFromDatabaseIfNotNull()
    }

    private fun getTodayDailyItemFromDatabaseIfNotNull() {
        viewModelScope.launch(IO) {
            val weatherInfo = weatherRepository.getWeatherDataFromCache()
            weatherInfo?.let {
                // Index 0 in order to fetch "Today"'s daily item
                val dailyItem = it.dailyList[0]
                _dailyItem.postValue(dailyItem)
            } ?: fetchWeatherInfo()
        }
    }

    private fun fetchWeatherInfo() {
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
        }
    }
}