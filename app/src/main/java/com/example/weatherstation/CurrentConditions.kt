package com.example.weatherstation

data class CurrentConditions(
    val weather: List<WeatherCondition>,
    val main: Currents,
    val name: String
)
