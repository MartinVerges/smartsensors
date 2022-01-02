package com.verges.smartsensors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class DeviceItemAdapter internal constructor(private val DeviceItemsList: MutableList<DeviceItems>) :
    Adapter<DeviceItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = DeviceItemsList[position]
        holder.name.text = item.name
        holder.mac.text = item.mac

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

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.deviceName)
        var mac: TextView = itemView.findViewById(R.id.deviceMac)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.itemLayout)
    }
}
