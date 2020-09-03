package com.dj.weather_mvvm.ui.main.more

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dj.weather_mvvm.model.GoogleMapSettings
import com.dj.weather_mvvm.model.LocationLatLng
import com.dj.weather_mvvm.repository.WeatherRepository
import com.google.android.libraries.maps.GoogleMap
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class GoogleMapViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _locationLatLng = MutableLiveData<LocationLatLng>()
    val locationLatLng
        get() = _locationLatLng

    private val _mapReadyAndLocationMediatorLiveData = MediatorLiveData<GoogleMapSettings>()
    val mapReadyAndLocationMediatorLiveData: LiveData<GoogleMapSettings>
        get() = _mapReadyAndLocationMediatorLiveData

    private val _isLocationLatLngInserted = MutableLiveData<Boolean>()
    val isLocationLatLngInserted
        get() = _isLocationLatLngInserted

    private val _isMapReady = MutableLiveData<Boolean>()
    private val _googleMap = MutableLiveData<GoogleMap>()

    init {
        getLocationFromDatabase()
        _isMapReady.value = false
        _mapReadyAndLocationMediatorLiveData.addSource(_isMapReady) {
            _mapReadyAndLocationMediatorLiveData.value =
                GoogleMapSettings(it, _locationLatLng.value, _googleMap.value)
        }
        _mapReadyAndLocationMediatorLiveData.addSource(_locationLatLng) {
            _mapReadyAndLocationMediatorLiveData.value =
                GoogleMapSettings(_isMapReady.value!!, it, _googleMap.value)
        }
        _mapReadyAndLocationMediatorLiveData.addSource(_googleMap) {
            _mapReadyAndLocationMediatorLiveData.value =
                GoogleMapSettings(_isMapReady.value!!, _locationLatLng.value, it)
        }
    }

    private fun getLocationFromDatabase() {
        viewModelScope.launch(IO) {
            val locationLatLng = weatherRepository.fetchMyLocation()
            locationLatLng?.let {
                _locationLatLng.postValue(it)
            }
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
        location?.let {
            viewModelScope.launch(IO) {
                val insertedValue = weatherRepository.insertLocationData(
                    locationLatLng = LocationLatLng(
                        lat = it.latitude.toString(),
                        lon = it.longitude.toString()
                    )
                )
                //if inserted remove prefetched weather data from cache in order to reload
                weatherRepository.deleteAllWeatherData()

                if (insertedValue > 0) {
                    _isLocationLatLngInserted.postValue(true)
                } else {
                    _isLocationLatLngInserted.postValue(false)
                }
            }
        }
    }

}