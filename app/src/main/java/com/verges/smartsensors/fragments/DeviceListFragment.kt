package com.verges.smartsensors.fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.DeviceItemAdapter
import com.verges.smartsensors.MainActivity
import com.verges.smartsensors.R
import com.verges.smartsensors.databinding.FragmentDeviceListBinding

class DeviceListFragment : Fragment() {
    private val TAG: String = this::class.java.simpleName

    private var _binding: FragmentDeviceListBinding? = null
    private val binding get() = _binding!!

    lateinit var mView: View

    private lateinit var bleManager: BluetoothManager
    private lateinit var bleAdapter: BluetoothAdapter
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var mHandler: Handler

    private val scanDelay: Long = 500
    private val scanPeriod: Long = 30000
    private var isScanning = false

    private var itemsList: MutableList<DeviceItemAdapter.DeviceItems> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)

        with(binding.deviceListView) {
            adapter = DeviceItemAdapter(itemsList)
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_action_rescan).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_rescan -> {
                scanForBleDevices(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view
        mHandler = Handler(Looper.getMainLooper())
        bleManager = view.context
            .getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter
        bleScanner = bleAdapter.bluetoothLeScanner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        scanForBleDevices(false)
    }

    override fun onResume() {
        super.onResume()
        if (!bleAdapter.isEnabled) startActivity(Intent(this.context, MainActivity::class.java))
        scanForBleDevices(true)
    }

    private val stopScanCallback = Runnable { scanForBleDevices(false) }
    private val startScanCallback = Runnable {
        Snackbar.make(mView.findViewById(R.id.deviceListView),
            R.string.info_start_scanning, Snackbar.LENGTH_LONG).show()
        isScanning = false
        val scanSetting = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(2000)
            .build()
        val scanFilter: MutableList<ScanFilter> = mutableListOf()
        scanFilter.add(ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString("00002af9-0000-1000-8000-00805f9b34fb")).build())
        //scanFilter.add(ScanFilter.Builder().setDeviceName("xxxx").build())
        bleScanner.startScan(scanFilter, scanSetting, bleScanCallback)
    }
    private fun scanForBleDevices(enable: Boolean) {
        if (enable) {
            isScanning = true
            bleScanner.stopScan(bleScanCallback)   // if some scan is running, we stop it and start a new one after a short delay.
            mHandler.postDelayed(startScanCallback, scanDelay)
            mHandler.postDelayed(stopScanCallback, scanPeriod)
        } else {
            isScanning = false
            mHandler.removeCallbacks(startScanCallback)
            mHandler.removeCallbacks(stopScanCallback)
            bleScanner.stopScan(bleScanCallback)
            Snackbar.make(mView.findViewById(R.id.deviceListView),
                R.string.info_stop_scanning, Snackbar.LENGTH_LONG).show()
        }
    }

    private val bleScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Snackbar.make(mView.findViewById(R.id.deviceListView),
                R.string.error_scanning_failed, Snackbar.LENGTH_LONG).show()
        }
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            Log.i(TAG, "Batch scan results: ${results.size}")
            results.forEach { onScanResult(0, it) }
        }
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(TAG, "onScanResult(${callbackType}): ${result.device.address} - ${result.device.name}")
            // C8:C9:A3:C5:DE:DE - tanksensor
            // ScanResult{device=C8:C9:A3:C5:DE:DE, scanRecord=ScanRecord
            // [mAdvertiseFlags=6,
            // mServiceUuids=[00002af9-0000-1000-8000-00805f9b34fb],
            // mServiceSolicitationUuids=[],
            // mManufacturerSpecificData={},
            // mServiceData={},
            // mTxPowerLevel=-2147483648,
            // mDeviceName=tanksensor],
            // rssi=-79,
            // timestampNanos=189306463459326,
            // eventType=17,
            // primaryPhy=1,
            // secondaryPhy=0,
            // advertisingSid=255,
            // txPower=127,
            // periodicAdvertisingInterval=0}

            if (result.device.name != null && result.device.address != null) {
                if (itemsList.filter { it.deviceAddress == result.device.address }.isNullOrEmpty()) {
                    val item =
                        DeviceItemAdapter.DeviceItems(result.device.name, result.device.address)
                    itemsList.add(item)
                    binding.deviceListView.adapter?.notifyItemInserted(itemsList.size - 1)
                }
            }
        }
    }
}

