package com.assignment.weatherapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.assignment.weatherapp.constants.SharedPrefConstants
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.model.Main
import com.assignment.weatherapp.model.Weather
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


class CommonUtils {

    companion object{

        fun closeKeyBoard(context: Context, view: View?) {
            if (view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun getRoundTemperatureWithStatus(main: Main?, weather: List<Weather>?): String{
            var res = "";
            if (main!=null && main.temp!=null){
                res = "${Math.round(main.temp)}°"
            }
            return "$res ${getWeatherStatus(weather)}"
        }

        fun getRoundTemperature(temp: Double?): Long{
            var res: Long = 0;
            if (temp!=null ){
                res = Math.round(temp)
            }
            return res
        }

        fun getWeatherStatus(weather: List<Weather>?): String{
            if (weather!=null && weather.size>0){
                return  weather[0].main?:""
            }
            return ""
        }

        fun getRoundMinMaxTemperature(main: Main?): String{
            if (main!=null){
                val minTemp = main.tempMin
                val maxTemp = main.tempMax
                if (minTemp!=null && maxTemp!=null){
                    val min = Math.round(minTemp)
                    val max = Math.round(maxTemp)
                    return "$min°/$max°"
                }
            }
            return ""
        }

        fun getStringFromObject(myObject: Any): String{
            val gson = Gson()
            return gson.toJson(myObject)
        }

        fun getDateTimeFromTimestamp(time: Long?): String{
            if (time!=null) {
                val date = Date(time*1000)
                val dateTime: String = SimpleDateFormat("dd/MM/yyyy HH:mm").format(date)
                return dateTime
            }else{
                return "--"
            }
        }

        fun getDateFromTimestamp(time: Long?): String{
            if (time!=null) {
                val date = Date(time*1000)
                val dateTime: String = SimpleDateFormat("dd MMM    EEE").format(date)
                return dateTime
            }else{
                return "--"
            }
        }

        fun getMonthDayFromTimestamp(time: Long?): Int{
            if (time!=null) {
                val cal = Calendar.getInstance()
                val date = Date(time*1000)
                cal.time = date
                val dayOfMonth: Int = cal.get(Calendar.DAY_OF_MONTH)
                return dayOfMonth
            }else{
                return -1
            }
        }

        fun getFilterDailyData(list: List<WeatherApiResponse>): MutableList<WeatherApiResponse> {
            var listForecast = mutableListOf<WeatherApiResponse>()
            var lastDate = getMonthDayFromTimestamp(System.currentTimeMillis()/1000)
            var maxTemp: Long = 0
            var minTemp: Long = 100
            for (i in 0..list.size-1){
                if (list[i].main!=null) {
                    val newDate = getMonthDayFromTimestamp(list[i].dt)
                    val itemMaxTemp = getRoundTemperature(list[i].main?.tempMax)
                    val itemMinTemp = getRoundTemperature(list[i].main?.tempMin)
                    if (itemMaxTemp > maxTemp) maxTemp = itemMaxTemp
                    if (itemMinTemp < minTemp) minTemp = itemMinTemp
                    if (newDate>0 && newDate!=lastDate && i>0){
                        list[i-1].main?.tempMin = minTemp.toDouble()
                        list[i-1].main?.tempMax = maxTemp.toDouble()
                        listForecast.add(list[i-1])
                        lastDate = newDate
                        maxTemp = 0
                        minTemp = 100
                    }
                    if (i==list.size-1){
                        list[i].main?.tempMin = minTemp.toDouble()
                        list[i].main?.tempMax = maxTemp.toDouble()
                        listForecast.add(list[i])
                    }
                }
            }
            return listForecast
        }
    }

}