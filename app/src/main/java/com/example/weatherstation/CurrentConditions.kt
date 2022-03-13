package com.example.weatherstation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class CurrentConditions(
    val weather: List<WeatherCondition>,
    var main: Currents,
    val name: String
) : Parcelable
