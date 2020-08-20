package com.dj.weather_mvvm.model

import android.location.Location
import com.google.android.libraries.maps.GoogleMap

data class GoogleMapSettings(
    val isMapReady: Boolean,
    val location: Location?,
    val googleMap: GoogleMap?
)