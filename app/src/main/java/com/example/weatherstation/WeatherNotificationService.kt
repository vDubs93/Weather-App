package com.example.weatherstation

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate

class WeatherNotificationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latlon = DoubleArray(2)
    private var notification: Notification? = null
    private lateinit var api: Api
    private val timer = Timer()
    private lateinit var locationCallback: LocationCallback
    private val locationRequest = LocationRequest.create()
    private val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        locationRequest.interval = 180000L
        locationRequest.fastestInterval = 180000L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    println("Got new location")
                    latlon[0] = location.latitude
                    latlon[1] = location.longitude
                    loadData()
                    createNotification()
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                println("Got last known location")

                    latlon[0] = it.latitude
                    latlon[1] = it.longitude


            }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    @SuppressLint("NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        api = provideApiService()
        loadData()

        createNotificationChannel()
        createNotification()
        startForeground(9455, notification)
        return START_STICKY
    }

    override fun onDestroy(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }
    @SuppressLint("InlinedApi")
    private fun createNotification() {
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
        loadData()
        notification = NotificationCompat.Builder(this, "WeatherUpdates")
            .setSmallIcon(R.drawable.sun)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(
                getString(
                    R.string.notification_text,
                    currentConditions.value?.name,
                    currentConditions.value?.main?.temp?.toInt()
                )
            )
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(this)) {
            notify(9455, notification!!)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("WeatherUpdates", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun loadData() = runBlocking {
        launch {
            try {
                currentConditions.value =
                    api.getCurrentConditionsLatLon(latlon[0].toString(),
                        latlon[1].toString()
                    )
            } catch (e: retrofit2.HttpException) {
                //let it die quietly
            }
        }
    }
    private fun provideApiService(): Api {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pro.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(Api::class.java)
    }
}