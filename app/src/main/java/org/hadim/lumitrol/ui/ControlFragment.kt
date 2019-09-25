package org.hadim.lumitrol.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.hadim.lumitrol.R
import org.hadim.lumitrol.model.CameraStateModel
import org.hadim.lumitrol.util.forEachChildView

class ControlFragment : Fragment() {

    private val cameraStateModel: CameraStateModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_control, container, false)

        if (cameraStateModel.isCameraDetected.value == true) {
            enableFragment(root)
        } else {
            disableFragment(root)
        }

        return root
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

    private fun disableFragment(root: View) {
        val rootLayout = root.findViewById(org.hadim.lumitrol.R.id.control_root) as LinearLayout
        rootLayout.forEachChildView { it.isEnabled = false }
    }

    private fun enableFragment(root: View) {
        val rootLayout = root.findViewById(org.hadim.lumitrol.R.id.control_root) as LinearLayout
        rootLayout.forEachChildView { it.isEnabled = true }

        cameraStateModel.isRecording.value = false

        // Enable recMode
        cameraStateModel.cameraRequest.recMode(null, null)

        // Handle callbacks for buttons.
        val captureButton: ImageButton = root.findViewById(org.hadim.lumitrol.R.id.control_capture_button) as ImageButton
        captureButton.setOnClickListener(View.OnClickListener {
            cameraStateModel.cameraRequest.capture(null, null)
        })

        val recordButton: ImageButton = root.findViewById(org.hadim.lumitrol.R.id.control_record_button) as ImageButton
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

            val recordButton: ImageButton = root.findViewById(R.id.control_record_button) as ImageButton
            if (cameraStateModel.isRecording.value == false) {
                recordButton.setImageResource(R.drawable.ic_videocam_black_24dp)
            } else {
                recordButton.setImageResource(R.drawable.ic_stop_black_24dp)
            }

        })
    }
}