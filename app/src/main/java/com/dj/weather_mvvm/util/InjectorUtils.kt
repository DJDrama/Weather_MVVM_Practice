package com.dj.weather_mvvm.util

import android.content.Context
import androidx.fragment.app.Fragment
import com.dj.weather_mvvm.db.AppDatabase
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.repository.WeatherRepository
import com.dj.weather_mvvm.ui.WeatherDetailInfoViewModel
import com.dj.weather_mvvm.ui.WeatherInfoDetailViewModelFactory
import com.dj.weather_mvvm.ui.WeatherListviewModelFactory

object InjectorUtils{

    private fun getWeatherRepository(context: Context): WeatherRepository{
        return WeatherRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).weatherInfoDao()
        )
    }

     fun provideWeatherListViewModelFactory(fragment: Fragment): WeatherListviewModelFactory {
        val repository = getWeatherRepository(fragment.requireContext())
        return WeatherListviewModelFactory(repository)
    }

    fun provideWeatherDetailInfoViewModelFactory(daily: Daily): WeatherInfoDetailViewModelFactory{
        return WeatherInfoDetailViewModelFactory(daily)
    }


}