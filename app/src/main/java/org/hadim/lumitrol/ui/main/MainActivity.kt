package org.hadim.lumitrol.ui.main

import android.os.Bundle
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


class MainActivity : BaseActivity() {

//    @Inject
//    lateinit var testViewModel: ConnectViewModel

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.nav_view)
    lateinit var navView: NavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Log.e("+============", testViewModel.toString())

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
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
