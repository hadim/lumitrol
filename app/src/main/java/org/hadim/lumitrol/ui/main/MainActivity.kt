package org.hadim.lumitrol.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import com.google.android.material.navigation.NavigationView
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseActivity
import org.hadim.lumitrol.model.Repository


class MainActivity : BaseActivity<MainActivityViewModel>() {

    companion object {
        const val TAG: String = "MainActivity"
    }

    override val viewModelClass: Class<MainActivityViewModel> = MainActivityViewModel::class.java
    override val layoutId: Int = R.layout.activity_main

    private lateinit var repository: Repository

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.nav_view)
    lateinit var navView: NavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start the repository instance (singleton)
        repository = Repository.getRepository(application)

        if (savedInstanceState == null) {

            // Setup toolbar
            setSupportActionBar(toolbar)

            // Populate navigation menu
            val topLevelDestinationIds = setOf(
                R.id.nav_connect,
                R.id.nav_control,
                R.id.nav_gallery
            )
            appBarConfiguration = AppBarConfiguration(topLevelDestinationIds, drawerLayout)

            // Setup navigation
            val navController = findNavController(this, R.id.nav_host_fragment)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            Log.d("$TAG/onCreate", "Init MainActivity")
        }

        enableNetworkOnWifi()
        registerWifiChangeCallback()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun registerWifiChangeCallback() {

        class WifiBroadcastReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                repository.checkAlive()
            }
        }

        val filter = IntentFilter()
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        filter.addAction("android.net.wifi.STATE_CHANGE")
        application.registerReceiver(WifiBroadcastReceiver(), filter)
    }

    private fun enableNetworkOnWifi() {
        val mConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        mConnectivityManager.requestNetwork(request.build(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                try {
                    mConnectivityManager.bindProcessToNetwork(network)
                } catch (e: Exception) {
                    Log.e("MainActivity/forceNetworkOnWifi", e.message as String)
                }

            }
        })
    }
}
