package org.hadim.lumitrol.network

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import be.teletask.onvif.DiscoveryManager
import be.teletask.onvif.DiscoveryMode
import be.teletask.onvif.listeners.DiscoveryListener
import be.teletask.onvif.models.Device
import be.teletask.onvif.models.UPnPDevice
import be.teletask.onvif.upnp.UPnPDeviceInformation
import be.teletask.onvif.upnp.UPnPDeviceInformationListener
import be.teletask.onvif.upnp.UPnPManager
import org.hadim.lumitrol.ui.connect.ConnectViewModel


class UPnPDiscovery(private val context: Context) {

    companion object {
        const val TAG: String = "UPnPDiscovery"
        const val DISCOVERY_TIMEOUT: Int = 2000  // ms
    }

    var multicastLock: WifiManager.MulticastLock? = null

    val manager = DiscoveryManager()
    val uPnPManager = UPnPManager()
    var runDiscover: Boolean = false

    fun lockMulticast() {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        wifiManager?.let {
            multicastLock = wifiManager.createMulticastLock("multicastLock")
            multicastLock?.let { multicastLock ->
                multicastLock.setReferenceCounted(true)
                multicastLock.acquire()
            }
            Log.d("${ConnectViewModel.TAG}/lockMulticast", "Lock multicast.")
        }
    }

    fun unlockMulticast() {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        wifiManager?.let {
            multicastLock?.release()
            multicastLock = null
            Log.d("${ConnectViewModel.TAG}/unlockMulticast", "Unlock multicast.")
        }
    }

    fun discover(
        onSuccess: ((device: UPnPDevice) -> Unit?)? = null,
        onError: ((onvifDevice: UPnPDevice, errorCode: Int, errorMessage: String) -> Unit?)? = null
    ) {

        lockMulticast()
        runDiscover = true

        var onceOnEnd: (() -> Unit)? = null
        onceOnEnd = { if (runDiscover) discoverOnce(onSuccess, onError, onceOnEnd) }

        discoverOnce(onSuccess, onError, onceOnEnd)
    }

    private fun discoverOnce(
        onSuccess: ((device: UPnPDevice) -> Unit?)? = null,
        onError: ((onvifDevice: UPnPDevice, errorCode: Int, errorMessage: String) -> Unit?)? = null,
        onEnd: (() -> Unit?)? = null
    ) {

        val manager = DiscoveryManager()
        manager.discoveryTimeout = DISCOVERY_TIMEOUT

        manager.discover(DiscoveryMode.UPNP, object : DiscoveryListener {
            override fun onDiscoveryStarted() {
                Log.d("$TAG/onDiscoveryStarted", "Discovery started")
            }

            override fun onDevicesFound(devices: List<Device>) {
                val uPnPDevices: List<UPnPDevice> = devices.map { device -> device as UPnPDevice }
                for (device in uPnPDevices) {

                    uPnPManager.getDeviceInformation(device, object : UPnPDeviceInformationListener {
                        override fun onDeviceInformationReceived(device: UPnPDevice, information: UPnPDeviceInformation) {
                            onSuccess?.let { onSuccess(device) }
                        }

                        override fun onError(onvifDevice: UPnPDevice, errorCode: Int, errorMessage: String) {
                            Log.e("$TAG/onDeviceInformationReceived", "Error: $errorMessage")
                            onError?.let { onError(onvifDevice, errorCode, errorMessage) }
                        }
                    })
                }
                onEnd?.let { onEnd() }
            }
        })
    }

    fun discoverManufacturer(
        manufacturer: String,
        onSuccess: (device: UPnPDevice) -> Boolean
    ) {
        discover({ device ->
            if (device.deviceInformation.manufacturer == manufacturer) {
                if (runDiscover) onSuccess(device)
            }
        })
    }

}