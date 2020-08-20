package com.dj.weather_mvvm.ui

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dj.weather_mvvm.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_google_map.*

class GoogleMapFragment : Fragment(R.layout.fragment_google_map),
    com.google.android.libraries.maps.OnMapReadyCallback {
    companion object {
        const val ZOOM_LEVEL = 13f
    }
    private val viewModel: WeatherSharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)
        subscribeObservers()
        btn_set_current_location.setOnClickListener {
            viewModel.getGoogleMap()?.let {
                it.cameraPosition.target.run {
                    val location = Location("")
                    location.latitude = latitude
                    location.longitude = longitude
                    viewModel.setLocation(location)
                    findNavController().navigateUp()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        p0?.let {
            it.setOnCameraMoveListener {
                btn_set_current_location.text = resources.getString(R.string.camera_moving)
                btn_set_current_location.isEnabled = false
            }
            it.setOnCameraIdleListener {
                btn_set_current_location.text =
                    resources.getString(R.string.set_current_location_text)
                btn_set_current_location.isEnabled = true
            }
            viewModel.setMapReady(true)
            viewModel.setGoogleMap(it)
        }
    }

    private fun subscribeObservers() {
        viewModel.mapReadyAndLocationMediatorLiveData.observe(viewLifecycleOwner) {
            it.run {
                if (isMapReady) {
                    this.googleMap?.let { gMap ->
                        this.location?.let { location ->
                            with(gMap) {
                                val latLng = LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                                moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        latLng,
                                        ZOOM_LEVEL
                                    )
                                )
                                clear()
                                addMarker(MarkerOptions().position(latLng))
                            }
                        }
                    }
                }
            }
        }
    }
}