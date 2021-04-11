package com.assignment.weatherapp.retrofit

import com.assignment.weatherapp.model.ForecastApiResponse
import com.assignment.weatherapp.model.WeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebApiInterface {
    @GET("weather")
    suspend fun getWeatherDetail(
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("appid") appId: String) : WeatherApiResponse

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("appid") appId: String) : ForecastApiResponse
}