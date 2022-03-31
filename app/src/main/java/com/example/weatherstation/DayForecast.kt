package com.example.weatherstation

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayForecast(
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Float,
    @Json(name = "dt") val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val weather: List<WeatherCondition>
) : Parcelable
