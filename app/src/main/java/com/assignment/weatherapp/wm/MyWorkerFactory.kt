package com.assignment.weatherapp.wm

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.assignment.weatherapp.repo.HomeRepository
import com.assignment.weatherapp.ui.WeatherNotificationHelper
import com.assignment.weatherapp.utils.SharedPrefStorage

class MyWorkerFactory(
    private val homeRepository: HomeRepository,
    private val notificationHelper: WeatherNotificationHelper,
    private val sharedPref: SharedPrefStorage
    ) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return NotificationWorker(appContext, workerParameters, homeRepository, notificationHelper, sharedPref)
    }
}