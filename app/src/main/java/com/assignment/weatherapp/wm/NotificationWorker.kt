package com.assignment.weatherapp.wm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.assignment.weatherapp.R
import com.assignment.weatherapp.constants.AppConstants
import com.assignment.weatherapp.constants.SharedPrefConstants
import com.assignment.weatherapp.model.Main
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.repo.HomeRepository
import com.assignment.weatherapp.retrofit.Resource
import com.assignment.weatherapp.ui.WeatherNotificationHelper
import com.assignment.weatherapp.utils.CommonUtils
import com.assignment.weatherapp.utils.SharedPrefStorage

class NotificationWorker(
    val context: Context,
    params: WorkerParameters,
    val homeRepository: HomeRepository,
    val notificationHelper: WeatherNotificationHelper,
    val sharedPref: SharedPrefStorage
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val location: String = sharedPref.getString(SharedPrefConstants.KEY_LAST_LOCATION)
        if (location.trim().length > 1) {
            callWeatherDetailApi(location)
            callForecastApi(location)
        }
        return Result.success()
    }

    /**
     * Call weather predictions api to get next 5 days weather predictions
     * @param location : combination of city and country
     */
    private suspend fun callForecastApi(location: String) {
        val weatherForecast = homeRepository.callWeatherForecastApi(location)
        when(weatherForecast){
            is Resource.Success -> {
                var city = ""
                var listForecast = mutableListOf<WeatherApiResponse>()
                if (weatherForecast.data?.weatherList!=null) {
                    city = weatherForecast.data.city?.name?: ""
                    listForecast.addAll(CommonUtils.getFilterDailyData(weatherForecast.data.weatherList as MutableList<WeatherApiResponse>))
                }
                if (listForecast.size > 1) {
                    val weatherApiResponse: WeatherApiResponse = listForecast[1]
                    if (weatherApiResponse.main != null) {
                        sendPredictionNotification(weatherApiResponse.main, city)
                    }
                }
            }
            is Resource.Error -> {
                //Nothing to do
            }
        }
    }

    /**
     * Call weather detail api to get current weather detail
     * @param location : combination of city and country
     */
    private suspend fun callWeatherDetailApi(location: String) {
        val weatherDetail = homeRepository.callWeatherDetailApi(location)
        when(weatherDetail){
            is Resource.Success -> {
                val weatherApiResponse: WeatherApiResponse? = weatherDetail.data
                if (weatherApiResponse!=null) {
                    sendNotification(weatherApiResponse)
                }
            }
            is Resource.Error -> {
                //Nothing to do
            }
        }
    }

    /**
     * Check condition for current temperature notification
     * if current temperature is greater than 30 or less than 10 then send local notification
     * @param weatherApiResponse the [WeatherApiResponse]
     */
    private fun sendNotification(weatherApiResponse: WeatherApiResponse,) {
        if (weatherApiResponse.main!=null && weatherApiResponse.main.temp!=null){
            val res = Math.round(weatherApiResponse.main.temp)
            if (res < 10){
                sendNotification(
                    "${weatherApiResponse.name?:""} $res°",
                    context.getString(R.string.temperature_less_than_ten),
                    AppConstants.NOTIFICATION_ID_TODAY)
            }else if (res > 30){
                sendNotification(
                    "${weatherApiResponse.name?:""} $res°",
                    context.getString(R.string.temperature_more_than_thirty),
                    AppConstants.NOTIFICATION_ID_TODAY)
            }
        }
    }

    /**
     * Check condition for prediction notification
     * if tomorrow temperature is greater than 30 or less than 10 then send local notification
     * @param main the [Main]
     * @param city name of city as [String]
     */
    private fun sendPredictionNotification(main: Main, city: String) {
        val minTemp = main.tempMin
        val maxTemp = main.tempMax
        if (minTemp!=null && maxTemp!=null) {
            val roundedMinTemp = Math.round(minTemp)
            val roundedMaxTemp = Math.round(maxTemp)
            if (roundedMinTemp < 10){
                sendNotification(
                    "$city Tomorrow $minTemp°",
                    context.getString(R.string.tomorrow_temperature_less_than_ten),
                    AppConstants.NOTIFICATION_ID_TOMORROW)
            }else if (roundedMaxTemp > 30){
                sendNotification(
                    "$city Tomorrow $maxTemp°",
                    context.getString(R.string.tomorrow_temperature_more_than_thirty),
                    AppConstants.NOTIFICATION_ID_TOMORROW)
            }
        }
    }

    /**
     * Send local notification to device
     * @param title the title of notification
     * @param content the message of notification
     * @param notId the Notification
     */
    private fun sendNotification(title: String, content: String, notId: Int){
        notificationHelper.showWeatherNotification(context, title, content, notId)
    }

}