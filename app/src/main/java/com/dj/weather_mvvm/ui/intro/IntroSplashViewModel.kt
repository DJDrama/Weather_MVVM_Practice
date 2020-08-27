package com.dj.weather_mvvm.ui.intro

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dj.weather_mvvm.repository.WeatherRepository


class IntroSplashViewModel
@ViewModelInject
constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _location: MutableLiveData<Location> = MutableLiveData()
    val location: MutableLiveData<Location>
        get() = _location


    init{

    }

}