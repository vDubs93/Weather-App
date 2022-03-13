package com.example.weatherstation

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.car.ui.AlertDialogBuilder

import com.example.weatherstation.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    @Inject
    lateinit var searchViewModel: SearchViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.setTitle(R.string.search_name)
        binding = FragmentSearchBinding.inflate(layoutInflater)
        searchViewModel.enableButton.observe(this) { enable ->
            binding.button.isEnabled = enable
        }
        binding.button.setOnClickListener{
            try {
                searchViewModel.loadData()
                val currentConditions = searchViewModel.currentConditions.value!!
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentFragment(
                    currentConditions, binding.zipCode.text.toString())
                findNavController().navigate(action)
            } catch (e:retrofit2.HttpException) {
                ZipErrorDialogFragment().show(childFragmentManager,ZipErrorDialogFragment.TAG)
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

}

