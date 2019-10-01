package org.hadim.lumitrol.ui.control

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.OnClick
import org.hadim.lumitrol.base.BaseFragment
import org.hadim.lumitrol.network.stream.StreamPlayer
import org.hadim.lumitrol.network.stream.StreamViewer
import java.util.*


class ControlFragment : BaseFragment<ControlViewModel>() {

    companion object {
        const val TAG: String = "ControlFragment"
    }

    override val viewModelClass: Class<ControlViewModel> = ControlViewModel::class.java
    override val layoutId: Int = org.hadim.lumitrol.R.layout.fragment_control

    @BindView(org.hadim.lumitrol.R.id.control_record_button)
    lateinit var recordButton: ImageButton

    @BindView(org.hadim.lumitrol.R.id.stream_view)
    lateinit var streamViewer: StreamViewer

    @BindView(org.hadim.lumitrol.R.id.progress_bar_stream)
    lateinit var progressBarStream: ProgressBar

    private var streamTimer: Timer? = null
    private var streamPlayer: StreamPlayer? = null
    private var streamPlayerThread: Thread? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ControlFragment")

        installObservers()

        if (viewModel.repository.isCameraDetected.value == true) {
            viewModel.enableRecMode()
            startStream()
        }

        return view
    }

    @OnClick(org.hadim.lumitrol.R.id.control_capture_button)
    fun captureButton() {
        Log.d("$TAG/captureButton", "clicked")
        viewModel.capture(onError = { error ->
            showError("Capture Error: $error")
        })
    }

    @OnClick(org.hadim.lumitrol.R.id.control_record_button)
    fun recordButton() {
        Log.d("$TAG/recordButton", "clicked")
        viewModel.record(onError = { error ->
            showError("Record Error: $error")
        })
    }

    private fun showProgressBar() {
        if (::progressBarStream.isInitialized)
            progressBarStream.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        if (::progressBarStream.isInitialized)
            progressBarStream.visibility = View.INVISIBLE
    }

    private fun installObservers() {

        viewModel.isRecording.observe(this, Observer { isRecording ->
            if (viewModel.isRecording.value == false) {
                recordButton.setImageResource(org.hadim.lumitrol.R.drawable.ic_videocam_black_24dp)
            } else {
                recordButton.setImageResource(org.hadim.lumitrol.R.drawable.ic_stop_black_24dp)
            }
        })

        viewModel.repository.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected == true) {
                enableFragment()
                startStream()
                viewModel.enableRecMode()
            } else {
                disableFragment()
                stopStream()
                hideProgressBar()
            }
        })
    }

    private fun startStream() {

        if (streamPlayerThread != null) return

        Log.d("$TAG/startStream", "Start")

        showProgressBar()

        streamPlayerThread?.let { return }

        val localUdpPort = 46995
        val ipAddress = viewModel.repository.ipAddress.value as String

        // Send startstream command every 5 seconds to maintain streaming.
        streamTimer?.cancel()
        streamTimer = Timer()
        streamTimer?.schedule(object : TimerTask() {
            override fun run() {
                viewModel.repository.startStream(localUdpPort)
            }
        }, 0, 5000)

        // Initialize player
        streamPlayer = StreamPlayer(
            imageConsumer = streamViewer::setCurrentImage,
            onStreaming = { Handler(Looper.getMainLooper()).post { hideProgressBar() } },
            onLoading = { Handler(Looper.getMainLooper()).post { showProgressBar() } },
            onFailure = { Handler(Looper.getMainLooper()).post { stopStream() } },
            ipAddress = ipAddress,
            udpPort = localUdpPort
        )
        streamPlayer?.let { streamPlayer ->
            streamPlayerThread = Thread(streamPlayer)
            streamPlayerThread?.start()
        }
    }

    private fun stopStream() {

        if (streamPlayerThread == null) return

        Log.d("$TAG/stopStream", "Stop")

        hideProgressBar()
        try {
            viewModel.repository.stopStream()
            streamTimer?.cancel()
            streamPlayerThread?.let { streamPlayerThread ->
                if (streamPlayerThread.state != Thread.State.TERMINATED) {
                    streamPlayerThread.interrupt()
                    streamPlayerThread.join()
                }
            }
            streamPlayer = null
            streamPlayerThread = null
        } catch (error: Exception) {
            Log.e("ControlFragment/stopStream", "Error during stream interruption.")
            error.message?.let { message ->
                Log.e("ControlFragment/startStream", message)
            }
            throw error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopStream()
    }
}
