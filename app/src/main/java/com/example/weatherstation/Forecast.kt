package com.example.weatherstation

data class ForecastConditions (
    val list: List<DayForecast>,
) {
    val size: Int = list.size

    operator fun get(i: Int): Any {
        return DayForecast(
            list[i].temp,
            list[i].pressure,
            list[i].humidity,
            list[i].date,
            list[i].sunrise,
            list[i].sunset,
            list[i].weather
        )
    }
}