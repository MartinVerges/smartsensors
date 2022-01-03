package com.verges.smartsensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        /*binding.enableBluetoothButton.setOnClickListener {
            (activity as MainActivity).checkPermissionAndForward()
        }*/
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
