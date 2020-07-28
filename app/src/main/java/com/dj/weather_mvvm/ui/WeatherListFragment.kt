package com.dj.weather_mvvm.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dj.weather_mvvm.R

class WeatherListFragment: Fragment(R.layout.fragment_weather_list){
    private val viewModel: WeatherListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        viewModel.fetchWeatherInfo()
    }
}