package org.hadim.lumitrol.ui.connect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import butterknife.BindView
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

    @BindView(R.id.connection_status_camera_text)
    lateinit var connectionStatusCameraTextView: TextView

    @BindView(R.id.connection_status_camera_icon)
    lateinit var connectionStatusCameraIcon: ImageView

    @BindView(R.id.connection_status_model_name_text)
    lateinit var connectionStatusModelNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ConnectFragment")

        installObservers()

        return view
    }

    private fun installObservers() {

        viewModel.repository.wifiState.observe(this, Observer { wifiState ->
            if (wifiState == WifiState.Connected) {
                connectionStatusWIFIIcon.setImageResource(android.R.drawable.presence_online)
                connectionStatusWIFITextView.text = getString(R.string.on)
            } else {
                connectionStatusWIFIIcon.setImageResource(android.R.drawable.presence_busy)
                connectionStatusWIFITextView.text = getString(R.string.off)
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
                connectionStatusCameraTextView.text = getString(R.string.ok)
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