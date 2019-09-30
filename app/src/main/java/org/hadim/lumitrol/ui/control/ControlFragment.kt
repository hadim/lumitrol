package org.hadim.lumitrol.ui.control

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.OnClick
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment
import java.util.*

class ControlFragment : BaseFragment<ControlViewModel>() {

    companion object {
        const val TAG: String = "ControlFragment"
    }

    override val viewModelClass: Class<ControlViewModel> = ControlViewModel::class.java
    override val layoutId: Int = R.layout.fragment_control

    @BindView(R.id.control_record_button)
    lateinit var recordButton: ImageButton

    @BindView(R.id.stream_view)
    lateinit var streamView: PlayerView

    private lateinit var streamTimer: Timer
    private lateinit var streamPlayer: Player

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ControlFragment")

        installObservers()
        viewModel.enableRecMode()

        startStream()

        return view
    }

    @OnClick(R.id.control_capture_button)
    fun captureButton() {
        Log.d("$TAG/captureButton", "clicked")
        viewModel.capture()
    }

    @OnClick(R.id.control_record_button)
    fun recordButton() {
        Log.d("$TAG/recordButton", "clicked")
        viewModel.record()
    }

    private fun installObservers() {

        viewModel.isRecording.observe(this, Observer { isRecording ->
            if (viewModel.isRecording.value == false) {
                recordButton.setImageResource(R.drawable.ic_videocam_black_24dp)
            } else {
                recordButton.setImageResource(R.drawable.ic_stop_black_24dp)
            }
        })

        viewModel.repository.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected == true) {
                enableFragment()
            } else {
                disableFragment()
            }
        })
    }

    private fun startStream() {

        if (::streamPlayer.isInitialized && streamPlayer.isPlaying) return

        var localUdpPort = 28635
        var ipAddress = viewModel.repository.ipAddress.value

        viewModel.repository.startStream(localUdpPort)
        initializePlayer(ipAddress, localUdpPort)
    }

    private fun initializePlayer(ipAddress: String?, localUdpPort: Int) {

        streamTimer = Timer()
        streamTimer.schedule(object : TimerTask() {
            override fun run() {
                //cameraStateModel.cameraRequest.startStream(null, null, localUdpPort)
            }
        }, 0, 5000)

        val streamPlayer = ExoPlayerFactory.newSimpleInstance(context?.applicationContext)

        val mediaDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Lumitrol"))

        val streamURI = Uri.parse("udp://$ipAddress:$localUdpPort")
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(streamURI)

        with(streamPlayer) {
            prepare(mediaSource, false, false)
            playWhenReady = true
        }

        streamView.setShutterBackgroundColor(Color.TRANSPARENT)
        streamView.player = streamPlayer
        streamView.requestFocus()
    }

    private fun stopStream() {

        if (::streamPlayer.isInitialized && !streamPlayer.isPlaying) return

        try {
            viewModel.repository.stopStream()
            streamTimer.cancel()
            if (::streamPlayer.isInitialized) streamPlayer.release()
        } catch (e: InterruptedException) {
            Log.e("ControlFragment/stopStream", "Error during stream interruption.")
            Log.e("ControlFragment/stopStream", e.message)
        }

    }
}
