package com.assignment.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.weatherapp.R
import com.assignment.weatherapp.model.ForecastApiResponse
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.repo.HomeRepository
import com.assignment.weatherapp.retrofit.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private var weatherMutableLiveData = MutableLiveData<WeatherApiResponse>()
    var weatherLiveData: LiveData<WeatherApiResponse> = weatherMutableLiveData

    private var forecastMutableLiveData = MutableLiveData<ForecastApiResponse>()
    var forecastLiveData: LiveData<ForecastApiResponse> = forecastMutableLiveData

    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = isLoading

    private val errorMsg = MutableLiveData<Int>()
    val errorMsgLiveData: LiveData<Int> = errorMsg

    /**
     * Call weather detail api to get current weather detail
     * @param location : combination of city and country
     */
    fun callWeatherDetailApi(location: String){
        if (location.trim().length > 1) {
          viewModelScope.launch {
              isLoading.value = true
              val result =  repository.callWeatherDetailApi(location)
              isLoading.value = false
             when(result){
                 is Resource.Success -> {
                     weatherMutableLiveData.value = result.data
                 }
                 is Resource.Error -> {
                     errorMsg.value = result.resId
                 }
             }
          }
        }else {
            errorMsg.value =R.string.enter_city_country_code
        }
    }

    /**
     * Call weather predictions api to get next 5 days weather predictions
     * @param location : combination of city and country
     */
    fun callWeatherForecastApi(location: String){
        if (location.trim().length > 1) {
            viewModelScope.launch {
                val result = repository.callWeatherForecastApi(location)
                when(result){
                    is Resource.Success -> {
                        forecastMutableLiveData.value = result.data
                    }
                    is Resource.Error -> {
                        errorMsg.value = result.resId
                    }
                }
            }
        }
    }

    /**
     * Check preference data for last search and call api with saved location
     * @param updateWeatherData `true` if want to update weather live data too
     */
    fun init(updateWeatherData: Boolean){
        viewModelScope.launch {
           val data =  repository.getLastData()
            if (data != null){
                if (updateWeatherData) weatherMutableLiveData.value = data
                val location = "${data.name ?: ""},${data.sys?.country ?: ""}"
                callWeatherDetailApi(location)
                callWeatherForecastApi(location)
            }
        }
    }
}