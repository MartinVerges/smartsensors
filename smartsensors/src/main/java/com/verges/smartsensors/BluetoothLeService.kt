package com.verges.smartsensors

import android.app.Service
import android.bluetooth.*
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.verges.smartsensors.BleGattAttributes.CHARACTERISTIC_ENV_SENSING
import java.util.*

class BluetoothLeService : Service() {
    private val mTAG: String = this::class.java.simpleName

    private val binder = LocalBinder()

    private lateinit var bleManager: BluetoothManager
    private lateinit var bleAdapter: BluetoothAdapter
    private var bluetoothGatt: BluetoothGatt? = null

    private var connectionState = STATE_DISCONNECTED

    companion object {
        const val ACTION_GATT_CONNECTED = "com.verges.smartsensors.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.verges.smartsensors.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "com.verges.smartsensors.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "com.verges.smartsensors.ACTION_DATA_AVAILABLE"

        const val EXTRA_DATA = "com.verges.smartsensors.EXTRA_DATA"

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(mTAG, "calling onUnbind(${intent.toString()})")

        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(mTAG, "calling onDestroy()")

        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
        return super.onDestroy()
    }
    /*
    private fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        Log.d(mTAG, "calling readCharacteristic(${characteristic})")

        bluetoothGatt?.readCharacteristic(characteristic) ?: run {
            Log.w(mTAG, "BluetoothGatt not initialized")
            return
        }
    }*/

    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        Log.d(mTAG, "setCharacteristicNotification(${characteristic.uuid}, $enabled)")

        if (bluetoothGatt != null) {
            if (CHARACTERISTIC_ENV_SENSING == characteristic.uuid) {
                val descriptor = characteristic.getDescriptor(BleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                bluetoothGatt?.writeDescriptor(descriptor)
            }
            bluetoothGatt?.setCharacteristicNotification(characteristic, enabled)
        } else {
            Log.e(mTAG, "setCharacteristicNotification() - BluetoothGatt not initialized")
        }
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.d(mTAG, "calling onConnectionStateChange()")

            if(status == GATT_SUCCESS) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        Log.i(mTAG, "Successfully connected to the GATT Server")
                        connectionState = STATE_CONNECTED
                        broadcastUpdate(ACTION_GATT_CONNECTED)
                        bluetoothGatt?.discoverServices()
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        Log.e(mTAG, "Disconnected from the GATT Server")
                        connectionState = STATE_DISCONNECTED
                        broadcastUpdate(ACTION_GATT_DISCONNECTED)
                        gatt.close()
                        bluetoothGatt?.close()
                    }
                    else -> {
                        Log.i(mTAG, "onConnectionStateChange($status, $newState)")
                    }
                }
            } else {
                gatt.close()
                bluetoothGatt?.close()
            }
        }
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            Log.d(mTAG, "calling onServicesDiscovered()")
            if (status == GATT_SUCCESS) broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
        }
        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.d(mTAG, "calling onCharacteristicRead()")
            if (status == GATT_SUCCESS) broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
        }
        override fun onCharacteristicChanged(gatt: BluetoothGatt,characteristic: BluetoothGattCharacteristic) {
            Log.d(mTAG, "calling onCharacteristicChanged()")
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
        }
    }

    fun initialize(): Boolean {
        bleManager = applicationContext.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter

        if (!bleAdapter.isEnabled) {
            Log.e(mTAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

    private fun broadcastUpdate(action: String) = sendBroadcast(Intent(action))
    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        Log.d(mTAG, "calling broadcastUpdate($action, ${characteristic.uuid})")

        val intent = Intent(action)
        when (characteristic.uuid) {
            CHARACTERISTIC_ENV_SENSING -> {   // Tank Level
                if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
                    setCharacteristicNotification(characteristic, true)

                    intent.putExtra(EXTRA_DATA, characteristic
                        .getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)
                        .toString()
                    )
                } else {
                    Log.e(mTAG, "PROPERTY_READ not set!")
                }
                //readCharacteristic(characteristic)
            }
            else -> {
                // For all other profiles, writes the data formatted in HEX.
                val data: ByteArray? = characteristic.value
                if (data?.isNotEmpty() == true) {
                    val hexString: String = data.joinToString(separator = " ") {
                        String.format("%02X", it)
                    }
                    intent.putExtra(EXTRA_DATA, "$data\n$hexString")
                    Log.e(mTAG, "data = $hexString")
                }
            }
        }
        sendBroadcast(intent)
    }

    fun connect(address: String): Boolean {
        Log.d(mTAG, "calling connect($address)")

        return if (!bleAdapter.isEnabled) {
            Log.e(mTAG, "Unable to obtain a BluetoothAdapter.")
            false
        } else {
            try {
                val device = bleAdapter.getRemoteDevice(address)
                bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Log.e(mTAG, "Device not found with provided address.")
                return false
            }
            true
        }
    }
    fun getSupportedGattServices(): List<BluetoothGattService> {
        Log.d(mTAG, "calling getSupportedGattServices()")

        return if (bluetoothGatt == null) {
            listOf()
        } else bluetoothGatt!!.services
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService() : BluetoothLeService = this@BluetoothLeService
    }
}
