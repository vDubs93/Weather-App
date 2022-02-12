package com.example.weatherstation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onResume(){
        super.onResume()
        setContentView(R.layout.activity_main)
    }

    fun switchActivity(v: View?) {
        startActivity(Intent(this@MainActivity, ForecastActivity::class.java))
    }
}
