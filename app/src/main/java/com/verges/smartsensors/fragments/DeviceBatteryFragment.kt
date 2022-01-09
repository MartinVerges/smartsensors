package com.verges.smartsensors.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.verges.smartsensors.R
import com.verges.smartsensors.databinding.FragmentBatteryBinding

class DeviceBatteryFragment : Fragment() {
    private var _binding: FragmentBatteryBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatteryBinding.inflate(inflater, container, false)

        val battery = binding.batteryMeterView

        battery.chargeLevel = 20
        battery.color = ContextCompat.getColor(battery.context, R.color.md_red_700)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
