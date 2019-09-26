package org.hadim.lumitrol.ui

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.hadim.lumitrol.model.CameraStateModel
import org.hadim.lumitrol.util.forEachChildView
import java.util.*


class ControlFragment : Fragment() {

    private val cameraStateModel: CameraStateModel by activityViewModels()
    private lateinit var rootLayout: View

    private lateinit var streamTimer: Timer
    private lateinit var streamPlayer: Player

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootLayout = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_control, container, false)

        if (cameraStateModel.isCameraDetected.value == true) {
            enableFragment()
            initFragment()
        } else {
            disableFragment()
            stopStream()
        }

//        cameraStateModel.isCameraDetected.observe(this, Observer { isCameraDetected ->
//            if (isCameraDetected) {
//                enableFragment()
//                initFragment()
//            } else {
//                stopStream()
//                disableFragment()
//            }
//        })

        return rootLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (cameraStateModel.isCameraDetected.value == true) {
            if (cameraStateModel.isRecording.value == true) {
                cameraStateModel.cameraRequest.stopRecord(null, null)
                cameraStateModel.isRecording.value = false
            }
        }
        stopStream()
    }

    private fun disableFragment() {
        rootLayout.forEachChildView { it.isEnabled = false }
    }

    private fun enableFragment() {
        rootLayout.forEachChildView { it.isEnabled = true }
    }

    private fun initFragment() {
        cameraStateModel.isRecording.value = false

        // Enable recMode
        cameraStateModel.cameraRequest.recMode(null, null)

        // Handle callbacks for buttons.
        val captureButton: ImageButton = rootLayout.findViewById(org.hadim.lumitrol.R.id.control_capture_button) as ImageButton
        captureButton.setOnClickListener(View.OnClickListener {
            cameraStateModel.cameraRequest.capture(null, null)
        })

        val recordButton: ImageButton = rootLayout.findViewById(org.hadim.lumitrol.R.id.control_record_button) as ImageButton
        recordButton.setOnClickListener(View.OnClickListener {
            if (cameraStateModel.isRecording.value == false) {
                cameraStateModel.cameraRequest.startRecord(null, null)
                cameraStateModel.isRecording.value = true
                captureButton.isEnabled = false
            } else {
                cameraStateModel.cameraRequest.stopRecord(null, null)
                cameraStateModel.isRecording.value = false
                captureButton.isEnabled = true
            }

        })

        cameraStateModel.isRecording.observe(this, Observer { isRecording ->

            val recordButton: ImageButton = rootLayout.findViewById(org.hadim.lumitrol.R.id.control_record_button) as ImageButton
            if (cameraStateModel.isRecording.value == false) {
                recordButton.setImageResource(org.hadim.lumitrol.R.drawable.ic_videocam_black_24dp)
            } else {
                recordButton.setImageResource(org.hadim.lumitrol.R.drawable.ic_stop_black_24dp)
            }

        })

        startStream()
    }

    private fun startStream() {

        if (::streamPlayer.isInitialized && streamPlayer?.isPlaying) return

        var localUdpPort = 41099
        var ipAddress = cameraStateModel.ipAddress.value

        cameraStateModel.cameraRequest.startStream(
            onSuccess = {
                try {
                    initializePlayer(ipAddress, localUdpPort)
                } catch (e: Exception) {
                    Log.e("ControlFragment/startStream", "Socket creation error.")
                    Log.e("ControlFragment/startStream", e.message)
                }
            }, onFailure = { t: Throwable? ->
                Log.e("ControlFragment/startStream", "Stream doesn't start.")
                t?.let { Log.e("ControlFragment/startStream", t.message) }
            },
            port = localUdpPort
        )
    }

    private fun initializePlayer(ipAddress: String?, localUdpPort: Int) {
        cameraStateModel.cameraRequest.autoReviewUnlock(null, null)

        streamTimer = Timer()
        streamTimer.schedule(object : TimerTask() {
            override fun run() {
                cameraStateModel.cameraRequest.startStream(null, null, localUdpPort)
            }
        }, 0, 1000)

        val streamPlayer = ExoPlayerFactory.newSimpleInstance(context?.applicationContext)

        val mediaDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Lumitrol"))

        val stream_uri = Uri.parse("udp://@$ipAddress:$localUdpPort")
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(stream_uri)

        with(streamPlayer) {
            prepare(mediaSource, false, false)
            playWhenReady = true
        }

        val streamView: PlayerView = rootLayout.findViewById(org.hadim.lumitrol.R.id.stream_view) as PlayerView
        streamView.setShutterBackgroundColor(Color.TRANSPARENT)
        streamView.player = streamPlayer
        streamView.requestFocus()
    }

    private fun stopStream() {

        if (::streamPlayer.isInitialized && !streamPlayer?.isPlaying) return

        try {
            cameraStateModel.cameraRequest.stopStream(null, null)
            streamTimer?.cancel()
            if (::streamPlayer.isInitialized) streamPlayer?.release()
        } catch (e: InterruptedException) {
            Log.e("ControlFragment/stopStream", "Error during stream interruption.")
            Log.e("ControlFragment/stopStream", e.message)
        }

    }
}