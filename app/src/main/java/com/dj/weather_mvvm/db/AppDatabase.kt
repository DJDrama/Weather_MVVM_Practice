package com.dj.weather_mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.model.WeatherInfo
import com.dj.weather_mvvm.util.DATABASE_NAME


@Database(entities = [WeatherInfo::class, Daily::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherInfoDao(): WeatherInfoDao
    abstract fun dailyDao(): DailyDao
    

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}
