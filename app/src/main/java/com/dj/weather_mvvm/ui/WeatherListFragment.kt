package com.dj.weather_mvvm.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_weather_list.*


class WeatherListFragment: Fragment(R.layout.fragment_weather_list){
    private val viewModel: WeatherListViewModel by viewModels {
        InjectorUtils.provideWeatherListViewModelFactory()
    }
    private lateinit var weatherListAdapter: WeatherListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()

        weatherListAdapter = WeatherListAdapter()
        recycler_view.apply{
            addItemDecoration(DividerItemDecoration(this.context, VERTICAL))
            adapter = weatherListAdapter
        }
        recycler_view.adapter = weatherListAdapter

        viewModel.fetchWeatherInfo()
        subscribeObservers()

    }

    private fun subscribeObservers(){
        viewModel.weatherInfo.observe(viewLifecycleOwner){ weatherInfo ->
            weatherListAdapter.submitList(weatherInfo.dailyList)
        }
    }
}