package com.example.weatherstation

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currents(
    val temp: Float,
    @Json(name = "feels_like") val feelsLike: Float,
    @Json(name = "temp_max") val high: Float,
    @Json(name = "temp_min") val low: Float,
    val pressure: Float,
    val humidity: Float
) : Parcelable
