package com.dj.weather_mvvm.repository

import com.dj.weather_mvvm.api.Api
import com.dj.weather_mvvm.db.WeatherInfoDao
import com.dj.weather_mvvm.model.WeatherInfo

class WeatherRepository private constructor(
    private val weatherInfoDao: WeatherInfoDao
){
    companion object {
        //For Singleton Instantiation
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance(weatherInfoDao: WeatherInfoDao) =
            instance ?: synchronized(this) {
                instance ?: WeatherRepository(weatherInfoDao).also { instance = it }
            }
    }


    // SEOUL, KOREA Latitude, Longitude
    suspend fun getWeatherData(lat: Double, long: Double): WeatherInfo {
        return Api.retrofitService.getDailyWeather(
            latitude = lat.toString(),
            longitude = long.toString()
        )
    }

    suspend fun insertWeatherData(weatherData: WeatherInfo){
       weatherInfoDao.insert(weatherData)
    }


}