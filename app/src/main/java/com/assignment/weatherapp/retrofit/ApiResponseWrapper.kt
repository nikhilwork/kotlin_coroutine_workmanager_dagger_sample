package com.assignment.weatherapp.retrofit

import com.assignment.weatherapp.R
import com.assignment.weatherapp.model.ForecastApiResponse
import com.assignment.weatherapp.model.WeatherApiResponse
import retrofit2.HttpException
import java.net.HttpURLConnection

class ApiResponseWrapper {
    companion object{
        fun createWeatherResponse(res: WeatherApiResponse): Resource<WeatherApiResponse>{
            if (res!=null) {
                return Resource.Success(res)
            } else {
                return Resource.Error(R.string.server_error)
            }
        }

        fun createForecastResponse(res: ForecastApiResponse): Resource<ForecastApiResponse>{
            if (res!=null) {
                return Resource.Success(res)
            } else {
                return Resource.Error(R.string.server_error)
            }
        }

        fun <T>createException(e: Throwable): Resource<T>{
            if (e is HttpException) {
                if (e.code() == HttpURLConnection.HTTP_NOT_FOUND){
                    return Resource.Error(R.string.city_not_found)
                }
            }
            return Resource.Error(R.string.server_error)
        }
    }
}