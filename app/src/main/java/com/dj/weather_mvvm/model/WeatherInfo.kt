package com.dj.weather_mvvm.model

import android.os.Parcelable
import androidx.room.*
import com.dj.weather_mvvm.db.Converters
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
@TypeConverters(Converters::class)
@Entity(tableName = "weather_info")
data class WeatherInfo(
    val lat: String,
    val lon: String,
    @ColumnInfo(name="timezone")
    @Json(name = "timezone")
    @PrimaryKey
    val timeZone: String,
    @Json(name = "timezone_offset")
    val timezoneOffset: String,
    @Json(name = "daily")
    @ColumnInfo(name="daily_list")
    val dailyList: List<Daily>
)



