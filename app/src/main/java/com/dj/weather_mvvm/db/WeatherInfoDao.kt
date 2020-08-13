package com.dj.weather_mvvm.db

import androidx.room.Dao
import androidx.room.Insert
import com.dj.weather_mvvm.model.WeatherInfo

@Dao
interface WeatherInfoDao {

    @Insert
    suspend fun insert(weatherInfo: WeatherInfo)

}