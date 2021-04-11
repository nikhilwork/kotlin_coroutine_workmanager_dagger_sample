package com.assignment.weatherapp.repo

import com.assignment.weatherapp.model.ForecastApiResponse
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.retrofit.Resource

interface HomeRepository {

    suspend fun getLastData():WeatherApiResponse?
    suspend fun callWeatherDetailApi(location: String): Resource<WeatherApiResponse>
    suspend fun callWeatherForecastApi(location: String): Resource<ForecastApiResponse>
}