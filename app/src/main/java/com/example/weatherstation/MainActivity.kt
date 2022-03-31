package com.example.weatherstation

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val apiKey = "6d3cbb90efe05909395139d822e2357b"
    @Inject
    lateinit var currentViewModel: CurrentViewModel
    @Inject
    lateinit var forecastViewModel: ForecastViewModel
    @Inject
    lateinit var searchViewModel: SearchViewModel
}
