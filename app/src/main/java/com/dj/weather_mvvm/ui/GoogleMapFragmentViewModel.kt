package com.dj.weather_mvvm.ui

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.libraries.maps.GoogleMap

data class GoogleMapSettings(
    val isMapReady: Boolean ,
    val location: Location?,
    val googleMap: GoogleMap?
)
class GoogleMapFragmentViewModel : ViewModel() {
    private val _location = MutableLiveData<Location>()
//    val locationLiveData: LiveData<Location>
//        get() = _location

    private val _isMapReady = MutableLiveData<Boolean>()
    private val _googleMap = MutableLiveData<GoogleMap>()

    private val _mapReadyAndLocationMediatorLiveData = MediatorLiveData<GoogleMapSettings>()
    val mapReadyAndLocationMediatorLiveData: LiveData<GoogleMapSettings>
        get() = _mapReadyAndLocationMediatorLiveData

    init {
        _isMapReady.value = false
        _mapReadyAndLocationMediatorLiveData.addSource(_isMapReady) {
            _mapReadyAndLocationMediatorLiveData.value = GoogleMapSettings(it, _location.value, _googleMap.value)
        }
        _mapReadyAndLocationMediatorLiveData.addSource(_location) {
            _mapReadyAndLocationMediatorLiveData.value =GoogleMapSettings(_isMapReady.value!!, it, _googleMap.value)
        }
        _mapReadyAndLocationMediatorLiveData.addSource(_googleMap){
            _mapReadyAndLocationMediatorLiveData.value =GoogleMapSettings(_isMapReady.value!!, _location.value, it)

        }
    }


    fun setMapReady(value: Boolean) {
        _isMapReady.value = value
    }

    fun setGoogleMap(map: GoogleMap){
        _googleMap.value = map
    }

    fun setLocation(location: Location) {
        _location.value = location
    }
}