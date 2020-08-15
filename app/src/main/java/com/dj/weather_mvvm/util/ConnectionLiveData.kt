package com.dj.weather_mvvm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData


/** https://medium.com/@alexzaitsev/android-viewmodel-check-network-connectivity-state-7c028a017cd4 **/

class ConnectionLiveData(private val context: Context) : LiveData<Boolean>() {
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            postValue(context.isConnected)
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onInactive() {
        super.onInactive()
        try {
            context.unregisterReceiver(networkReceiver)
        } catch (e: Exception) {
        }
    }
}

val Context.isConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected == true