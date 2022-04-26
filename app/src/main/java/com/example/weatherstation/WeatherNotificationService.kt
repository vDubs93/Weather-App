package com.example.weatherstation

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

class WeatherNotificationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latlon = DoubleArray(2)
    private lateinit var notification: Notification
    private lateinit var api: Api
    private val timer = Timer()
    private val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.applicationContext)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                latlon[0] = it.latitude
                latlon[1] = it.longitude

            }
        api = provideApiService()
        super.onCreate()
    }

    @SuppressLint("NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        loadData()

        createNotificationChannel()
        createNotification()

        //Because I couldn't get intervals to work with the location updates.  Not sure what's going on there.
        timer.schedule(TimeUnit.MINUTES.toMillis(30)) {
            requestNewLocation()
        }
        requestNewLocation()
        startForeground(9455, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy(){
        timer.cancel()
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
            notify(9455, notification)
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

    private fun requestNewLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000L
        locationRequest.fastestInterval = 1000L
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    latlon[0] = location.latitude
                    latlon[1] = location.longitude
                    loadData()
                    createNotification()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

}