package com.example.weatherstation

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.car.ui.AlertDialogBuilder

class ZipErrorDialogFragment: DialogFragment() {
     override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.invalid_zip_code))
            .setPositiveButton(getString(R.string.ok)){ _,_ -> }
            .create()
    companion object {
        const val TAG = "ZipErrorDialog"
    }
}