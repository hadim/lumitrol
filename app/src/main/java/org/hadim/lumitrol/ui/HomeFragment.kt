package org.hadim.lumitrol.ui

import android.content.Context
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.common.net.InetAddresses
import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement
import org.hadim.lumitrol.model.CameraStateModel
import org.hadim.lumitrol.network.CameraRequest
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class HomeFragment : Fragment() {

    private val cameraStateModel: CameraStateModel by activityViewModels()

    private lateinit var connectionStatusIPTextView: TextView
    private lateinit var connectionStatusIPIcon: ImageView
    private lateinit var connectionStatusIPProgressBar: ProgressBar

    private lateinit var connectionStatusCameraTextView: TextView
    private lateinit var connectionStatusCameraIcon: ImageView
    private lateinit var connectionStatusCameraProgressBar: ProgressBar

    private lateinit var connectionStatusModelNameTextView: TextView

    private var defaultIPAddressTextColor: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraStateModel.ipAddress.observe(this, Observer { newIPAddress ->
            connectionStatusIPTextView.setText(newIPAddress)

            connectionStatusIPProgressBar.visibility = View.VISIBLE
            connectionStatusIPIcon.visibility = View.GONE

            cameraStateModel.isIReachable.value = false
            cameraStateModel.isCameraDetected.value = false

            doAsync {

                var isReachable = InetAddresses.forString(newIPAddress).isReachable(2000)
                cameraStateModel.isIReachable.postValue(isReachable)

                uiThread {
                    connectionStatusIPProgressBar.visibility = View.GONE
                    connectionStatusIPIcon.visibility = View.VISIBLE
                }
            }

        })

        cameraStateModel.isIReachable.observe(this, Observer { isReachable ->

            cameraStateModel.isCameraDetected.value = false
            cameraStateModel.modelName.value = "None"

            if (isReachable) {
                connectionStatusIPIcon.setImageResource(android.R.drawable.presence_online)

                connectionStatusCameraProgressBar.visibility = View.VISIBLE
                connectionStatusCameraIcon.visibility = View.GONE


                val ipAddress = cameraStateModel.ipAddress.value as String
                cameraStateModel.cameraRequest = CameraRequest(ipAddress, this.context as Context)

                cameraStateModel.cameraRequest.getInfo(
                    onSuccess = { response: String ->

                        val isValid = cameraStateModel.parseInfo((response))


                        if (isValid == true) {

                            Log.i("HOME/cameraDetection", "Connection with the camera established.")
                            cameraStateModel.isCameraDetected.postValue(true)
                            connectionStatusCameraProgressBar.visibility = View.GONE
                            connectionStatusCameraIcon.visibility = View.VISIBLE


                        } else {

                            Log.e("HOME/cameraDetection", "Can't parse the camera response.")
                            Log.e("HOME/cameraDetection", "Response:")
                            Log.e("HOME/cameraDetection", response)

                            cameraStateModel.isCameraDetected.postValue(false)
                            connectionStatusCameraProgressBar.visibility = View.GONE
                            connectionStatusCameraIcon.visibility = View.VISIBLE
                        }
                    },
                    onFailure = { t: Throwable? ->
                        Log.e("HOME/cameraDetection", "Camera detection failed.")

                        if (t != null && t.message != null) {
                            Log.e("HOME/cameraDetection", t.message as String)
                        }

                        cameraStateModel.isCameraDetected.postValue(false)
                        connectionStatusCameraProgressBar.visibility = View.GONE
                        connectionStatusCameraIcon.visibility = View.VISIBLE
                    })


            } else {
                connectionStatusIPIcon.setImageResource(android.R.drawable.presence_busy)
            }
        })

        cameraStateModel.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected) {
                connectionStatusCameraIcon.setImageResource(android.R.drawable.presence_online)
                connectionStatusCameraTextView.text = "OK"

            } else {
                connectionStatusCameraIcon.setImageResource(android.R.drawable.presence_busy)
                connectionStatusCameraTextView.text = "None"
            }
        })

        cameraStateModel.modelName.observe(this, Observer { modelName ->
            connectionStatusModelNameTextView.text = modelName
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_home, container, false)

        // Set some class attributes.
        connectionStatusIPTextView = root.findViewById(org.hadim.lumitrol.R.id.connection_status_ip_text) as TextView
        connectionStatusIPIcon = root.findViewById(org.hadim.lumitrol.R.id.connection_status_ip_icon) as ImageView
        connectionStatusIPProgressBar = root.findViewById(org.hadim.lumitrol.R.id.connection_status_ip_progress_bar) as ProgressBar

        connectionStatusCameraTextView = root.findViewById(org.hadim.lumitrol.R.id.connection_status_camera_text) as TextView
        connectionStatusCameraIcon = root.findViewById(org.hadim.lumitrol.R.id.connection_status_camera_icon) as ImageView
        connectionStatusCameraProgressBar =
            root.findViewById(org.hadim.lumitrol.R.id.connection_status_camera_progress_bar) as ProgressBar

        connectionStatusModelNameTextView = root.findViewById(org.hadim.lumitrol.R.id.connection_status_model_name_text) as TextView

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

        ipAddressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateManualIPAddress(root)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Restore manual IP
        if (cameraStateModel.ipAddress.value != null && cameraStateModel.isIPManual.value!!) {
            ipAddressField.setText(cameraStateModel.ipAddress.value)
        }

        // Enable button for manual connection
        val manuaConnectButton: Button = root.findViewById(org.hadim.lumitrol.R.id.manual_connect_button) as Button
        manuaConnectButton.setOnClickListener(View.OnClickListener {
            var address: String? = validateManualIPAddress(root)
            if (address != null) {
                cameraStateModel.ipAddress.value = address
                cameraStateModel.isIPManual.value = true
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