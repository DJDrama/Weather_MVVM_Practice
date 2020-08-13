package com.dj.weather_mvvm.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dj.weather_mvvm.model.WeatherInfo

@Dao
interface WeatherInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherInfo: WeatherInfo)

}