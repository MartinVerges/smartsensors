package com.verges.smartsensors

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.databinding.ActivityDeviceScanBinding


class DeviceScanActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDeviceScanBinding

    private lateinit var bleManager: BluetoothManager
    private lateinit var bleAdapter: BluetoothAdapter
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var mHandler: Handler

    private var isScanning = false

    // Stops scanning after X seconds.
    private val scanPeriod: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeviceScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_device_scan)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        mHandler = Handler(Looper.getMainLooper())
        bleManager = applicationContext
            .getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter
        bleScanner = bleAdapter.bluetoothLeScanner
    }

    override fun onResume() {
        super.onResume()

        if (!bleAdapter.isEnabled) startActivity(Intent(this, MainActivity::class.java))

        scanForBleDevices(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_device_scan)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun scanForBleDevices(enable: Boolean) {
        if (enable) {
            Snackbar.make(findViewById(R.id.deviceListView), R.string.info_start_scanning, Snackbar.LENGTH_LONG).show()

            mHandler.postDelayed({
                isScanning = false
                val scanSetting = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .setReportDelay(2000)
                    .setLegacy(false)
                    .build()
                val scanFilter: MutableList<ScanFilter> = mutableListOf()
                //scanFilter.add(ScanFilter.Builder().setDeviceName("xxxx").build())
                bleScanner.startScan(scanFilter, scanSetting, bleScanCallback)

                invalidateOptionsMenu()
            }, scanPeriod)

            isScanning = true
            bleScanner.stopScan(bleScanCallback)
            Snackbar.make(findViewById(R.id.deviceListView), R.string.info_stop_scanning, Snackbar.LENGTH_LONG).show()
        } else {
            isScanning = false
            bleScanner.stopScan(bleScanCallback)
            Snackbar.make(findViewById(R.id.deviceListView), R.string.info_stop_scanning, Snackbar.LENGTH_LONG).show()
        }
        invalidateOptionsMenu()
    }

    private val bleScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Snackbar.make(findViewById(R.id.deviceListView), R.string.error_scanning_failed, Snackbar.LENGTH_LONG).show()
        }
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            Log.i("ScanDeviceActivity", "Batch scan results: ${results.size}")
            results.forEach { onScanResult(0, it) }
        }
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.d("ScanDeviceActivity", "onScanResult(${callbackType}): ${result?.device?.address} - ${result?.device?.name}")
            // C8:C9:A3:C5:DE:DE - tanksensor
        }
    }
}
