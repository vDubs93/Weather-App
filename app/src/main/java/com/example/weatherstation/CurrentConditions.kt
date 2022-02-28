package com.example.weatherstation

data class CurrentConditions(
    val weather: List<WeatherCondition>,
    var main: Currents,
    val name: String
)
