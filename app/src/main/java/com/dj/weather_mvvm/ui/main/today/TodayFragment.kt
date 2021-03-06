package com.dj.weather_mvvm.ui.main.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dj.weather_mvvm.R
import com.dj.weather_mvvm.databinding.FragmentTodayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private val todayViewModel: TodayViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTodayBinding>(
            inflater, R.layout.fragment_today, container, false
        )
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = todayViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayViewModel.getTodayDailyItemFromDatabaseIfNotNull()
        subscribeObservers()
    }
    private fun subscribeObservers(){
        todayViewModel.timeZone.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                (requireActivity() as AppCompatActivity).supportActionBar?.title = it
            }
        }
    }
}