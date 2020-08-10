package com.dj.weather_mvvm.repository

import com.dj.weather_mvvm.api.Api
import com.dj.weather_mvvm.api.ApiService
import com.dj.weather_mvvm.model.WeatherInfo

class WeatherRepository {
    companion object {
        //For Singleton Instantiation
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: WeatherRepository().also { instance = it }
            }
    }


    // SEOUL, KOREA Latitude, Longitude
    suspend fun getWeatherData(lat: Double, long: Double): WeatherInfo {
        return Api.retrofitService.getDailyWeather(
            latitude = lat.toString(),
            longitude = long.toString()
        )
    }


}