package com.dj.weather_mvvm.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.util.InjectorUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_list.*


class WeatherListFragment : Fragment(R.layout.fragment_weather_list) {
    companion object {
        private const val REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY"
        private const val REQUEST_CODE_PERMISSION = 101
        private const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        private const val MAX_NUMBER_REQUEST_PERMISSIONS = 2
        private const val UPDATE_INTERVAL = 10 * 1000L
        private const val FASTEST_INTERVAL = 2000L
        private const val REQUEST_CHECK_SETTINGS = 1011
    }
    private val viewModel: WeatherListViewModel by viewModels {
        InjectorUtils.provideWeatherListViewModelFactory()
    }
    private lateinit var weatherListAdapter: WeatherListAdapter
    private var mHasPermission: Boolean = false
    private var mPermissionRequestCount: Int = 0
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        locationManager =
            this.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        weatherListAdapter = WeatherListAdapter()
        recycler_view.apply {
            addItemDecoration(DividerItemDecoration(this.context, VERTICAL))
            adapter = weatherListAdapter
        }
        recycler_view.adapter = weatherListAdapter
        subscribeObservers()
        requestPermissionsIfNecessary()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    /**
                     * 1. location save into database
                     * 2. compare on startup
                     * **/
                    fetchWeatherInfo(location)
                    stopLocationUpdates()
                    //just do once so break
                    break
                }
            }
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireContext())
        if (mHasPermission) {
            getLastLocation()
        }
        updateValuesFromBundle(savedInstanceState)

        swipe_refresh_layout.setOnRefreshListener {
            recycler_view.visibility=View.INVISIBLE
            progressBar.visibility=View.VISIBLE
            tv_desc.visibility = View.VISIBLE
            tv_desc.setText(R.string.fetching_location)
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Snackbar.make(
                coordinator_layout,
                R.string.set_permissions_in_settings,
                Snackbar.LENGTH_INDEFINITE
            ).show()
            return
        }
        if (::fusedLocationProviderClient.isInitialized) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    fetchWeatherInfo(it)
                } ?: setLocationSettings()
            }
        }
    }

    private fun fetchWeatherInfo(location: Location){
        tv_desc.setText(R.string.fetching_weather_info)
        viewModel.fetchWeatherInfo(location)
    }


    private fun setLocationSettings() {
        // Create the location request to start receiving updates
        // https://developer.android.com/training/location/change-location-settings
        mLocationRequest = LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val task = settingsClient.checkLocationSettings(locationSettingsRequest)
        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            requestingLocationUpdates = true
            startLocationUpdates()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.

                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (checkPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun requestPermissionsIfNecessary() {
        mHasPermission = checkPermission()
        if (!mHasPermission) {
            if (mPermissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                mPermissionRequestCount++
                requestPermissions(arrayOf(locationPermission), REQUEST_CODE_PERMISSION)
            } else {
                Snackbar.make(
                    coordinator_layout,
                    R.string.set_permissions_in_settings,
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        } else {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if permissions were granted after a permissions request flow.
        if (requestCode == REQUEST_CODE_PERMISSION) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            locationPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun subscribeObservers() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) { weatherInfo ->
            (activity as AppCompatActivity).supportActionBar?.title = weatherInfo.timeZone
            recycler_view.visibility=View.VISIBLE
            progressBar.visibility=View.GONE
            tv_desc.visibility = View.GONE
            if(swipe_refresh_layout.isRefreshing){
                swipe_refresh_layout.isRefreshing = false
            }
            weatherListAdapter.submitList(weatherInfo.dailyList)
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) {
            startLocationUpdates()
        } else {
            setLocationSettings()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()

    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return
        // Update the value of requestingLocationUpdates from the Bundle.
        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(
                REQUESTING_LOCATION_UPDATES_KEY
            )
        }
        // ...
        // Update UI to match restored state
        //updateUI()
    }
}