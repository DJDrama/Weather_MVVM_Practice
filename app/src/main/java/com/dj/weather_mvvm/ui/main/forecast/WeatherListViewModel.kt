package com.dj.weather_mvvm.ui.main.forecast

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherListViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _isNetworkAvailable = MutableLiveData<Boolean>()

    private val _isDailyItemClicked: MutableLiveData<Boolean> = MutableLiveData()
    val isDailyItemClicked: LiveData<Boolean>
        get() = _isDailyItemClicked

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = _weatherInfo

    private val _location: MutableLiveData<Location> = MutableLiveData()
    val location: MutableLiveData<Location>
        get() = _location

    init {
        _isNetworkAvailable.value = false
        getTodayDailyItemFromDatabaseIfNotNull()

    }

    fun setNetworkAvailability(value: Boolean) {
        _isNetworkAvailable.value = value
    }

    fun setLocation(location: Location?) {
        _location.value = location
    }

    fun setDailyItemClicked(value: Boolean) {
        _isDailyItemClicked.value = value
    }

    private fun getTodayDailyItemFromDatabaseIfNotNull() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherInfo = weatherRepository.getWeatherDataFromCache()
            weatherInfo?.let {
                _weatherInfo.postValue(it)
            } ?: fetchWeatherInfo()
        }
    }

    fun fetchWeatherInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            //1. First fetch latitude and longitude from database
            val locationLatLng = weatherRepository.getLocationLatLng()

            //2. Fetch WeatherInfo according to latitude and longitude
            val weatherInfo = weatherRepository.getWeatherDataFromApi(
                locationLatLng.lat.toDouble(),
                locationLatLng.lon.toDouble()
            )
            weatherRepository.deleteAllWeatherData()
            weatherRepository.insertWeatherData(weatherInfo)

            _weatherInfo.postValue(weatherInfo)
        }
    }
}