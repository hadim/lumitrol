package org.hadim.lumitrol.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment

class GalleryFragment : BaseFragment<GalleryViewModel>() {

    companion object {
        const val TAG: String = "GalleryFragment"
    }

    override val viewModelClass: Class<GalleryViewModel> = GalleryViewModel::class.java
    override val layoutId: Int = R.layout.fragment_gallery

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init GalleryFragment")

        installObservers()
        viewModel.enablePlayMode()

        return view
    }

    private fun installObservers() {

        viewModel.repository.isCameraDetected.observe(this, Observer { isCameraDetected ->
            if (isCameraDetected == true) {
                enableFragment()
                populateGallery()
            } else {
                disableFragment()
            }
        })

    }

    private fun populateGallery() {
        viewModel.getGallery()
    }
}