package com.assignment.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi


class InternetConnectionManager(val context: Context) {
    /**
     * get Network connectivity status
     * @return true or false boolean value
     */
    fun isNetworkAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           return isAnyActiveConnection()
        }else{
           return isActiveConnectionBelowM()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun isAnyActiveConnection(): Boolean {
        var connection = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            if (network != null) {
                val nc = connectivityManager.getNetworkCapabilities(network)
                connection = nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        return connection
    }

    @Suppress("DEPRECATION")
    fun isActiveConnectionBelowM(): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}