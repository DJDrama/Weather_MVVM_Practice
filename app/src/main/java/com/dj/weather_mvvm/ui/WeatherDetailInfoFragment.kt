package com.dj.weather_mvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.databinding.FragmentWeatherDetailInfoBinding
import com.dj.weather_mvvm.util.InjectorUtils
class WeatherDetailInfoFragment : Fragment(R.layout.fragment_weather_detail_info){

    private val args: WeatherDetailInfoFragmentArgs by navArgs()
    private val weatherDetailInfoViewModel: WeatherDetailInfoViewModel by viewModels{
        InjectorUtils.provideWeatherDetailInfoViewModelFactory(args.daily)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWeatherDetailInfoBinding>(
            inflater, R.layout.fragment_weather_detail_info, container, false
        )
        binding.apply{
            viewModel = weatherDetailInfoViewModel
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}