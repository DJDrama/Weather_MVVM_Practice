package com.dj.weather_mvvm.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "weather_info")
data class WeatherInfo(
    val lat: String,
    val lon: String,
    @ColumnInfo(name="timezone")
    @Json(name = "timezone")
    val timeZone: String,
    @Json(name = "timezone_offset")
    val timezoneOffset: String,
    @Json(name = "daily")
    val dailyList: List<Daily>
)



