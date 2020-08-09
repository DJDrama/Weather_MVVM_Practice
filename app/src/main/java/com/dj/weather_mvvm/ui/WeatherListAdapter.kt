package com.dj.weather_mvvm.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dj.weather_mvvm.databinding.DailyWeatherViewItemBinding
import com.dj.weather_mvvm.model.Daily

class WeatherListAdapter :
    ListAdapter<Daily, WeatherListAdapter.DailyWeatherViewHolder>(DiffCallback) {

    class DailyWeatherViewHolder(private var binding: DailyWeatherViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.dailyWeather?.let { daily ->
                    navigateToDetail(daily, it)
                }
            }
        }

        fun bind(daily: Daily) {
            binding.apply {
                dailyWeather = daily
                //Forces the data binding to execute immediately
                //allows the RecyclerView to make the correct view size measurements
                executePendingBindings()
            }
        }

        private fun navigateToDetail(daily: Daily, view: View) {
            val directions =
                WeatherListFragmentDirections.actionWeatherListFragmentToWeatherDetailInfoFragment(
                    daily = daily
                )
            view.findNavController().navigate(directions)
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.dt == newItem.dt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        return DailyWeatherViewHolder(
            DailyWeatherViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily)
    }


}