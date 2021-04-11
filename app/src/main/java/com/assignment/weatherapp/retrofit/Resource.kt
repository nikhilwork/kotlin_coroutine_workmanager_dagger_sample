package com.assignment.weatherapp.retrofit

sealed class Resource<T> {
    class Success<T>(val data: T?) : Resource<T>()
    class Error<T>(val resId: Int) : Resource<T>()
}