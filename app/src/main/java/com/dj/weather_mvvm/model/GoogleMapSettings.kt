package com.dj.weather_mvvm.model

import com.google.android.libraries.maps.GoogleMap

data class GoogleMapSettings(
    val isMapReady: Boolean,
    val location: LocationLatLng?,
    val googleMap: GoogleMap?
)