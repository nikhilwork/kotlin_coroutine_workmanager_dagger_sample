package com.assignment.weatherapp.utils

import android.content.Context
import com.assignment.weatherapp.constants.SharedPrefConstants
import javax.inject.Inject

class SharedPrefStorage @Inject constructor(context: Context){

    private val sharedPref = context.getSharedPreferences(SharedPrefConstants.WEATHER_APP_PREF, Context.MODE_PRIVATE)

    fun putString(key: String, value: String){
        with(sharedPref.edit()){
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String{
        return sharedPref.getString(key, "")!!
    }

}