package org.hadim.lumitrol.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.common.net.InetAddresses
import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_home, container, false)

        // Enable button for automatic connection
        val connectButton: Button = root.findViewById(org.hadim.lumitrol.R.id.connect_button) as Button
        connectButton.setOnClickListener(View.OnClickListener {

            // TODO: Here we do the discovery in the UI thread for testing purpose.
            //  Working implementation should be moved to a AsyncTask object (in SSDPDiscovery).

            val serviceName = "urn:schemas-upnp-org:service:ContentDirectory:1"

            val client = SsdpClient.create()
            val all = SsdpRequest.discoverAll()
            client.discoverServices(all, object : DiscoveryListener {
                override fun onServiceDiscovered(service: SsdpService) {
                    Log.d("HOME", "Found service: $service")
                }
                override fun onServiceAnnouncement(announcement: SsdpServiceAnnouncement) {
                    Log.d("HOME", "Service announced something: $announcement")
                }
                override fun onFailed(ex: Exception?) {
                    Log.d("HOME", "FAILEDDDDD")
                }
            })
        })

        // Validate Manual IP address

        val ipAddressField: EditText = root.findViewById(org.hadim.lumitrol.R.id.ip_address_field) as EditText
        val defaultIPAddressTextColor = ipAddressField.textColors.defaultColor
        val errorManualIPTextView = root.findViewById(org.hadim.lumitrol.R.id.errorManualIPText) as TextView

        ipAddressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    var address = InetAddresses.forString(s.toString())
                    Log.d("HOME", address.toString())
                    ipAddressField.setTextColor(defaultIPAddressTextColor)
                    errorManualIPTextView.text = ""
                    errorManualIPTextView.visibility = View.GONE
                } catch (e: Exception) {
                    Log.d("HOME", e.message)
                    ipAddressField.setTextColor(Color.RED)
                    errorManualIPTextView.text = e.message
                    errorManualIPTextView.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return root
    }
}