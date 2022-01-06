package com.verges.smartsensors

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.verges.smartsensors.databinding.ActivityMainBinding


// const val REQUEST_PERMISSION_LOCATION = 0

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
//    private lateinit var view: View

    /*
    fun checkPermissionAndForward() {
        if (checkSelfPermissionCompat(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermissionCompat(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            layout.showSnackbar(R.string.permission_denied, Snackbar.LENGTH_SHORT)
            if (shouldShowRequestPermissionRationaleCompat(ACCESS_FINE_LOCATION)) {
                layout.showSnackbar(
                    R.string.permission_required,
                    Snackbar.LENGTH_INDEFINITE, R.string.ok
                ) {
                    requestPermissionsCompat(
                        arrayOf(
                            BLUETOOTH,
                            BLUETOOTH_ADMIN,
                            ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION
                        ),
                        REQUEST_PERMISSION_LOCATION
                    )
                }
            } else {
                // Request the permission. The result will be received in onRequestPermissionResult().
                requestPermissionsCompat(arrayOf(ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
            }
        } else {
            layout.showSnackbar(R.string.permission_granted, Snackbar.LENGTH_SHORT)
            startActivity(Intent(this, DeviceScanActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                layout.showSnackbar(R.string.permission_granted, Snackbar.LENGTH_SHORT)
                val intent = Intent(this, DeviceScanActivity::class.java)
                startActivity(intent)
            } else {
                // Permission request was denied.
                layout.showSnackbar(R.string.permission_denied, Snackbar.LENGTH_SHORT)
                layout.findNavController()
                    .navigate(R.id.action_EnableBluetoothFragment_to_LocationRequiredFragment)
            }
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.mainActivityLayout)
        setSupportActionBar(binding.toolbar)

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        view = findViewById(R.id.bluetoothError)

        //bluetoothEnabled()
/*
        findViewById<Button>(R.id.enable_bluetooth_button).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    permissions.entries.forEach {
                        Log.d("SMARTSENSOR", "${it.key} = ${it.value}")
                    }
                }.launch(
                    arrayOf(
                        BLUETOOTH_SCAN,
                        BLUETOOTH_CONNECT
                    )
                )
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBt.launch(enableBtIntent)
            }
        }
        */
    }
/*
    private fun bluetoothEnabled() {
        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        if (!adapter.isEnabled) {
            // prompt the user to enable bluetooth
            Snackbar.make(view, R.string.permission_denied, Snackbar.LENGTH_LONG).show()
        } else {
            view.findNavController()
                .navigate(EnableBluetoothFragmentDirections.actionEnableBluetoothFragmentToLocationRequiredFragment())
        }
    }

    private var requestBt = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Snackbar.make(view, R.string.permission_granted, Snackbar.LENGTH_LONG).show()
            view.findNavController()
                .navigate(EnableBluetoothFragmentDirections.actionEnableBluetoothFragmentToLocationRequiredFragment())
        } else {
            Snackbar.make(view, R.string.permission_denied, Snackbar.LENGTH_LONG).show()
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
*/
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
