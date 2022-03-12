package com.example.weatherstation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(private val service: Api) : ViewModel() {

    val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()

    fun loadData() = runBlocking {

        launch { currentConditions.value = service.getCurrentConditions("55104") }

    }
}