package com.dj.weather_mvvm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.util.InjectorUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_list.*


class WeatherListFragment : Fragment(R.layout.fragment_weather_list) {
    companion object {
        private const val REQUEST_CODE_PERMISSION = 101
        private const val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        private const val MAX_NUMBER_REQUEST_PERMISSIONS = 2
    }

    private val viewModel: WeatherListViewModel by viewModels {
        InjectorUtils.provideWeatherListViewModelFactory()
    }
    private lateinit var weatherListAdapter: WeatherListAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mHasPermission: Boolean = false
    private var mPermissionRequestCount: Int = 0

    private lateinit var locationManager: LocationManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        requestPermissionsIfNecessary()
        if (!mHasPermission) {
            return
        }
        locationManager =
            this.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            turnOnGps()
        } else {
            getCurrentLocation()
        }
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
//        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            location?.let{
//                viewModel.fetchWeatherInfo(it)
//            }
//        }

        weatherListAdapter = WeatherListAdapter()
        recycler_view.apply {
            addItemDecoration(DividerItemDecoration(this.context, VERTICAL))
            adapter = weatherListAdapter
        }
        recycler_view.adapter = weatherListAdapter

        subscribeObservers()

    }

    private fun turnOnGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        builder.setMessage("Enable GPS").setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val location: Location? =
            locationManager.getLastKnownLocation(GPS_PROVIDER)
        location?.let{
            viewModel.fetchWeatherInfo(location)

        } ?:Toast.makeText(this.requireContext(), "Unable to find location.", Toast.LENGTH_SHORT).show()
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
        var hasPermission: Boolean
        hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            locationPermission
        ) == PackageManager.PERMISSION_GRANTED
        return hasPermission
    }

    private fun subscribeObservers() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) { weatherInfo ->
            (activity as AppCompatActivity).supportActionBar?.setTitle(weatherInfo.timeZone)
            weatherListAdapter.submitList(weatherInfo.dailyList)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (requestingLocationUpdates) startLocationUpdates()
//    }
//
//    private fun startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(locationRequest,
//            locationCallback,
//            Looper.getMainLooper())
//    }
}