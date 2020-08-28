package com.dj.weather_mvvm.model

import android.os.Parcelable
import androidx.room.*
import com.dj.weather_mvvm.db.Converters
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
@Entity(tableName = "location_lat_lng")
data class LocationLatLng(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val lat: String,
    val lon: String,
)



