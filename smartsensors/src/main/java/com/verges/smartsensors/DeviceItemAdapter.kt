package com.verges.smartsensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.verges.smartsensors.fragments.DeviceListFragmentDirections
import com.verges.smartsensors.fragments.DeviceTankLevelFragment
import com.verges.smartsensors.fragments.DeviceTankLevelFragmentDirections

class DeviceItemAdapter
internal constructor(private val DeviceItemsList: MutableList<DeviceItems>) :
    RecyclerView.Adapter<DeviceItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_row_device_entry, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = DeviceItemsList[position]
        viewHolder.deviceName.text = item.deviceName
        viewHolder.deviceAddress.text = item.deviceAddress
        viewHolder.rssi.text = item.rssi

        viewHolder.itemLayout.setOnClickListener {
            viewHolder.itemView.findNavController()
                .navigate(DeviceListFragmentDirections.actionDeviceListToTankLevel(
                    deviceName = item.deviceName,
                    deviceAddress = item.deviceAddress
                ))
        }
    }

    override fun getItemCount(): Int = DeviceItemsList.size

    class DeviceItems internal constructor(var deviceName: String, var deviceAddress: String, var rssi: String)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deviceName: TextView = itemView.findViewById(R.id.deviceName)
        var deviceAddress: TextView = itemView.findViewById(R.id.deviceAddress)
        var rssi: TextView = itemView.findViewById(R.id.signalStrengthText)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.itemLayout)
        val view: View = itemView
    }
}

