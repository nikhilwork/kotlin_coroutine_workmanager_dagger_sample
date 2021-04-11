package com.assignment.weatherapp.model

import com.google.gson.annotations.SerializedName

data class ForecastApiResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("city")
    val city: City?,
    @SerializedName("list")
    val weatherList: List<WeatherApiResponse>?
)