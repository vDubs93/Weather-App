package com.example.weatherstation

//import com.bumptech.glide.Glide
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ForecastItemFragment : Fragment() {
    private val args: ForecastItemFragmentArgs by navArgs()
    private lateinit var date: String
    private lateinit var temp: ForecastTemp
    private var pressure: Float = 0.0F
    private var humidity: Float = 0.0F
    private var speed: Float = 0.0F
    private lateinit var weather: WeatherCondition
    private lateinit var description: String
    private lateinit var icon: String

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val instant = Instant.ofEpochSecond(args.data.date)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM dd")
        date = dateTime.format(formatter)
        temp = args.data.temp
        pressure = args.data.pressure
        humidity = args.data.humidity
        speed = args.data.speed
        weather = args.data.weather[0]
        icon = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
        activity?.title = getString(R.string.forecast_item_name, date)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                DisplayForecast()
            }
        }
    }

    @Preview
    @Composable
    fun DisplayForecast() {
        Column {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    AsyncImage(
                        model = icon,
                        contentDescription = "weather-image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(128.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Row {
                    Text(
                        getString(R.string.temp, temp.day.toInt()),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = weather.main,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                Row {
                    Text(
                        text = getString(R.string.high, temp.max.toInt()),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = getString(R.string.humidity, humidity.toInt()),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Row {
                    Text(
                        text = getString(R.string.low, temp.max.toInt()),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = getString(R.string.pressure, pressure.toInt()),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row {
                    Text(
                        text = getString(R.string.wind_speed, speed.toInt()),
                        fontSize = 25.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }

            }

        }
    }
}