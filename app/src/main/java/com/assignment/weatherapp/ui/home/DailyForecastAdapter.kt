package com.assignment.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.weatherapp.databinding.ItemDailyForecastBinding
import com.assignment.weatherapp.model.WeatherApiResponse
import com.assignment.weatherapp.utils.CommonUtils

class DailyForecastAdapter(
    private val weatherList: List<WeatherApiResponse>
    ) : RecyclerView.Adapter<DailyForecastAdapter.MyViewHolder>() {


    class MyViewHolder(val binding: ItemDailyForecastBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvDay.text = CommonUtils.getDateFromTimestamp(weatherList[position].dt)
        holder.binding.tvHumidity.text = weatherList[position].main?.humidity.toString() + "%"
        holder.binding.tvMinMax.text = CommonUtils.getRoundMinMaxTemperature(weatherList[position].main)
        holder.binding.tvStatus.text = CommonUtils.getWeatherStatus(weatherList[position].weather)
    }

    override fun getItemCount(): Int {
       return weatherList.size
    }
}
