package com.dj.weather_mvvm.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dj.weather_mvvm.R
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_google_map.*

class GoogleMapFragment : Fragment(R.layout.fragment_google_map),
    com.google.android.libraries.maps.OnMapReadyCallback {

    companion object{
        const val ZOOM_LEVEL = 13f
    }
    private val viewModel: GoogleMapFragmentViewModel by viewModels()
    private val args: GoogleMapFragmentArgs? by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)

        subscribeObservers()
        args?.let {
            val location = Location("")
            location.latitude = it.latitude?.toDouble()!!
            location.longitude = it.longitude?.toDouble()!!
            viewModel.setLocation(location)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let {
            viewModel.setMapReady(true)
            viewModel.setGoogleMap(it)
        }
    }

    private fun subscribeObservers() {
        viewModel.mapReadyAndLocationMediatorLiveData.observe(viewLifecycleOwner) {
            it.run {
                if(isMapReady){
                   this.googleMap?.let{gMap->
                       this.location?.let{location->
                           with(gMap) {
                               moveCamera(
                                   CameraUpdateFactory.newLatLngZoom(
                                       LatLng(
                                           location.latitude,
                                           location.longitude
                                       ),
                                       ZOOM_LEVEL
                                   )
                               )
                           }
                       }
                   }
                }
            }
        }
    }


}