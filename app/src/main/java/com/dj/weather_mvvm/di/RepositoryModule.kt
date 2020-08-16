package com.dj.weather_mvvm.di

import com.dj.weather_mvvm.api.ApiService
import com.dj.weather_mvvm.db.WeatherInfoDao
import com.dj.weather_mvvm.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherInfoDao: WeatherInfoDao,
        retrofit: ApiService
    ): WeatherRepository{
        return WeatherRepository(weatherInfoDao, retrofit)
    }
}