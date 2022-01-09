package com.verges.smartsensors.fragments

import android.Manifest.permission.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.DeviceScanActivity
import com.verges.smartsensors.R
import com.verges.smartsensors.databinding.FragmentMainLocationRequiredBinding


class MainLocationRequiredFragment : Fragment() {
    private var _binding: FragmentMainLocationRequiredBinding? = null
    private val binding get() = _binding!!

    private val permissionArray = mutableListOf(
        BLUETOOTH,
        BLUETOOTH_ADMIN,
//        ACCESS_BACKGROUND_LOCATION,   // no permission popup if enabled!
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    )
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionArray.add(BLUETOOTH_SCAN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainLocationRequiredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            map.entries.filter { !it.value }.forEach {
                Log.e("DEBUG", "${it.key} = ${it.value}")
            }
            if (map.entries.all { it.value }) {
                startActivity(Intent(context, DeviceScanActivity::class.java))
            } else {
                Snackbar.make(view, R.string.permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }
        permissionLauncher.launch(permissionArray.toTypedArray())

        view.findViewById<Button>(R.id.grant_location_button).setOnClickListener {
            permissionLauncher.launch(permissionArray.toTypedArray())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
