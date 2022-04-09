package com.example.weatherstation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), ActivityCompat.OnRequestPermissionsResultCallback{
    private val apiKey = "6d3cbb90efe05909395139d822e2357b"
    @Inject
    lateinit var currentViewModel: CurrentViewModel
    @Inject
    lateinit var forecastViewModel: ForecastViewModel
    @Inject
    lateinit var searchViewModel: SearchViewModel
}

