package org.hadim.lumitrol.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.common.net.InetAddresses
import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement
import org.hadim.lumitrol.model.CameraStateModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future

class HomeFragment : Fragment() {

    private lateinit var cameraStateModel: CameraStateModel

    private lateinit var connectionStatusIPTextView: TextView
    private lateinit var connectionStatusIPIcon: ImageView
    private lateinit var connectionStatusProgressBar: ProgressBar

    private var defaultIPAddressTextColor: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val factory = SavedStateViewModelFactory(this.context)
//        cameraStateModel = ViewModelProviders.of(this, factory).get(CameraStateModel::class.java)

        cameraStateModel = ViewModelProvider(this).get(CameraStateModel::class.java)

        val ipAddressObserver = Observer<String> { newIPAddress ->
            if (connectionStatusIPTextView != null) {
                connectionStatusIPTextView.setText(newIPAddress)
            }

            if (connectionStatusIPIcon != null) {

                connectionStatusIPIcon.setImageResource(android.R.drawable.presence_busy)
                connectionStatusProgressBar.visibility = View.VISIBLE
                connectionStatusIPIcon.visibility = View.GONE

                doAsync {

                    var isReachable = InetAddresses.forString(newIPAddress).isReachable(2000)

                    uiThread {
                        connectionStatusProgressBar.visibility = View.GONE
                        connectionStatusIPIcon.visibility = View.VISIBLE

                        if (isReachable) {
                            connectionStatusIPIcon.setImageResource(android.R.drawable.presence_online)
                        } else {
                            connectionStatusIPIcon.setImageResource(android.R.drawable.presence_busy)
                        }
                    }
                }

            }

        }
        cameraStateModel.ipAddress.observe(this, ipAddressObserver)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_home, container, false)

        // Set some class attributes.
        connectionStatusIPTextView = root.findViewById(org.hadim.lumitrol.R.id.connection_status_ip_text) as TextView

        // Enable button for automatic connection
        val connectButton: Button = root.findViewById(org.hadim.lumitrol.R.id.connect_button) as Button
        connectButton.setOnClickListener(View.OnClickListener {

            // TODO: Here we do the discovery in the UI thread for testing purpose.
            // Working implementation should be moved to a AsyncTask object (in SSDPDiscovery).

            // val serviceName = "urn:schemas-upnp-org:service:ContentDirectory:1"

            val client = SsdpClient.create()
            val all = SsdpRequest.discoverAll()
            client.discoverServices(all, object : DiscoveryListener {
                override fun onServiceDiscovered(service: SsdpService) {
                    Log.d("HomeFragment/connectButton", "Found service: $service")
                }

                override fun onServiceAnnouncement(announcement: SsdpServiceAnnouncement) {
                    Log.d("HomeFragment/connectButton", "Service announced something: $announcement")
                }

                override fun onFailed(ex: Exception?) {
                    Log.d("HomeFragment/connectButton", "FAILEDDDDD")
                }
            })
        })

        // Validate Manual IP address
        val ipAddressField: EditText = root.findViewById(org.hadim.lumitrol.R.id.ip_address_field) as EditText
        defaultIPAddressTextColor = ipAddressField.textColors.defaultColor

        connectionStatusIPIcon = root.findViewById(org.hadim.lumitrol.R.id.connection_status_ip_icon) as ImageView
        connectionStatusProgressBar = root.findViewById(org.hadim.lumitrol.R.id.connection_status_progress_bar) as ProgressBar

        ipAddressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateManualIPAddress(root)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Enable button for manual connection
        val manuaConnectButton: Button = root.findViewById(org.hadim.lumitrol.R.id.manual_connect_button) as Button
        manuaConnectButton.setOnClickListener(View.OnClickListener {
            var address: String? = validateManualIPAddress(root)
            if (address != null) {
                cameraStateModel.ipAddress.value = address
            }
        })

        return root
    }

    fun validateManualIPAddress(root: View): String? {

        val ipAddressField: EditText = root.findViewById(org.hadim.lumitrol.R.id.ip_address_field) as EditText
        val errorManualIPTextView = root.findViewById(org.hadim.lumitrol.R.id.errorManualIPText) as TextView
        val manuaConnectButton: Button = root.findViewById(org.hadim.lumitrol.R.id.manual_connect_button) as Button

        try {
            var address = InetAddresses.forString(ipAddressField.text.toString()).hostAddress
            ipAddressField.setTextColor(defaultIPAddressTextColor)
            errorManualIPTextView.text = ""
            errorManualIPTextView.visibility = View.GONE
            manuaConnectButton.isEnabled = true
            return address

        } catch (e: Exception) {
            ipAddressField.setTextColor(Color.RED)
            errorManualIPTextView.text = e.message
            errorManualIPTextView.visibility = View.VISIBLE
            manuaConnectButton.isEnabled = false
            return null
        }
    }
}