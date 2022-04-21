package com.example.weatherstation

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.car.ui.AlertDialogBuilder

class PermissionDeniedDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.location_permission_denied))
            .setPositiveButton(getString(R.string.ok)){ _,_ -> }
            .create()
    companion object {
        const val TAG = "PermDeniedErrorDialog"
    }
}
