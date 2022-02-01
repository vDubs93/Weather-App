package com.example.weatherstation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val adapterData = listOf<DayForecast>(
        DayForecast(1643733360, 1643706915, 1643757915, ForecastTemp(72F,68F,76F), 1023F, 100),
        DayForecast(1643819760, 1643793375, 1643844375, ForecastTemp(68F,60F,75F), 1023F, 100),
        DayForecast(1643906160, 1643879835, 1643930835, ForecastTemp(71F,59F,80F), 1023F, 100),
        DayForecast(1643992560, 1643966295, 1644017295, ForecastTemp(69F,67F,74F), 1023F, 100),
        DayForecast(1644078960, 1644052755, 1644103755, ForecastTemp(55F,47F,65F), 1023F, 100),
        DayForecast(1644165360, 1644139215, 1644147015, ForecastTemp(64F,62F,69F), 1023F, 100),
        DayForecast(1644251760, 1644225675, 1644276675, ForecastTemp(73F,69F,82F), 1023F, 100),
        DayForecast(1644338160, 1644312135, 1644363135, ForecastTemp(74F,70F,83F), 1023F, 100),
        DayForecast(1644424560, 1644398595, 1644449595, ForecastTemp(72F,70F,79F), 1023F, 100),
        DayForecast(1644510960, 1644485055, 1644536055, ForecastTemp(65F,59F,69F), 1023F, 100),
        DayForecast(1644597360, 1644571515, 1644622515, ForecastTemp(65F,58F,75F), 1023F, 100),
        DayForecast(1644683760, 1644657975, 1644708975, ForecastTemp(70F,62F,78F), 1023F, 100),
        DayForecast(1644770160, 1644744435, 1644795435, ForecastTemp(68F,62F,77F), 1023F, 100),
        DayForecast(1644856560, 1644830895, 1644881895, ForecastTemp(67F,58F,73F), 1023F, 100),
        DayForecast(1644942960, 1644917355, 1644968355, ForecastTemp(64F,53F,69F), 1023F, 100),
        DayForecast(1645029360, 1645003815, 1645054815, ForecastTemp(66F,60F,72F), 1023F, 100),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(adapterData)
    }

    fun onClick() {
        finish()
    }
}