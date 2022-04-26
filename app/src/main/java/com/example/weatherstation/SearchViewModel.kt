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
    var lat: Double? = null
    var lon: Double? = null
    private val _navigate = MutableLiveData(false)
    private val _enableButton = MutableLiveData(false)
    private val _latLonSet = MutableLiveData(false)
    private val _notificationButton = MutableLiveData(false)
    val notificationButton: LiveData<Boolean>
        get() = _notificationButton
    val enableButton: LiveData<Boolean>
        get() = _enableButton
    val navigate: LiveData<Boolean>
        get() = _navigate
    val latLonSet: LiveData<Boolean>
        get() = _latLonSet
    fun updateZipCode(zipCode: String) {
        if(zipCode != this.zipCode){
            this.zipCode = zipCode
            _enableButton.value = isValidZipCode(zipCode)
        }
    }
    fun updateLatLon(lat: Double, lon: Double){
        this.lat = lat
        this.lon = lon
        println("lat and lon set")
        _latLonSet.value = true
    }
    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all {it.isDigit()}
    }
    fun loadDataLatLon() = runBlocking {
        launch {
            try {
                currentConditions.value =
                        service.getCurrentConditionsLatLon(lat.toString(),
                            lon.toString()
                        )

                _navigate.value = true
            } catch (e: retrofit2.HttpException) {
                _navigate.value = false
            }
        }
    }
    fun loadDataZip() = runBlocking {
        launch {
            if (isValidZipCode(zipCode.toString())){
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
