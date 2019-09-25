package org.hadim.lumitrol.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.hadim.lumitrol.R
import org.hadim.lumitrol.model.CameraStateModel
import org.hadim.lumitrol.util.forEachChildView

class ControlFragment : Fragment() {

    private val cameraStateModel: CameraStateModel by activityViewModels()
    private lateinit var rootLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootLayout = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_control, container, false)

        if (cameraStateModel.isCameraDetected.value == true) {
            enableFragment()
            initFragment()
        } else {
            disableFragment()
        }

        cameraStateModel.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected) {
                enableFragment()
                initFragment()
            } else {
                disableFragment()
            }
        })

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

            val recordButton: ImageButton = rootLayout.findViewById(R.id.control_record_button) as ImageButton
            if (cameraStateModel.isRecording.value == false) {
                recordButton.setImageResource(R.drawable.ic_videocam_black_24dp)
            } else {
                recordButton.setImageResource(R.drawable.ic_stop_black_24dp)
            }

        })
    }
}