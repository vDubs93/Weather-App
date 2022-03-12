package com.example.weatherstation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel() {

    val forecastList: MutableLiveData<Forecast> = MutableLiveData()

    fun loadData() = runBlocking {
        launch { forecastList.value = service.getForecastConditions("55104") }

    }
}