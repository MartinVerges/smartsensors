package com.verges.smartsensors.fragments

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.*
import com.verges.smartsensors.BleGattAttributes.CHARACTERISTIC_ENV_SENSING
import com.verges.smartsensors.BleGattAttributes.CHARACTERISTIC_GENERIC_LEVEL
import com.verges.smartsensors.databinding.FragmentTankLevelBinding


class DeviceTankLevelFragment : Fragment() {
    private val mTAG: String = this::class.java.simpleName

    private var _binding: FragmentTankLevelBinding? = null
    private val binding get() = _binding!!

    lateinit var mView: View

    private val args: DeviceTankLevelFragmentArgs by navArgs()

    private var bluetoothService : BluetoothLeService? = null
    private var connected = false

    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // don't forget to add it to makeGattUpdateIntentFilter
            // otherwise this action won't get called here
            Log.d(mTAG, "gattUpdateReceiver ${intent.action.toString()}")

            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    connected = true
                    Snackbar.make(mView, R.string.connected, Snackbar.LENGTH_LONG).show()
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    connected = false
                    Snackbar.make(mView, R.string.disconnected, Snackbar.LENGTH_LONG).show()
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    Log.d(mTAG, "ACTION_GATT_SERVICES_DISCOVERED")
                    discoverGattServices(bluetoothService?.getSupportedGattServices())
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    var level = intent.getStringExtra(BluetoothLeService.EXTRA_DATA)?.toInt() ?: 0
                    if (level > 100) level = 100
                    if (level < 0) level = 0
                    Log.e(mTAG, "Action data received with current level = $level")

                    binding.waveLoadingView.progressValue = level
                    binding.tanklevelValue.text = getString(R.string.tank_level_value, level)
                }
            }
        }
    }

    private fun discoverGattServices(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        // Found UUID = 00002af9-0000-1000-8000-00805f9b34fb
        // Found Characteristic = 0000181a-0000-1000-8000-00805f9b34fb

        val gattService = gattServices.find { it.uuid == CHARACTERISTIC_GENERIC_LEVEL }
        if (gattService != null) {
            Log.i(mTAG, "Found Service UUID = ${gattService.uuid}")
            val gattCharacteristic = gattService.characteristics.find { it.uuid == CHARACTERISTIC_ENV_SENSING }
            if (gattCharacteristic != null) {
                Log.d(mTAG, "Found Characteristic = ${gattCharacteristic.uuid}")
                Log.d(mTAG, "gattCharacteristic.properties = ${gattCharacteristic.properties}")

                if ((gattCharacteristic.properties or BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                    bluetoothService?.setCharacteristicNotification(gattCharacteristic, true)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(gattUpdateReceiver)
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
            addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        if (bluetoothService != null) {
            val result = bluetoothService!!.connect(args.deviceAddress)
            Log.d(mTAG, "Connect request result=$result")
        }
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            Log.d(mTAG, "onServiceConnected($componentName, $service)")

            bluetoothService = (service as BluetoothLeService.LocalBinder).getService()
            bluetoothService?.let { bluetooth ->
                if (!bluetooth.initialize()) {
                    Log.e(mTAG, "Unable to initialize Bluetooth")
                }
                // perform device connection
                Log.d(mTAG, "serviceConnection begin connecting")
                bluetooth.connect(args.deviceAddress)
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bluetoothService = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        val gattServiceIntent = Intent(view.context, BluetoothLeService::class.java)
        activity!!.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTankLevelBinding.inflate(inflater, container, false)

        with(binding.waveLoadingView) {
            progressValue = 0
            setAnimDuration(2000)
        }

        with(binding) {
            tanklevelValue.text = getString(R.string.tank_level_value, 0)
            deviceAddressInfo.text = args.deviceAddress
            deviceNameInfo.text = args.deviceName
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
