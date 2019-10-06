package org.hadim.lumitrol.ui.connect

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import org.hadim.lumitrol.base.BaseViewModel
import org.hadim.lumitrol.network.UPnPDiscovery
import java.net.InetAddress
import java.net.URL


class ConnectViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val TAG: String = "ConnectViewModel"
    }

    private var discovery: UPnPDiscovery? = null

    init {
        Log.d("$TAG/init", "Init ConnectViewModel")
    }

    fun manualConnection(ipAddress: String) {
        Log.d("$TAG/manualConnection", "clicked")
        stopAutomaticConnection()
        repository.isIpManual.value = true
        repository.resetIpAddress()
        repository.ipAddress.value = ipAddress
        repository.buildApiService(force = true)
        repository.checkAlive()
    }

    fun automaticConnection() {
        Log.d("$TAG/automaticConnection", "clicked")

        repository.resetIpAddress()
        stopAutomaticConnection()

        discovery = UPnPDiscovery(context)
        discovery?.let { discovery ->
            discovery.discoverManufacturer("Panasonic") { device ->
                Log.d("$TAG/automaticConnection", "Devices found: ${device.hostName}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.location}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.header}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.server}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.usn}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.deviceInformation.friendlyName}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.deviceInformation.manufacturer}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.deviceInformation.modelDescription}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.deviceInformation.modelName}")
                Log.d("$TAG/automaticConnection", "Devices found: ${device.deviceInformation.deviceType}")

                stopAutomaticConnection()

                val address = InetAddress.getByName(URL(device.hostName).host).hostAddress

                // Run on the main thread
                Handler(context.mainLooper).post {
                    repository.ipAddress.value = address.toString()
                    repository.buildApiService(force = true)
                    repository.checkAlive()
                }

                true
            }
        }
    }

    private fun stopAutomaticConnection() {
        discovery?.runDiscover = false
        discovery?.unlockMulticast()
        discovery = null
    }

}