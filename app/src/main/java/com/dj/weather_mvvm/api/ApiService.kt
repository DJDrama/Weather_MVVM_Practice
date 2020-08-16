package com.dj.weather_mvvm.api

import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.util.OPEN_WEATHER_KEY
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/"
interface ApiService {
    @GET("data/2.5/onecall")
    suspend fun getDailyWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("exclude") exclude: String? = "current,minutely,hourly",
        @Query("appid") appId: String? = OPEN_WEATHER_KEY,
        @Query("units") units: String? = "metric"
    ): WeatherInfo
}