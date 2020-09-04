package com.dj.weather_mvvm.ui.main.more

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.dj.weather_mvvm.R
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_google_map.*

@AndroidEntryPoint
class GoogleMapFragment : Fragment(R.layout.fragment_google_map),
    com.google.android.libraries.maps.OnMapReadyCallback {
    companion object {
        const val ZOOM_LEVEL = 13f
    }
    private val viewModel: GoogleMapViewModel by viewModels()
    private var mapView: MapView?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map)
        mapView?.let{
            it.onCreate(savedInstanceState)
            it.getMapAsync(this)
        }
        subscribeObservers()
        btn_set_current_location.setOnClickListener {
            viewModel.getGoogleMap()?.let {
                it.cameraPosition.target.run {
                    val location = Location("")
                    location.latitude = latitude
                    location.longitude = longitude
                    viewModel.setLocation(location)
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
                                    location.lat.toDouble(),
                                    location.lon.toDouble()
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
        viewModel.isLocationLatLngInserted.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(), "Set Complete!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }else{
                Toast.makeText(requireContext(), "Please Try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        viewModel.getGoogleMap()?.clear()
        mapView?.onDestroy()
        mapView = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}