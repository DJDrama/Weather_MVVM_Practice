package com.dj.weather_mvvm.ui.main.more

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dj.weather_mvvm.R
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment: Fragment(R.layout.fragment_more){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cl_set_my_location.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_googleMapFragment2)
        }
    }


}