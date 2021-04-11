package com.assignment.weatherapp

import androidx.work.Configuration
import com.assignment.weatherapp.di.DaggerAppComponent
import com.assignment.weatherapp.repo.HomeRepository
import com.assignment.weatherapp.ui.WeatherNotificationHelper
import com.assignment.weatherapp.utils.SharedPrefStorage
import com.assignment.weatherapp.wm.MyWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class WeatherApplication : DaggerApplication(), Configuration.Provider {

    @Inject
    lateinit var sharedPref: SharedPrefStorage
    @Inject
    lateinit var notificationHelper: WeatherNotificationHelper
    @Inject
    lateinit var homeRepository: HomeRepository

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(MyWorkerFactory(homeRepository, notificationHelper, sharedPref))
            .build()
}