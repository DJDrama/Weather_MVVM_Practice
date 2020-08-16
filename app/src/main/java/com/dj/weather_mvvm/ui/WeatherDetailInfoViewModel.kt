package com.dj.weather_mvvm.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dj.weather_mvvm.model.Daily

class WeatherDetailInfoViewModel : ViewModel() {
    private val _dailyItem: MutableLiveData<Daily> = MutableLiveData()
    val dailyItem: LiveData<Daily>
        get() = _dailyItem


    fun setDailyData(daily: Daily) {
        _dailyItem.value = daily
    }
}