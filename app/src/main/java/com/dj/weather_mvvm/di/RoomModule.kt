package com.dj.weather_mvvm.di

import android.content.Context
import android.location.Location
import androidx.room.Room
import com.dj.weather_mvvm.db.*
import com.dj.weather_mvvm.util.DATABASE_NAME
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherInfoDao(appDatabase: AppDatabase): WeatherInfoDao {
        return appDatabase.weatherInfoDao()
    }

    @Singleton
    @Provides
    fun provideLocationLatLngDao(appDatabase: AppDatabase): LocationLatLngDao {
        return appDatabase.locationLatLngDao()
    }
}