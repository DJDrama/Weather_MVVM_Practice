package com.dj.weather_mvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.databinding.FragmentWeatherDetailInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailInfoFragment : Fragment(R.layout.fragment_weather_detail_info){

    private val sharedViewModel: WeatherSharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWeatherDetailInfoBinding>(
            inflater, R.layout.fragment_weather_detail_info, container, false
        )
        binding.apply{
            viewModel = sharedViewModel
        }
        return binding.root
    }
}