package com.dj.weather_mvvm.db

import androidx.room.Dao
import androidx.room.Insert
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.model.WeatherInfo

@Dao
interface DailyDao {

    @Insert
    suspend fun insert(daily: Daily)

}