package com.example.weatherstation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pro.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        api = retrofit.create(Api::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(listOf())
    }

    override fun onResume() {
        super.onResume()
        val call: Call<Forecast> = api.getForecastConditions("55104")
        call.enqueue(
        object : Callback<Forecast> {
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>
            ) {

                val forecastConditions = response.body()
                forecastConditions?.let {
                    recyclerView.adapter = MyAdapter(forecastConditions.list)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                println("Failed to retrieve data")
            }

        })
    }
    fun onClick() {
        finish()
    }
}