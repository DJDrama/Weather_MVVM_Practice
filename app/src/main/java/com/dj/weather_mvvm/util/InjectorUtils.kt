package com.dj.weather_mvvm.util

import androidx.fragment.app.Fragment
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.repository.WeatherRepository
import com.dj.weather_mvvm.ui.WeatherDetailInfoViewModel
import com.dj.weather_mvvm.ui.WeatherInfoDetailViewModelFactory
import com.dj.weather_mvvm.ui.WeatherListviewModelFactory

object InjectorUtils{

    private fun getWeatherRepository(): WeatherRepository{
        return WeatherRepository.getInstance()
    }


     fun provideWeatherListViewModelFactory(/*fragment: Fragment*/): WeatherListviewModelFactory {
        val repository = getWeatherRepository()
        return WeatherListviewModelFactory(repository)
    }

    fun provideWeatherDetailInfoViewModelFactory(daily: Daily): WeatherInfoDetailViewModelFactory{
        return WeatherInfoDetailViewModelFactory(daily)
    }


}