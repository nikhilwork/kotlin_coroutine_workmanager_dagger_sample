package com.assignment.weatherapp.repo

import com.assignment.weatherapp.R
import com.assignment.weatherapp.constants.AppConstants
import com.assignment.weatherapp.constants.SharedPrefConstants
import com.assignment.weatherapp.model.ForecastApiResponse
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.repo.HomeRepository
import com.assignment.weatherapp.retrofit.ApiResponseWrapper
import com.assignment.weatherapp.retrofit.Resource
import com.assignment.weatherapp.retrofit.WebApiInterface
import com.assignment.weatherapp.utils.InternetConnectionManager
import com.assignment.weatherapp.utils.SharedPrefStorage
import com.google.gson.Gson
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    val webApiInterface: WebApiInterface,
    val sharedPref: SharedPrefStorage,
    val internetConnectionManager: InternetConnectionManager) :
    HomeRepository {

    /**
     * Get last api response from shared preference
     * @return [WeatherApiResponse] if data available in preference else null
     */
    private fun getLastApiResponseFromSharedPref(): WeatherApiResponse?{
        val lastData = sharedPref.getString(SharedPrefConstants.KEY_LAST_WEATHER_RESULT)
        return if (lastData.isEmpty()){
            null
        }else {
            val lastWeatherApiResponse: WeatherApiResponse? =  Gson().fromJson(lastData,WeatherApiResponse::class.java)
            lastWeatherApiResponse
        }
    }

    override suspend fun getLastData(): WeatherApiResponse? {
        return getLastApiResponseFromSharedPref()
    }

    /**
     * A suspend function to call weather detail api
     * @return [Resource.Success.data] if api response success else [Resource.Error.resId] of error message
     * @param location : combination of city and country
     */
    override suspend fun callWeatherDetailApi(location: String): Resource<WeatherApiResponse> {
        return if (internetConnectionManager.isNetworkAvailable()) {
            try {
                val res = webApiInterface.getWeatherDetail(location, AppConstants.WEATHER_API_UNITS, AppConstants.WEATHER_API_KEY)
                ApiResponseWrapper.createWeatherResponse(res);
            } catch (e: Throwable) {
                ApiResponseWrapper.createException(e);
            }
        }else{
            Resource.Error(R.string.internet_error)
        }
    }

    /**
     * A suspend function to call weather predictions api
     * @return [Resource.Success.data] if api response success else [Resource.Error.resId] of error message
     * @param location : combination of city and country
     */
    override suspend fun callWeatherForecastApi(location: String): Resource<ForecastApiResponse> {
        return if (internetConnectionManager.isNetworkAvailable()) {
            try {
                val res = webApiInterface.getWeatherForecast(location, AppConstants.WEATHER_API_UNITS, AppConstants.WEATHER_API_KEY)
                ApiResponseWrapper.createForecastResponse(res);
            } catch (e: Throwable) {
                ApiResponseWrapper.createException(e);
            }
        }else{
            Resource.Error(R.string.internet_error)
        }
    }
}

