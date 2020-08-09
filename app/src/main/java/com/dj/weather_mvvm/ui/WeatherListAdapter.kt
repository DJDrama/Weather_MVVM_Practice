package com.dj.weather_mvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dj.weather_mvvm.databinding.DailyWeatherViewItemBinding
import com.dj.weather_mvvm.model.Daily

class WeatherListAdapter :
    ListAdapter<Daily, WeatherListAdapter.DailyWeatherViewHolder>(DiffCallback) {

    class DailyWeatherViewHolder(private var binding: DailyWeatherViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init{
            binding.setClickListener{

            }
        }
        fun bind(daily: Daily) {
            binding.dailyWeather = daily
            //Forces the data binding to execute immediately
            //allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
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
        return DailyWeatherViewHolder(DailyWeatherViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily)
    }


}