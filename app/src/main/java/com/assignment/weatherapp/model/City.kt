package com.assignment.weatherapp.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("coord")
    val coord: Coord?,
)
