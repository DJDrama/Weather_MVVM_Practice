package com.dj.weather_mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.model.LocationLatLng
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.util.DATABASE_NAME


@Database(entities = [WeatherInfo::class, LocationLatLng::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherInfoDao(): WeatherInfoDao
    abstract fun locationLatLngDao(): LocationLatLngDao
}
