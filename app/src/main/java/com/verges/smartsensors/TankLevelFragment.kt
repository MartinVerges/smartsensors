package com.verges.smartsensors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.verges.smartsensors.databinding.FragmentTankLevelBinding

class TankLevelFragment : Fragment() {
    private var _binding: FragmentTankLevelBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTankLevelBinding.inflate(inflater, container, false)

        with(binding.waveLoadingView) {
            progressValue = 75
            setAnimDuration(2000)
        }

        binding.tanklevelValue.text = getString(R.string.tanklevel_value, 75)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
