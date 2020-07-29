package com.dj.weather_mvvm.model

import com.squareup.moshi.Json

data class WeatherInfo(
    val lat: String,
    val lon: String,
    @Json(name = "timezone")
    val timeZone: String,
    @Json(name = "timezone_offset")
    val timezoneOffset: String,
    @Json(name = "daily")
    val dailyList: List<Daily>
)

data class Daily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    @Json(name = "feels_like")
    val feelsLike: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    @Json(name = "dew_point")
    val dewPoint: Double,
    @Json(name = "wind_speed")
    val windSpeed: Double,
    @Json(name = "wind_deg")
    val windDeg: Int,
    val weather: List<Weather>,
    val clouds: Int,
    val pop: Double,
    val uvi: Double
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)