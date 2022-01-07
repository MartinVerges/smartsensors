package com.verges.smartsensors
/*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class DeviceItemAdapter internal constructor(private val DeviceItemsList: MutableList<DeviceItems>) :
    Adapter<DeviceItemAdapter.DeviceItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return DeviceItemHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceItemHolder, position: Int) {
        val item = DeviceItemsList[position]
        holder.deviceName.text = item.deviceName
        holder.deviceAddress.text = item.deviceAddress

        holder.itemLayout.setOnClickListener {
            if (position.odd) holder.itemView.findNavController()
                .navigate(R.id.action_DeviceList_to_TankLevel)
            else holder.itemView.findNavController()
                .navigate(R.id.action_DeviceList_to_Battery)
        }
    }

    override fun getItemCount(): Int {
        return DeviceItemsList.size
    }
    class DeviceItems internal constructor(var deviceName: String, var deviceAddress: String)

    inner class DeviceItemHolder(itemView: View) : ViewHolder(itemView) {
        var deviceName: TextView = itemView.findViewById(R.id.deviceName)
        var deviceAddress: TextView = itemView.findViewById(R.id.deviceAddress)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.itemLayout)
    }
}
*/
