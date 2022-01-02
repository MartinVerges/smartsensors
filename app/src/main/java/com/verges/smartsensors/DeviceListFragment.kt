package com.verges.smartsensors

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.verges.smartsensors.databinding.FragmentDeviceListBinding

class DeviceListFragment : Fragment() {
    private var _binding: FragmentDeviceListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var itemsList: MutableList<DeviceItems> = mutableListOf()
    private fun prepareItems() {
        for (i in 0..49) {
            val item = DeviceItems("Item$i", (20 + i).toString())
            itemsList.add(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)

        itemsList.clear()
        with(binding.deviceListView) {
            adapter = DeviceItemAdapter(itemsList)
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        }
        prepareItems()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
