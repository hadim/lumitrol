package org.hadim.lumitrol.ui.connect

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import butterknife.BindView
import butterknife.OnClick
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment

class ConnectFragment : BaseFragment() {

    companion object {
        const val TAG: String = "ConnectFragment"
    }

    private val connectViewModel: ConnectViewModel
            by viewModels { SavedStateViewModelFactory(this.activity?.application as Application, this) }

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

    override fun layoutRes(): Int {
        return R.layout.fragment_connect
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ConnectFragment")

        return root
    }

    @OnClick(R.id.manual_connect_button)
    fun manualConnection() {
        Log.d("$TAG/manualConnection", "clicked")
        connectViewModel.ping()
    }

    @OnClick(R.id.connect_button)
    fun automaticConnection() {
        Log.d("$TAG/automaticConnection", "clicked")
    }
}