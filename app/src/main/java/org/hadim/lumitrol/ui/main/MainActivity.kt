package org.hadim.lumitrol.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import com.google.android.material.navigation.NavigationView
import org.hadim.lumitrol.base.BaseActivity


class MainActivity : BaseActivity() {

    companion object {
        const val TAG: String = "MainActivity"
    }

    private val mainActivityViewModel: MainActivityViewModel
            by viewModels { SavedStateViewModelFactory(this.application, this) }

    @BindView(org.hadim.lumitrol.R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(org.hadim.lumitrol.R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(org.hadim.lumitrol.R.id.nav_view)
    lateinit var navView: NavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun layoutRes(): Int {
        return org.hadim.lumitrol.R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {

            // Setup toolbar
            setSupportActionBar(toolbar)

            // Populate navigation menu
            val topLevelDestinationIds = setOf(
                org.hadim.lumitrol.R.id.nav_connect,
                org.hadim.lumitrol.R.id.nav_control,
                org.hadim.lumitrol.R.id.nav_gallery
            )
            appBarConfiguration = AppBarConfiguration(topLevelDestinationIds, drawerLayout)

            // Setup navigation
            val navController = findNavController(this, org.hadim.lumitrol.R.id.nav_host_fragment)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            Log.d("$TAG/onCreate", "Init MainActivity")

            mainActivityViewModel.netWorkError.observe(this, Observer { netWorkError ->
                Log.d(TAG, netWorkError)
            })

            mainActivityViewModel.netWorkFailure.observe(this, Observer { netWorkFailure ->
                Log.d(TAG, netWorkFailure)
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, org.hadim.lumitrol.R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
