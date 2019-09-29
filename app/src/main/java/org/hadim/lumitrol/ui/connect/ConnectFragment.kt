package org.hadim.lumitrol.ui.connect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment

class ConnectFragment : BaseFragment<ConnectViewModel>() {

    companion object {
        const val TAG: String = "ConnectFragment"
    }

    override val viewModelClass: Class<ConnectViewModel> = ConnectViewModel::class.java
    override val layoutId: Int = R.layout.fragment_connect

    @BindView(R.id.log_text)
    lateinit var logTextView: TextView

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ConnectFragment")

        return view
    }

    @OnClick(R.id.manual_connect_button)
    fun manualConnection() {
        Log.d("$TAG/manualConnection", "clicked")
        viewModel.repository.isIpManual.postValue(true)
    }

    @OnClick(R.id.connect_button)
    fun automaticConnection() {
        Log.d("$TAG/automaticConnection", "clicked")
        viewModel.repository.isIpManual.postValue(false)
    }
}