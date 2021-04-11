package com.assignment.weatherapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.assignment.weatherapp.constants.AppConstants.Companion.WM_INITIAL_DELAY
import com.assignment.weatherapp.constants.AppConstants.Companion.WM_PERIODIC_INTERVAL
import com.assignment.weatherapp.constants.SharedPrefConstants
import com.assignment.weatherapp.di.utils.ViewModelFactory
import com.assignment.weatherapp.databinding.ActivityHomeBinding
import com.assignment.weatherapp.model.ForecastApiResponse
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.ui.WeatherNotificationHelper
import com.assignment.weatherapp.utils.CommonUtils
import com.assignment.weatherapp.utils.SharedPrefStorage
import com.assignment.weatherapp.wm.NotificationWorker
import dagger.android.support.DaggerAppCompatActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var sharedPref: SharedPrefStorage
    private val viewModel: HomeActivityViewModel by viewModels{viewModelFactory}
    private lateinit var binding: ActivityHomeBinding
    private var listForecast = mutableListOf<WeatherApiResponse>()
    private lateinit var forecastListAdapter: DailyForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerViewAdapter()
        viewModel.init(true)

        viewModel.isLoadingLiveData.observe(this){
            if (it)binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
        }

        viewModel.errorMsgLiveData.observe(this) {
            showErrorMessage(getString(it))
        }

        viewModel.weatherLiveData.observe(this){
            handleWeatherDetailResult(it)
        }

        viewModel.forecastLiveData.observe(this){
            handleForecastResult(it)
        }

        binding.ivRefresh.setOnClickListener {
            viewModel.init(false)
        }

        binding.btnSearch.setOnClickListener {
            val location = "${binding.etCityName.text},${binding.etCountryCode.text}"
            callWebApis(location)
        }
    }

    private fun callWebApis(location: String){
        viewModel.callWeatherDetailApi(location)
        viewModel.callWeatherForecastApi(location)
        CommonUtils.closeKeyBoard(this, this.currentFocus)
    }

    private fun setRecyclerViewAdapter() {
        forecastListAdapter = DailyForecastAdapter(listForecast)
        binding.rvForecast.layoutManager = LinearLayoutManager(this)
        binding.rvForecast.adapter = forecastListAdapter
    }

    private fun handleForecastResult(data: ForecastApiResponse) {
        listForecast.clear()
        if (data.weatherList!=null) {
            listForecast.addAll(CommonUtils.getFilterDailyData(data.weatherList as MutableList<WeatherApiResponse>))
        }
        forecastListAdapter.notifyDataSetChanged()
    }

    private fun handleWeatherDetailResult(data: WeatherApiResponse){
        updateViews(data)
        sharedPref.putString(SharedPrefConstants.KEY_LAST_WEATHER_RESULT, CommonUtils.getStringFromObject(data))
        val lastLocation: String = sharedPref.getString(SharedPrefConstants.KEY_LAST_LOCATION)
        val currentLocation = "${data.name},${data.sys?.country?:""}"
        if(!lastLocation.equals(currentLocation,true)) {
            sharedPref.putString(SharedPrefConstants.KEY_LAST_LOCATION, currentLocation)
            initializeJobToWorkManager()
        }
    }

    private fun updateViews(weatherApiResponse: WeatherApiResponse) {
        binding.tvTemperature.text = CommonUtils.getRoundTemperatureWithStatus(weatherApiResponse.main, weatherApiResponse.weather);
        binding.tvLocation.text = weatherApiResponse.name?:""
        binding.tvLastSyncTime.text = CommonUtils.getDateTimeFromTimestamp(weatherApiResponse.dt)
    }

    private fun showErrorMessage(message: String?){
        if (message != null && message.trim().length > 0) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeJobToWorkManager(){
        WorkManager.getInstance(applicationContext).cancelAllWork()
        val notificationWork = PeriodicWorkRequest
            .Builder(NotificationWorker::class.java, WM_PERIODIC_INTERVAL, TimeUnit.HOURS)
            .setInitialDelay(WM_INITIAL_DELAY, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(notificationWork)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("ON-onDestroy")
    }

    override fun onPause() {
        super.onPause()
        println("ON-onPause")
    }

    override fun onStop() {
        super.onStop()
        println("ON-onStop")
    }

    override fun onRestart() {
        super.onRestart()
        println("ON-onRestart")
    }

    override fun onResume() {
        super.onResume()
        println("ON-onResume")
    }

    override fun onStart() {
        super.onStart()
        println("ON-onStart")
    }

}