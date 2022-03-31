package com.example.weatherstation

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CurrentViewModel @Inject constructor(private val service : Api): ViewModel() {
}