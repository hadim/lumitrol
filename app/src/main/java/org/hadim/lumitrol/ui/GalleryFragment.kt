package org.hadim.lumitrol.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.hadim.lumitrol.model.CameraStateModel
import org.hadim.lumitrol.util.forEachChildView

class GalleryFragment : Fragment() {

    private val cameraStateModel: CameraStateModel by activityViewModels()
    private lateinit var rootLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootLayout = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_gallery, container, false)

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
        // Enable playMode
        cameraStateModel.cameraRequest.playMode(null, null)
    }
}