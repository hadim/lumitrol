package org.hadim.lumitrol.upnp

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import org.fourthline.cling.android.AndroidUpnpService
import org.fourthline.cling.android.FixedAndroidLogHandler
import org.fourthline.cling.model.meta.Device
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.registry.DefaultRegistryListener
import org.fourthline.cling.registry.Registry
import java.util.logging.Level
import java.util.logging.Logger


class UpnpManager(var application: Application) : ServiceConnection, DefaultRegistryListener() {

    companion object {
        const val TAG: String = "UpnpManager"
    }

    private var context: Context = application.applicationContext

    var upnpService: AndroidUpnpService? = null
    var detectedDevice: Device<*, *, *>? = null
    var onDeviceAddedCallback: ((device: RemoteDevice) -> Unit)? = null

    fun startUpnpService() {
        // Fix the logging integration between java.util.logging and Android internal logging
        org.seamless.util.logging.LoggingUtil.resetRootHandler(
            FixedAndroidLogHandler()
        )
        // Now you can enable logging as needed for various categories of Cling:
        Logger.getLogger("org.fourthline.cling").level = Level.WARNING

        // Start the uPnP Android service to discover camera.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, CameraUpnpService::class.java))
        } else {
            context.startService(Intent(context, CameraUpnpService::class.java))
        }

        context.bindService(
            Intent(context, CameraUpnpService::class.java),
            this, Context.BIND_AUTO_CREATE
        )
    }

    fun destroy() {
        upnpService?.registry?.removeListener(this);
        context.stopService(Intent(context, CameraUpnpService::class.java))
    }

    override fun onServiceConnected(className: ComponentName, service: IBinder) {

        upnpService = service as AndroidUpnpService

        Log.d("$TAG/onServiceConnected", upnpService.toString())

        // Get ready for future device advertisements
        upnpService?.registry?.addListener(this)

        // Search asynchronously for all devices, they will respond soon
        upnpService?.controlPoint?.search()
    }

    override fun onServiceDisconnected(className: ComponentName) {
        Log.d("$TAG/onServiceDisconnected", className.toString())
        upnpService = null
    }

    override fun remoteDeviceDiscoveryStarted(registry: Registry, device: RemoteDevice) {
    }

    override fun remoteDeviceDiscoveryFailed(registry: Registry, device: RemoteDevice, ex: Exception?) {
        Log.e("$TAG/remoteDeviceDiscoveryFailed", device.displayString)
        Log.e("$TAG/remoteDeviceDiscoveryFailed", ex.toString())
    }

    override fun remoteDeviceAdded(registry: Registry, device: RemoteDevice) {
        if (detectedDevice == null) {
            // Connect to the first detected device that has
            // "Panasonic" as a manufacturer.
            if (device.details.manufacturerDetails.manufacturer == "Panasonic") {
                Log.i("$TAG/deviceAdded", device.details.manufacturerDetails.manufacturer)
                Log.i("$TAG/deviceAdded", device.details.modelDetails.modelName)
                Log.i("$TAG/deviceAdded", device.details.modelDetails.modelNumber)
                Log.i("$TAG/deviceAdded", device.type.toString())
                Log.i("$TAG/deviceAdded", device.identity?.udn?.identifierString)
                Log.i("$TAG/deviceAdded", device.identity.descriptorURL.toString())
                Log.i("$TAG/deviceAdded", device.identity.descriptorURL.host)
                detectedDevice = device
                onDeviceAddedCallback?.let {
                    it(device)
                }
            }
        }
    }

    override fun remoteDeviceRemoved(registry: Registry, device: RemoteDevice) {
        Log.i("$TAG/deviceRemoved", device.toString())
    }

    fun onDeviceAdded(onDeviceAddedCallback: (device: RemoteDevice) -> Unit) {
        this.onDeviceAddedCallback = onDeviceAddedCallback
    }

}