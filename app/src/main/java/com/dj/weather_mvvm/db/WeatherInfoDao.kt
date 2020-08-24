package com.dj.weather_mvvm.db

import androidx.room.*
import com.dj.weather_mvvm.model.WeatherInfo

@Dao
interface WeatherInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherInfo: WeatherInfo)

    @Query("SELECT * FROM weather_info")
    suspend fun getWeatherInfo(): WeatherInfo

    @Query("DELETE FROM weather_info")
    suspend fun deleteAll()
}