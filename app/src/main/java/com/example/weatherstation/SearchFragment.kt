package com.example.weatherstation

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherstation.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    @Inject
    lateinit var searchViewModel: SearchViewModel
    private val REQUEST_LOCATION_ACCESS : Int = 2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.setTitle(R.string.search_name)

        binding = FragmentSearchBinding.inflate(layoutInflater)
        searchViewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.button.isEnabled = enable
        }

        binding.button.setOnClickListener{
            searchViewModel.loadData()
            if (searchViewModel.navigate.value == true){
                val currentConditions = searchViewModel.currentConditions.value!!
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentFragment(
                    currentConditions, binding.zipCode.text.toString(), "", "")
                findNavController().navigate(action)
            } else {
                ZipErrorDialogFragment().show(childFragmentManager, ZipErrorDialogFragment.TAG)
            }
        }
        binding.button2.setOnClickListener{

            requestLocationPermission()
            println("requesting Permission")
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
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)){
            showLocationPermissionRationale()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_ACCESS
            )
        }

    }
    private fun showLocationPermissionRationale() {
        AlertDialog.Builder(requireActivity())
            .setMessage(R.string.location_permission_rationale)
            .setNeutralButton(R.string.ok) {_,_ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION_ACCESS
                )
            }
            .create()
            .show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        println("Checking Permission Result")
        if (requestCode == 1) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                (activity as MainActivity).requestingLocationUpdates = true
                searchViewModel.loadData()

                val currentConditions = searchViewModel.currentConditions.value!!
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentFragment(
                    currentConditions, "", searchViewModel.lat, searchViewModel.lon
                )
                findNavController().navigate(action)
            }
        }
    }
}

