package org.hadim.lumitrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.hadim.lumitrol.model.CameraStateModel


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val cameraStateModel: CameraStateModel by viewModels { SavedStateViewModelFactory(this.application, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_control, R.id.nav_gallery), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        registerWifiChangeCallback()
        checkWifi()

        cameraStateModel.isWifiEnabled.observe(this, Observer { isWifiEnabled ->
            if (!isWifiEnabled) {
                cameraStateModel.ipAddress.value = "None"
            }
        })

        cameraStateModel.ipAddress.observe(this, Observer { ipAddress ->
            if (ipAddress == "None") {
                cameraStateModel.isIReachable.value = false
            }
        })

        cameraStateModel.isIReachable.observe(this, Observer { isIReachable ->
            if (!isIReachable) {
                cameraStateModel.isCameraDetected.value = false
            }
        })

        cameraStateModel.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected) {
                // TODO: update cameraStateModel
                cameraStateModel.checkAlive(null)
            } else {
                cameraStateModel.cancelCheckAlive()
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun checkWifi() {
        // Check Wi-Fi is enabled.
        val wifi = applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifi != null) {
            cameraStateModel.isWifiEnabled.value = wifi.isWifiEnabled
            enableNetworkOnWifi()
        } else {
            Log.e("MAIN/checkWifi", "Error while checking Wi-Fi is enabled.")
            cameraStateModel.isWifiEnabled.value = false
        }
    }

    private fun enableNetworkOnWifi() {
        val mConnectivityManager = applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        mConnectivityManager.requestNetwork(request.build(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                try {
                    mConnectivityManager.bindProcessToNetwork(network)
                } catch (e: Exception) {
                    Log.e("MAIN/forceNetworkOnWifi", e.message)
                }

            }
        })
    }

    private fun registerWifiChangeCallback() {

        class WifiBroadcastReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                checkWifi()
            }
        }

        val filter = IntentFilter()
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        filter.addAction("android.net.wifi.STATE_CHANGE")
        registerReceiver(WifiBroadcastReceiver(), filter)
    }
}
