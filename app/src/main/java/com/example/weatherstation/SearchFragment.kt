package com.example.weatherstation

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.service.notification.StatusBarNotification
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherstation.databinding.FragmentSearchBinding
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    @Inject
    lateinit var searchViewModel: SearchViewModel
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var showingNotifications: Boolean = false

    @SuppressLint("newAPI")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.setTitle(R.string.search_name)
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    requestLocationUpdates()
                }
                else -> {
                    PermissionDeniedDialogFragment().show(childFragmentManager, PermissionDeniedDialogFragment.TAG)
                }
            }
        }
        val mNotificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = mNotificationManager.activeNotifications
        for(notification in notifications)
            showingNotifications = (notification.id == 9455)


        binding = FragmentSearchBinding.inflate(layoutInflater)
        if (showingNotifications)
            binding.button3.text = getString(R.string.disableNotifications)
        else
            binding.button3.text = getString(R.string.startNotifications)
        searchViewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.button.isEnabled = enable
        }

        binding.button.setOnClickListener{
            searchViewModel.loadDataZip()
            if (searchViewModel.navigate.value == true){
                navigateToCurrentZip()
            } else {
                ZipErrorDialogFragment().show(childFragmentManager, ZipErrorDialogFragment.TAG)
            }
        }

        binding.button2.setOnClickListener {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        binding.button3.setOnClickListener {
            if (!showingNotifications){
                requestForegroundPermission()
                showingNotifications = true
                binding.button3.text = getString(R.string.disableNotifications)
            } else {
                this.context?.stopService(Intent(this.context, WeatherNotificationService::class.java))
                showingNotifications = false
                binding.button3.text = getString(R.string.startNotifications)
            }
        }

        binding.zipCode.addTextChangedListener (
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.toString()?.let{searchViewModel.updateZipCode(it)}

                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }
        )

        return binding.root
    }


    private fun navigateToCurrentZip() {
        val zip: String = binding.zipCode.text.toString()
        val currentConditions = searchViewModel.currentConditions.value!!
        val action = SearchFragmentDirections.actionSearchFragmentToCurrentFragment(
            currentConditions, zip, null, null
        )
        findNavController().navigate(action)
    }

    private fun navigateToCurrentLatLon() {
        val lat: Double? = searchViewModel.lat
        val lon: Double? = searchViewModel.lon
        val currentConditions = searchViewModel.currentConditions.value!!
        val action = SearchFragmentDirections.actionSearchFragmentToCurrentFragment(
            currentConditions,null, lat.toString(), lon.toString())
        findNavController().navigate(action)
    }
    @SuppressLint("NewApi")
    private fun requestForegroundPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.FOREGROUND_SERVICE), 3)
        }
        val intent = Intent(this.context, WeatherNotificationService::class.java)
        this.context?.startForegroundService(intent)

    }
    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 2)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                requestNewLocation()
                if (it != null) {
                    Log.d("SearchFragment", it.toString())
                    searchViewModel.updateLatLon(it.latitude, it.longitude)
                    searchViewModel.loadDataLatLon()

                        navigateToCurrentLatLon()
                    }

            }
    }


    private fun requestNewLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1800000L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    println(location.latitude)
                    println(location.longitude)
                    searchViewModel.updateLatLon(location.latitude, location.longitude)

                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

}

