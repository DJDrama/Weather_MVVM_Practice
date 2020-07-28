package com.dj.weather_mvvm.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dj.weather_mvvm.R
import kotlinx.coroutines.delay

class SplashFragment: Fragment(R.layout.fragment_splash){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        navToWeatherListFragment()
    }

    private fun navToWeatherListFragment(){
        lifecycleScope.launchWhenStarted {
            delay(1000)
            findNavController().navigate(R.id.action_splashFragment_to_weatherListFragment)
        }
    }
}