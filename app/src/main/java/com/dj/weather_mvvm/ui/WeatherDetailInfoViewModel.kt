package com.dj.weather_mvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dj.weather_mvvm.model.Daily

class WeatherDetailInfoViewModel(
    daily: Daily
) : ViewModel() {
    val dailyItem: Daily = daily
}