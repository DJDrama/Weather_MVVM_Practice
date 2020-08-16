package com.dj.weather_mvvm.repository

import com.dj.weather_mvvm.api.ApiService
import com.dj.weather_mvvm.db.WeatherInfoDao
import com.dj.weather_mvvm.model.WeatherInfo
import retrofit2.Retrofit

class WeatherRepository constructor(
    private val weatherInfoDao: WeatherInfoDao,
    private val retrofit: ApiService
) {
    /*
     companion object {
         //For Singleton Instantiation
         @Volatile
         private var instance: WeatherRepository? = null
         fun getInstance(weatherInfoDao: WeatherInfoDao) =
             instance ?: synchronized(this) {
                 instance ?: WeatherRepository(weatherInfoDao).also { instance = it }
             }
     }
 */

    // SEOUL, KOREA Latitude, Longitude
    suspend fun getWeatherDataFromApi(lat: Double, long: Double): WeatherInfo {
        return retrofit.getDailyWeather(
            latitude = lat.toString(),
            longitude = long.toString()
        )
    }

    suspend fun insertWeatherData(weatherData: WeatherInfo) {
        weatherInfoDao.insert(weatherData)
    }

    suspend fun getWeatherDataFromCache(): WeatherInfo? {
        return weatherInfoDao.getWeatherInfo()
    }


}