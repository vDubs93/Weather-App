package com.example.weatherstation

import com.squareup.moshi.Json

data class Currents(
    val temp: Float,
    @Json(name = "feels_like") val feelsLike: Float,
    @Json(name = "temp_max") val high: Float,
    @Json(name = "temp_min") val low: Float,
    val pressure: Float,
    val humidity: Float
)
