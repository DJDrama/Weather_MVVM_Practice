package com.dj.weather_mvvm.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Daily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp, /** typeconvereter **/
    @Json(name = "feels_like")
    val feelsLike: FeelsLike, /** typeconvereter **/
    val pressure: Int,
    val humidity: Int,
    @Json(name = "dew_point")
    val dewPoint: Double,
    @Json(name = "wind_speed")
    val windSpeed: Double,
    @Json(name = "wind_deg")
    val windDeg: Int,
    val weather: List<Weather>, /** typeconvereter **/
    val clouds: Int,
    val pop: Double,
    val uvi: Double
) : Parcelable

@Parcelize
data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
) : Parcelable

@Parcelize
data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
) : Parcelable

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable