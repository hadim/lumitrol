package org.hadim.lumitrol.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hadim.lumitrol.model.CameraStateModel
import org.hadim.lumitrol.util.forEachChildView

class GalleryFragment : Fragment() {

    private val cameraStateModel: CameraStateModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(org.hadim.lumitrol.R.layout.fragment_gallery, container, false)

        if (cameraStateModel.isCameraDetected.value == true) {
            enableFragment(root)
        } else {
            disableFragment(root)
        }

        return root
    }

    private fun disableFragment(root: View) {
        val rootLayout = root.findViewById(org.hadim.lumitrol.R.id.gallery_root) as LinearLayout
        rootLayout.forEachChildView { it.isEnabled = false }
    }

    private fun enableFragment(root: View) {
        val rootLayout = root.findViewById(org.hadim.lumitrol.R.id.gallery_root) as LinearLayout
        rootLayout.forEachChildView { it.isEnabled = true }

        // Enable playMode
        cameraStateModel.cameraRequest.playMode(null, null)
    }
}