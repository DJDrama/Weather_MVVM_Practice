package com.dj.weather_mvvm.ui

import android.location.Location
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.model.GoogleMapSettings
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.repository.WeatherRepository
import com.google.android.libraries.maps.GoogleMap
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherSharedViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    private val _mapReadyAndLocationMediatorLiveData = MediatorLiveData<GoogleMapSettings>()
    val mapReadyAndLocationMediatorLiveData: LiveData<GoogleMapSettings>
        get() = _mapReadyAndLocationMediatorLiveData


    private val _isMapReady = MutableLiveData<Boolean>()
    private val _googleMap = MutableLiveData<GoogleMap>()


    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo>
        get() = _weatherInfo

    private val _isNetworkAvailable = MutableLiveData<Boolean>()

    private val _setMyLocationClicked = MutableLiveData<Boolean>()
    val setMyLocationClickedLiveData: LiveData<Boolean>
        get() = _setMyLocationClicked

    private val _location: MutableLiveData<Location> = MutableLiveData()
    val location: MutableLiveData<Location>
        get() = _location

    private val _dailyItem: MutableLiveData<Daily> = MutableLiveData()
    val dailyItem: LiveData<Daily>
        get() = _dailyItem

    private val _isDailyItemClicked: MutableLiveData<Boolean> = MutableLiveData()
    val isDailyItemClicked: LiveData<Boolean>
        get() = _isDailyItemClicked

    init {
        _isNetworkAvailable.value = false
    }

    fun setDailyItemClicked(value: Boolean) {
        _isDailyItemClicked.value = value
    }

    fun setDailyItem(daily: Daily) {
        _dailyItem.value = daily
    }

    fun setMyLocation(location: Location) {
        _location.value = location
    }

    fun setNetworkAvailability(value: Boolean) {
        _isNetworkAvailable.value = value
    }

    fun setMyLocationClicked(value: Boolean) {
        _setMyLocationClicked.value = value
    }

    fun fetchWeatherInfo(location: Location) {
        if (!_isNetworkAvailable.value!!) return
        viewModelScope.launch(IO) {
            val weatherInfo = weatherRepository.getWeatherDataFromApi(
                location.latitude,
                location.longitude
            )
            weatherRepository.deleteAllWeatherData()
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
                    val location = Location("")
                    location.latitude = it.lat.toDouble()
                    location.longitude = it.lon.toDouble()
                    _location.postValue(location)
                }
            }
        }
    }


    init {
        _isMapReady.value = false
        _mapReadyAndLocationMediatorLiveData.addSource(_isMapReady) {
            _mapReadyAndLocationMediatorLiveData.value =
                GoogleMapSettings(it, _location.value, _googleMap.value)
        }
        _mapReadyAndLocationMediatorLiveData.addSource(_location) {
            _mapReadyAndLocationMediatorLiveData.value =
                GoogleMapSettings(_isMapReady.value!!, it, _googleMap.value)
        }
        _mapReadyAndLocationMediatorLiveData.addSource(_googleMap) {
            _mapReadyAndLocationMediatorLiveData.value =
                GoogleMapSettings(_isMapReady.value!!, _location.value, it)
        }
    }


    fun setMapReady(value: Boolean) {
        _isMapReady.value = value
    }

    fun setGoogleMap(map: GoogleMap) {
        _googleMap.value = map
    }

    fun getGoogleMap(): GoogleMap? {
        return _googleMap.value
    }

    fun setLocation(location: Location?) {
        _location.value = location
    }
}

