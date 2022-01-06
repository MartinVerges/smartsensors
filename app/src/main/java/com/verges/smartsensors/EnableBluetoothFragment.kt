package com.verges.smartsensors

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.databinding.FragmentEnableBluetoothBinding

class EnableBluetoothFragment : Fragment() {
    private var _binding: FragmentEnableBluetoothBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnableBluetoothBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bluetoothManager = requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        if (adapter.isEnabled) {
            view.findNavController()
                .navigate(EnableBluetoothFragmentDirections.actionEnableBluetoothFragmentToLocationRequiredFragment())
        }

        val requestBt = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                Snackbar.make(view, R.string.permission_granted, Snackbar.LENGTH_LONG).show()
                view.findNavController()
                    .navigate(EnableBluetoothFragmentDirections.actionEnableBluetoothFragmentToLocationRequiredFragment())
            } else {
                Snackbar.make(view, R.string.permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }

        view.findViewById<Button>(R.id.enable_bluetooth_button).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    permissions.entries.forEach {
                        Log.d("SMARTSENSOR", "${it.key} = ${it.value}")
                    }
                }.launch(
                    arrayOf(
                        BLUETOOTH_SCAN,
                        BLUETOOTH_CONNECT
                    )
                )
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBt.launch(enableBtIntent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
