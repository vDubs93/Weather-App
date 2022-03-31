package com.example.weatherstation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ForecastTemp(val day: Float, val min: Float, val max: Float) : Parcelable


