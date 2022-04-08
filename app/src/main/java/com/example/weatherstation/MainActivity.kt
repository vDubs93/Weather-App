package com.example.weatherstation

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
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
    var requestingLocationUpdates: Boolean = false

    private var locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult){
            locationResult ?: return
            for (location in locationResult.locations){
                println(location.latitude)
                println(location.longitude)
                searchViewModel.updateLatLon(location.latitude.toString(), location.longitude.toString())
            }
            searchViewModel.loadData()
        }

    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)


    }
    override fun onResume() {
        super.onResume()
        if(requestingLocationUpdates) startLocationUpdates()
    }
    fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        print("Got location")
        try {
            var locationRequest: LocationRequest = LocationRequest.create()
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } catch (e: SecurityException){}
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 2) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("PermissionGranted")
                requestingLocationUpdates = true
            } else {
                PermissionDeniedDialogFragment().show(supportFragmentManager, PermissionDeniedDialogFragment.TAG)
            }
        }
    }

}

