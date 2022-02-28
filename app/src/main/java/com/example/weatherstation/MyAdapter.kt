package com.example.weatherstation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherstation.databinding.RowDateBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) :
    RecyclerView.Adapter<MyAdapter.RowDateViewHolder>() {

    inner class RowDateViewHolder(private val binding: RowDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NewApi")
        fun bind(data: DayForecast) {

            val instant = Instant.ofEpochSecond(data.date)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("MMM dd")
            binding.date.text = formatter.format(dateTime)
            val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")
            val riseInstant = Instant.ofEpochSecond(data.sunrise)
            val riseTime = LocalDateTime.ofInstant(riseInstant, ZoneId.systemDefault())
            val setInstant = Instant.ofEpochSecond(data.sunset)
            val setTime = LocalDateTime.ofInstant(setInstant, ZoneId.systemDefault())
            LocalDateTime.ofInstant(setInstant, ZoneId.systemDefault())

            val temp = data.temp


            binding.Temp.text = binding.Temp.context.getString(R.string.temp, temp.day.toInt())
            binding.high.text =
                binding.high.context.getString(R.string.forecastHigh, temp.max.toInt())
            binding.low.text = binding.low.context.getString(R.string.forecastLow, temp.min.toInt())
            binding.sunrise.text =
                binding.sunrise.context.getString(R.string.sunrise, timeFormatter.format(riseTime))
            binding.sunset.text =
                binding.sunset.context.getString(R.string.sunset, timeFormatter.format(setTime))

            val icon = data.weather.firstOrNull()
            icon?.let {
                Glide.with(binding.sunIcon)
                    .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                    .into(binding.sunIcon)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowDateViewHolder {
        println("ViewHolder created")
        val view = RowDateBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RowDateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RowDateViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}