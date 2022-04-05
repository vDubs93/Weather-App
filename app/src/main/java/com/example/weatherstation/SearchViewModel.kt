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
    private val _navigate = MutableLiveData(false)
    private val _enableButton = MutableLiveData(false)
    var lat: String = ""
    var lon: String = ""
    val enableButton: LiveData<Boolean>
        get() = _enableButton
    val navigate: LiveData<Boolean>
        get() = _navigate
    fun updateZipCode(zipCode: String) {
        if(zipCode != this.zipCode){
            this.zipCode = zipCode
            _enableButton.value = isValidZipCode(zipCode)
        }
    }
    fun updateLatLon(lat: String, lon: String){
        this.lat = lat
        this.lon = lon
    }
    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all {it.isDigit()}
    }
    fun loadData() = runBlocking {
        launch {
            if (lat != "" && lon != "") {
                try {
                    currentConditions.value = service.getCurrentConditionsLatLon(lat, lon)
                    _navigate.value = true
                } catch (e: retrofit2.HttpException) {
                    _navigate.value = false
                }
            } else {
                try {
                    currentConditions.value = zipCode?.let {
                        service.getCurrentConditionsZip(it)
                    }
                    _navigate.value = true
                } catch (e: retrofit2.HttpException) {
                    _navigate.value = false
                }
            }
        }
    }
}
