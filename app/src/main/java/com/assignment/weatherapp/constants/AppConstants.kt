package com.assignment.weatherapp.constants

class AppConstants {
    companion object {
        const val WEATHER_API_BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val HTTP_CONNECTION_TIMEOUT = 30L
        const val HTTP_READ_WRITE_TIMEOUT = 30L
        const val WEATHER_API_KEY = "072c729446853ccbb648f486c76c15e7"
        const val WEATHER_FORECAST_API_KEY = "b617fa27a03947aae6116eaf5313b313"
        const val WEATHER_API_UNITS = "metric"
        const val CHANNEL_ID = "com.assignment.weather.channelId"
        const val CHANNEL_NAME = "WeatherChannel1"
        const val CHANNEL_DESC = "Weather update notification"
        const val NOTIFICATION_ID_TODAY = 1
        const val NOTIFICATION_ID_TOMORROW = 2
        const val WM_PERIODIC_INTERVAL = 12L
        const val WM_INITIAL_DELAY = 10L
    }
}