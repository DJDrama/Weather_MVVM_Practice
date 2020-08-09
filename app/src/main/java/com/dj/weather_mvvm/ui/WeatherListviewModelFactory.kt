package com.dj.weather_mvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dj.weather_mvvm.repository.WeatherRepository

class WeatherListviewModelFactory(
    private val repository: WeatherRepository
//    owner: SavedStateRegistryOwner,
//    defaultArgs: Bundle? = null
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherListViewModel(repository) as T
    }
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel?> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ): T {
//        return PlantListViewModel(repository, handle) as T
//    }
}
