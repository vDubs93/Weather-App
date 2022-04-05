package com.example.weatherstation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherstation.databinding.FragmentCurrentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentFragment : Fragment(R.layout.fragment_current
) {

    private lateinit var zipCode: String
    private lateinit var lat: String
    private lateinit var lon: String
    private lateinit var binding: FragmentCurrentBinding
    @Inject
    lateinit var currentViewModel: CurrentViewModel
    private lateinit var currentConditions: CurrentConditions
    val args: CurrentFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.setTitle(R.string.current_name)
        currentConditions = args.currentConditions
        zipCode = args.zipCode
        lat = args.lat
        lon = args.lon
        binding = FragmentCurrentBinding.inflate(layoutInflater)
        binding.forecastButton.setOnClickListener {
            val zipCode: String = this.zipCode
            val action = CurrentFragmentDirections.actionCurrentFragmentToForecastFragment(zipCode, lat, lon)
            findNavController().navigate(action)
        }
        bindData(currentConditions)
        return binding.root
    }

    companion object {
        const val TAG = "CurrentConditionsFragment"


    }

    private fun bindData(currentConditions: CurrentConditions) {
        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(
            R.string.temperature, currentConditions.main.temp.toInt()
        )
        binding.feelsLike.text = getString(
            R.string.feels_like, currentConditions.main.feelsLike.toInt()
        )
        binding.low.text = getString(
            R.string.low, currentConditions.main.low.toInt()
        )
        binding.high.text = getString(
            R.string.high, currentConditions.main.high.toInt()
        )
        binding.humidity.text = getString(
            R.string.humidity, currentConditions.main.humidity.toInt()
        )
        binding.pressure.text = getString(
            R.string.pressure, currentConditions.main.pressure.toInt()
        )
        val icon = currentConditions.weather.firstOrNull()
        icon?.let {
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                .into(binding.sunIcon)
        }

    }
}