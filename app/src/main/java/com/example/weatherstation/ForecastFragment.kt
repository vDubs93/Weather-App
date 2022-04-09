package com.example.weatherstation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherstation.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast){
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentForecastBinding

    @Inject
    lateinit var forecastViewModel: ForecastViewModel
    val args: ForecastFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.setTitle(R.string.forecast_name)
        val zipCode: String? = args.zipCode
        val lat: String? = args.lat
        val lon: String? = args.lon
        if (lat == null)
            forecastViewModel.loadData(zipCode)
        else
            forecastViewModel.loadData(lat, lon)
        binding = FragmentForecastBinding.inflate(layoutInflater)
        return binding.root

    }
    companion object {
        const val TAG = "ForecastFragment"
    }
    override fun onResume() {
        super.onResume()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MyAdapter(listOf())
        if (this::forecastViewModel.isInitialized)
            forecastViewModel.forecastList.observe(this) {
                recyclerView.adapter = MyAdapter(it.list)
            }

    }

    fun onClick() {
        childFragmentManager.beginTransaction().apply{
            childFragmentManager.popBackStack()
        }
    }
}