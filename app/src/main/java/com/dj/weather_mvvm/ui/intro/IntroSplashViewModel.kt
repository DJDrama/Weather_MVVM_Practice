package com.dj.weather_mvvm.ui.intro

import android.location.Location
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.model.LocationLatLng
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class IntroSplashViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    private val _location: MutableLiveData<Location> = MutableLiveData()
    val location: MutableLiveData<Location>
        get() = _location

    private val _isNetworkAvailable = MutableLiveData<Boolean>()


    init {
        _isNetworkAvailable.value = false
        _location.value = null
    }
    fun setNetworkAvailability(value: Boolean) {
        _isNetworkAvailable.value = value
    }

    fun setMyLocation(location: Location) {
        viewModelScope.launch(IO) {
            val longValue = weatherRepository.insertLocationData(
                LocationLatLng(
                    lat = location.latitude.toString(),
                    lon = location.longitude.toString()
                )
            )
            if (longValue > 0) {
                _location.postValue(location)
            }
        }
    }
}