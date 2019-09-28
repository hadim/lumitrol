package org.hadim.lumitrol.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment

class GalleryFragment : BaseFragment() {

    companion object {
        const val TAG: String = "GalleryFragment"
    }

//    @Inject
//    lateinit var galleryViewModel: GalleryViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_gallery
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = super.onCreateView(inflater, container, savedInstanceState)

        Log.d("$TAG/onCreateView", "Init GalleryFragment")

        return root
    }
}