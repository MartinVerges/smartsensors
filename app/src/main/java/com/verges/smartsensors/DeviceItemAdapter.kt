package com.verges.smartsensors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class DeviceItems internal constructor(var deviceName: String, var deviceAddress: String)

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

        viewHolder.itemLayout.setOnClickListener {
            viewHolder.itemView.findNavController()
                .navigate(R.id.action_DeviceList_to_TankLevel)

/*            if (position.odd) viewHolder.itemView.findNavController()
                .navigate(R.id.action_DeviceList_to_TankLevel)
            else viewHolder.itemView.findNavController()
                .navigate(R.id.action_DeviceList_to_Battery)*/
        }
    }

    override fun getItemCount(): Int = DeviceItemsList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deviceName: TextView = itemView.findViewById(R.id.deviceName)
        var deviceAddress: TextView = itemView.findViewById(R.id.deviceAddress)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.itemLayout)
        val view: View = itemView
    }
}
