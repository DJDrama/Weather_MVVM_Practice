package com.dj.weather_mvvm.repository

import com.dj.weather_mvvm.api.ApiService
import com.dj.weather_mvvm.db.LocationLatLngDao
import com.dj.weather_mvvm.db.WeatherInfoDao
import com.dj.weather_mvvm.model.LocationLatLng
import com.dj.weather_mvvm.model.WeatherInfo

class WeatherRepository constructor(
    private val weatherInfoDao: WeatherInfoDao,
    private val locationLatLngDao: LocationLatLngDao,
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

    /** LocationLatLngDao **/
    suspend fun getLocationLatLng(): LocationLatLng{
        return locationLatLngDao.getLatestLatLng()
    }
    suspend fun insertLocationData(locationLatLng: LocationLatLng) : Long{
        return locationLatLngDao.insert(locationLatLng)
    }

    // SEOUL, KOREA Latitude, Longitude
    suspend fun getWeatherDataFromApi(lat: Double, long: Double): WeatherInfo {
        return retrofit.getDailyWeather(
            latitude = lat.toString(),
            longitude = long.toString()
        )
    }

    suspend fun deleteAllWeatherData(){
        weatherInfoDao.deleteAll()
    }

    suspend fun insertWeatherData(weatherData: WeatherInfo) {
        weatherInfoDao.insert(weatherData)
    }

    suspend fun getWeatherDataFromCache(): WeatherInfo? {
        return weatherInfoDao.getWeatherInfo()
    }


    /** MORE **/
    suspend fun fetchMyLocation(): LocationLatLng?{
        return locationLatLngDao.getLatestLatLng()
    }

}