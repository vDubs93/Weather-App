package com.example.weatherstation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val service: Api) : ViewModel() {
    val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()
    private var zipCode: String? = null

    private val _enableButton = MutableLiveData<Boolean>(false)

    val enableButton: LiveData<Boolean>
        get() = _enableButton


    fun updateZipCode(zipCode: String) {
        if(zipCode != this.zipCode){
            this.zipCode = zipCode
            _enableButton.value = isValidZipCode(zipCode)
        }

    }
    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all {it.isDigit()}
    }
    fun loadData() = runBlocking {

        launch { currentConditions.value = zipCode?.let { service.getCurrentConditions(it)
        }}


    }
}