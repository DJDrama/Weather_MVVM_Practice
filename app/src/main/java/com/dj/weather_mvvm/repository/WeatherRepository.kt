package com.dj.weather_mvvm.repository

import com.dj.weather_mvvm.api.Api
import com.dj.weather_mvvm.api.ApiService
import com.dj.weather_mvvm.db.DailyDao
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.model.WeatherInfo

class WeatherRepository private constructor(
    private val dailyDao: DailyDao
){
    companion object {
        //For Singleton Instantiation
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance(dailyDao: DailyDao) =
            instance ?: synchronized(this) {
                instance ?: WeatherRepository(dailyDao).also { instance = it }
            }
    }


    // SEOUL, KOREA Latitude, Longitude
    suspend fun getWeatherData(lat: Double, long: Double): WeatherInfo {
        return Api.retrofitService.getDailyWeather(
            latitude = lat.toString(),
            longitude = long.toString()
        )
    }

    suspend fun insertDailyItems(dailyItems: List<Daily>){
        dailyDao.insert(dailyItems)
    }


}