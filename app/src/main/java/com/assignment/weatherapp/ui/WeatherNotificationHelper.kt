package com.assignment.weatherapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.assignment.weatherapp.R
import com.assignment.weatherapp.constants.AppConstants
import com.assignment.weatherapp.constants.SharedPrefConstants
import com.assignment.weatherapp.ui.home.HomeActivity
import com.assignment.weatherapp.utils.SharedPrefStorage
import javax.inject.Inject

class WeatherNotificationHelper @Inject constructor(val context: Context, val sharedPref: SharedPrefStorage){

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AppConstants.CHANNEL_ID,
                AppConstants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
                .apply {
                    description = AppConstants.CHANNEL_DESC
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showWeatherNotification(context: Context, textTitle: String, textContent: String, notId: Int){
        createNotificationChannel()
        val homeIntent = Intent(context, HomeActivity::class.java)
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(context, AppConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cloud)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notId, builder.build())
        }
    }

}