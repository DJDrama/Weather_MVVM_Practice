package com.dj.weather_mvvm.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dj.weather_mvvm.databinding.DailyWeatherViewItemBinding
import com.dj.weather_mvvm.model.Daily

interface DailyItemClickListener{
    fun onDailyItemClicked(dailyItem: Daily)
}
class WeatherListAdapter
    constructor(
        private val dailyItemClickListener: DailyItemClickListener?
    ):
    ListAdapter<Daily, WeatherListAdapter.DailyWeatherViewHolder>(DiffCallback) {

    class DailyWeatherViewHolder(private var binding: DailyWeatherViewItemBinding,
                                 private val dailyItemClickListener: DailyItemClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.dailyWeather?.let { daily ->
                    dailyItemClickListener?.onDailyItemClicked(daily)
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
            ),
            dailyItemClickListener
        )
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily)
    }


}