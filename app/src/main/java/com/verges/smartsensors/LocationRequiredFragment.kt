package com.verges.smartsensors

import android.Manifest.permission.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.databinding.FragmentLocationRequiredBinding
import androidx.core.app.ActivityCompat.requestPermissions




class LocationRequiredFragment : Fragment() {
    private var _binding: FragmentLocationRequiredBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val permissionArray = arrayOf(
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        ACCESS_BACKGROUND_LOCATION,
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationRequiredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { p ->
            var isGranted = true
            p.entries.forEach {
                if (!it.value) {
                    isGranted = false
                    Log.e("DEBUG", "${it.key} = ${it.value}")
                }
            }
            if (isGranted) {
                startActivity(Intent(context, DeviceScanActivity::class.java))
            } else {
                Snackbar.make(view, R.string.permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }
        permissionLauncher.launch(permissionArray)

        view.findViewById<Button>(R.id.grant_location_button).setOnClickListener {
            permissionLauncher.launch(permissionArray)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}