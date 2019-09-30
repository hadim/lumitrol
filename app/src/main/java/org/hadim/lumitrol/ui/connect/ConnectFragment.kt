package org.hadim.lumitrol.ui.connect

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.OnClick
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment
import org.hadim.lumitrol.model.WifiState

class ConnectFragment : BaseFragment<ConnectViewModel>() {

    companion object {
        const val TAG: String = "ConnectFragment"
    }

    override val viewModelClass: Class<ConnectViewModel> = ConnectViewModel::class.java
    override val layoutId: Int = R.layout.fragment_connect

    @BindView(R.id.connection_status_wifi_text)
    lateinit var connectionStatusWIFITextView: TextView

    @BindView(R.id.connection_status_wifi_icon)
    lateinit var connectionStatusWIFIIcon: ImageView

    @BindView(R.id.connection_status_ip_text)
    lateinit var connectionStatusIPTextView: TextView

    @BindView(R.id.connection_status_ip_icon)
    lateinit var connectionStatusIPIcon: ImageView

    @BindView(R.id.connection_status_ip_progress_bar)
    lateinit var connectionStatusIPProgressBar: ProgressBar

    @BindView(R.id.connection_status_camera_text)
    lateinit var connectionStatusCameraTextView: TextView

    @BindView(R.id.connection_status_camera_icon)
    lateinit var connectionStatusCameraIcon: ImageView

    @BindView(R.id.connection_status_camera_progress_bar)
    lateinit var connectionStatusCameraProgressBar: ProgressBar

    @BindView(R.id.connection_status_model_name_text)
    lateinit var connectionStatusModelNameTextView: TextView

    @BindView(R.id.ip_address_field)
    lateinit var ipAddressField: EditText

    @BindView(R.id.manual_connect_button)
    lateinit var manuaConnectButton: Button


    private var defaultIPAddressTextColor: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ConnectFragment")

        defaultIPAddressTextColor = ipAddressField.textColors.defaultColor

        installObservers()

        return view
    }

    private fun validateManualIPAddress(): String? {
        var ipAddressString: String? = null
        try {
            ipAddressString = java.net.InetAddress.getByName(ipAddressField.text.toString()).hostAddress
            ipAddressField.setTextColor(defaultIPAddressTextColor)
            manuaConnectButton.isEnabled = true

        } catch (e: Exception) {
            ipAddressField.setTextColor(Color.RED)
            manuaConnectButton.isEnabled = false
        }

        return ipAddressString
    }

    @OnClick(R.id.manual_connect_button)
    fun manualConnection() {
        Log.d("$TAG/manualConnection", "clicked")
        viewModel.repository.isIpManual.value = true
        viewModel.repository.resetIpAddress()
        viewModel.repository.ipAddress.value = validateManualIPAddress()

        viewModel.repository.buildApiService(force = true)
        viewModel.repository.checkAlive()
    }

    @OnClick(R.id.reset_ip_button)
    fun resetIpAddress() {
        viewModel.repository.resetIpAddress()
        ipAddressField.setText(getString(R.string.default_ip))
    }

    @OnClick(R.id.connect_button)
    fun automaticConnection() {
        Log.d("$TAG/automaticConnection", "clicked")
        viewModel.repository.resetIpAddress()

        viewModel.repository.buildApiService(force = true)
        viewModel.repository.checkAlive()
    }

    private fun installObservers() {

        ipAddressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateManualIPAddress()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.repository.wifiState.observe(this, Observer { wifiState ->
            if (wifiState == WifiState.Connected) {
                connectionStatusWIFIIcon.setImageResource(android.R.drawable.presence_online)
                connectionStatusWIFITextView.text = "ON"
            } else {
                connectionStatusWIFIIcon.setImageResource(android.R.drawable.presence_busy)
                connectionStatusWIFITextView.text = "OFF"
            }
        })

        viewModel.repository.ipAddress.observe(this, Observer { ipAddress ->
            ipAddress?.let {
                connectionStatusIPTextView.text = ipAddress
            } ?: run {
                connectionStatusIPTextView.text = getString(R.string.noIpAddressText)
            }
        })

        viewModel.repository.isIpReachable.observe(this, Observer { isIpReachable ->
            if (isIpReachable == true) {
                connectionStatusIPIcon.setImageResource(android.R.drawable.presence_online)
            } else {
                connectionStatusIPIcon.setImageResource(android.R.drawable.presence_busy)
            }
        })

        viewModel.repository.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected == true) {
                connectionStatusCameraIcon.setImageResource(android.R.drawable.presence_online)
                connectionStatusCameraTextView.text = "OK"
            } else {
                connectionStatusCameraIcon.setImageResource(android.R.drawable.presence_busy)
                connectionStatusCameraTextView.text = getString(R.string.noIpAddressText)
            }
        })

        viewModel.repository.cameraModelName.observe(this, Observer { cameraModelName ->
            cameraModelName?.let {
                connectionStatusModelNameTextView.text = cameraModelName
            } ?: run {
                connectionStatusModelNameTextView.text = getString(R.string.noIpAddressText)
            }
        })
    }
}