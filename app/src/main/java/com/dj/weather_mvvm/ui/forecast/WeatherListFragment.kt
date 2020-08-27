package com.dj.weather_mvvm.ui.forecast

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.ui.DailyItemClickListener
import com.dj.weather_mvvm.ui.WeatherListAdapter
import com.dj.weather_mvvm.ui.WeatherSharedViewModel
import com.dj.weather_mvvm.util.ConnectionLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_weather_list.*


@AndroidEntryPoint
class WeatherListFragment : Fragment(R.layout.fragment_weather_list), DailyItemClickListener {

    private val viewModel: WeatherSharedViewModel by activityViewModels()
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var weatherListAdapter: WeatherListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)
        connectionLiveData = ConnectionLiveData(this.requireContext())

        weatherListAdapter = WeatherListAdapter(this)
        recycler_view.apply {
            addItemDecoration(DividerItemDecoration(this.context, VERTICAL))
            adapter = weatherListAdapter
        }
        recycler_view.adapter = weatherListAdapter
        subscribeObservers()


        swipe_refresh_layout.setOnRefreshListener {
            recycler_view.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            tv_desc.visibility = View.VISIBLE
            tv_desc.setText(R.string.fetching_location)
            //getLastLocation()
            /** setting to null so we can get new latitude and longitude and fetch new weatherInfo on refresh **/
            viewModel.setLocation(null)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_set_my_location, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_set_my_location -> {
                viewModel.setMyLocationClicked(true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


//
//    private fun fetchWeatherInfo(location: Location) {
//        tv_desc.setText(R.string.fetching_weather_info)
//        viewModel.fetchWeatherInfo(location)
//    }
//





    private fun subscribeObservers() {
        viewModel.location.observe(viewLifecycleOwner) { location ->
            location?.let {
                fetchWeatherInfo(location)
            } ?: if (requestingLocationUpdates) {
                startLocationUpdates()
            } else {
                setLocationSettings()
            }
        }
        viewModel.weatherInfo.observe(viewLifecycleOwner) { weatherInfo ->
            (activity as AppCompatActivity).supportActionBar?.title = weatherInfo.timeZone
            recycler_view.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            tv_desc.visibility = View.GONE
            if (swipe_refresh_layout.isRefreshing) {
                swipe_refresh_layout.isRefreshing = false
            }
            weatherListAdapter.submitList(weatherInfo.dailyList)
        }
        connectionLiveData.observe(viewLifecycleOwner) {
            viewModel.setNetworkAvailability(it)
        }
        viewModel.setMyLocationClickedLiveData.observe(viewLifecycleOwner) {
            if (it) {
                val directions =
                    WeatherListFragmentDirections.actionWeatherListFragmentToGoogleMapFragment(
                        latitude = viewModel.location?.value?.latitude.toString(),
                        longitude = viewModel.location?.value?.longitude.toString()
                    )
                findNavController().navigate(directions)
                viewModel.setMyLocationClicked(false)
            }
        }
        viewModel.isDailyItemClicked.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_weatherListFragment_to_weatherDetailInfoFragment)
                viewModel.setDailyItemClicked(false)
            }
        }
    }



    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }






    override fun onDailyItemClicked(dailyItem: Daily) {
        viewModel.setDailyItem(dailyItem)
        viewModel.setDailyItemClicked(true)
    }
}