package com.example.weatherstation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {


    private val apiKey = "6d3cbb90efe05909395139d822e2357b"

    private lateinit var api: Api
    private lateinit var cityName: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView
    private lateinit var high: TextView
    private lateinit var low: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView
    private lateinit var icon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityName = findViewById(R.id.city_name)
        temperature = findViewById(R.id.temperature)
        feelsLike= findViewById(R.id.feels_like)
        high= findViewById(R.id.high)
        low= findViewById(R.id.low)
        humidity= findViewById(R.id.humidity)
        pressure= findViewById(R.id.pressure)
        icon = findViewById(R.id.sunIcon)

        setUpButtons()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

    }

    override fun onResume(){
        super.onResume()
        val call: Call<CurrentConditions> = api.getCurrentConditions("55104")
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentConditions = response.body()
                currentConditions?.let {
                    bindData(currentConditions)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {

            }

        })


        setUpButtons()
    }

    private fun setUpButtons(){
        val forecastButton = findViewById<Button>(R.id.forecast_button)
        forecastButton.setOnClickListener{
            startActivity(Intent(this@MainActivity, ForecastActivity::class.java))
        }
    }
    private fun bindData(currentConditions: CurrentConditions){
        cityName.text = currentConditions.name
        temperature.text = getString(
            R.string.temperature, currentConditions.main.temp.toInt()
        )
        feelsLike.text = getString(
            R.string.feels_like, currentConditions.main.feelsLike.toInt()
        )
        low.text = getString(
            R.string.low, currentConditions.main.low.toInt()
        )
        high.text = getString(
            R.string.high, currentConditions.main.high.toInt()
        )
        humidity.text = getString(
            R.string.humidity, currentConditions.main.humidity.toInt()
        )
        pressure.text = getString(
            R.string.pressure, currentConditions.main.pressure.toInt()
        )
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconURL = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconURL)
            .into(icon)

    }
}
