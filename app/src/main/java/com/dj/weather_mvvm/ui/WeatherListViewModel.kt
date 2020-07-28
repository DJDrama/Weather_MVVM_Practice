package com.dj.weather_mvvm.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.weather_mvvm.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WeatherListViewModel: ViewModel(){

    fun fetchWeatherInfo(){
        viewModelScope.launch(IO){
            val weatherInfo = WeatherRepository.getInstance().getWeatherData()

        }

    }
}