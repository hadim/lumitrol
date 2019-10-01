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
import org.hadim.lumitrol.base.BaseFragment
import org.hadim.lumitrol.model.WifiState


class ConnectFragment : BaseFragment<ConnectViewModel>() {

    companion object {
        const val TAG: String = "ConnectFragment"
    }

    override val viewModelClass: Class<ConnectViewModel> = ConnectViewModel::class.java
    override val layoutId: Int = org.hadim.lumitrol.R.layout.fragment_connect

    @BindView(org.hadim.lumitrol.R.id.connection_status_wifi_text)
    lateinit var connectionStatusWIFITextView: TextView

    @BindView(org.hadim.lumitrol.R.id.connection_status_wifi_icon)
    lateinit var connectionStatusWIFIIcon: ImageView

    @BindView(org.hadim.lumitrol.R.id.connection_status_ip_text)
    lateinit var connectionStatusIPTextView: TextView

    @BindView(org.hadim.lumitrol.R.id.connection_status_ip_icon)
    lateinit var connectionStatusIPIcon: ImageView

    @BindView(org.hadim.lumitrol.R.id.connection_status_ip_progress_bar)
    lateinit var connectionStatusIPProgressBar: ProgressBar

    @BindView(org.hadim.lumitrol.R.id.connection_status_camera_text)
    lateinit var connectionStatusCameraTextView: TextView

    @BindView(org.hadim.lumitrol.R.id.connection_status_camera_icon)
    lateinit var connectionStatusCameraIcon: ImageView

    @BindView(org.hadim.lumitrol.R.id.connection_status_camera_progress_bar)
    lateinit var connectionStatusCameraProgressBar: ProgressBar

    @BindView(org.hadim.lumitrol.R.id.connection_status_model_name_text)
    lateinit var connectionStatusModelNameTextView: TextView

    @BindView(org.hadim.lumitrol.R.id.ip_address_field)
    lateinit var ipAddressField: EditText

    @BindView(org.hadim.lumitrol.R.id.manual_connect_button)
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

    @OnClick(org.hadim.lumitrol.R.id.manual_connect_button)
    fun manualConnection() {
        var ipAddressString: String? = null
        try {
            ipAddressString = java.net.InetAddress.getByName(ipAddressField.text.toString()).hostAddress
            ipAddressField.setTextColor(defaultIPAddressTextColor)
            manuaConnectButton.isEnabled = true
            viewModel.manualConnection(ipAddressString)

        } catch (e: Exception) {
            ipAddressField.setTextColor(Color.RED)
            manuaConnectButton.isEnabled = false
        }
    }

    @OnClick(org.hadim.lumitrol.R.id.connect_button)
    fun automaticConnection() {
        viewModel.automaticConnection()
    }

    @OnClick(org.hadim.lumitrol.R.id.reset_ip_button)
    fun resetIpAddress() {
        viewModel.repository.resetIpAddress()
        ipAddressField.setText(getString(org.hadim.lumitrol.R.string.default_ip))
    }

    private fun installObservers() {

        ipAddressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    var address = java.net.InetAddress.getByName(ipAddressField.text.toString()).hostAddress
                    ipAddressField.setTextColor(defaultIPAddressTextColor)
                    manuaConnectButton.isEnabled = true
                    viewModel.manualConnection(address)
                } catch (e: Exception) {
                    ipAddressField.setTextColor(Color.RED)
                    manuaConnectButton.isEnabled = false
                }
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
                connectionStatusIPTextView.text = getString(org.hadim.lumitrol.R.string.noIpAddressText)
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
                connectionStatusCameraTextView.text = getString(org.hadim.lumitrol.R.string.noIpAddressText)
            }
        })

        viewModel.repository.cameraModelName.observe(this, Observer { cameraModelName ->
            cameraModelName?.let {
                connectionStatusModelNameTextView.text = cameraModelName
            } ?: run {
                connectionStatusModelNameTextView.text = getString(org.hadim.lumitrol.R.string.noIpAddressText)
            }
        })
    }
}