package com.example.weatherstation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel() {

    val forecastList: MutableLiveData<Forecast> = MutableLiveData()

    fun loadData(zipCode: String) = runBlocking {
        launch { forecastList.value = service.getForecastConditionsZip(zipCode) }

    }
    fun loadData(lat: String, lon: String) = runBlocking {
        launch {forecastList.value = service.getForecastConditionsLatLon(lat, lon)}
    }
}