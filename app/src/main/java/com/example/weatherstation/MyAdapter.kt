package com.example.weatherstation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val dateView: TextView = view.findViewById(R.id.date)
        private val tempView: TextView = view.findViewById(R.id.Temp)
        private val highView: TextView = view.findViewById(R.id.high)
        private val lowView: TextView = view.findViewById(R.id.low)
        private val sunriseView: TextView = view.findViewById(R.id.sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.sunset)
        private val iconView: ImageView = view.findViewById(R.id.sunIcon)

        @SuppressLint("NewApi")
        fun bind(data: DayForecast) {

            val instant = Instant.ofEpochSecond(data.date)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("MMM dd")
            dateView.text = formatter.format(dateTime)
            val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")
            val riseInstant = Instant.ofEpochSecond(data.sunrise)
            val riseTime = LocalDateTime.ofInstant(riseInstant,ZoneId.systemDefault())
            val setInstant = Instant.ofEpochSecond(data.sunset)
            val setTime = LocalDateTime.ofInstant(setInstant,ZoneId.systemDefault())
            LocalDateTime.ofInstant(setInstant,ZoneId.systemDefault())

            val temp = data.temp


            tempView.text = tempView.context.getString(R.string.temp, temp.day.toInt())
            highView.text = highView.context.getString(R.string.forecastHigh, temp.max.toInt())
            lowView.text = lowView.context.getString(R.string.forecastLow, temp.min.toInt())
            sunriseView.text = sunriseView.context.getString(R.string.sunrise,timeFormatter.format(riseTime))
            sunsetView.text = sunsetView.context.getString(R.string.sunset,timeFormatter.format(setTime))
            val iconName = data.weather.firstOrNull()?.icon
            val iconURL = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(iconView)
                .load(iconURL)
                .into(iconView)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("ViewHolder created")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}