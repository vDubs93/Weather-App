package com.example.weatherstation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CurrentViewModel @Inject constructor(private val service: Api) : ViewModel() {

    val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()

}