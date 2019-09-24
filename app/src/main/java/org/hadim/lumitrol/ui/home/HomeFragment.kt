package org.hadim.lumitrol.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.widget.Button
import org.hadim.lumitrol.network.SSDPDiscovery
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement
import io.resourcepool.ssdp.model.DiscoveryRequest
import java.lang.Exception


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_home, container, false)

        val connectButton: Button =
            root.findViewById(org.hadim.lumitrol.R.id.connect_button) as Button
        connectButton.setOnClickListener(View.OnClickListener {

            val cameraDiscoverer = SSDPDiscovery()

            val serviceName = "urn:schemas-upnp-org:service:ContentDirectory:1"
            val context: Context = activity as Context

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

            Log.d("HOME", "DONE")


//            var addresses = cameraDiscoverer.execute(serviceName, context)
            //Log.d("HOME", addresses.toList().toString())
//            try {
//                Thread.sleep(1500)
//                var addresses = cameraDiscoverer.addresses
//                Log.d("HOME", addresses.toList().toString())
//
//            } catch (e: Exception) {
//                Log.d("HOME", "Discovery failed.")
//                e.printStackTrace()
//            }


        })

        var cameraIP = "192.168.54.1"

        return root
    }
}