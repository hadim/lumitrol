package org.hadim.lumitrol.ui.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.model.Repository
import javax.inject.Inject


class GalleryViewModel @Inject constructor(repository: Repository) : ViewModel() {

    companion object {
        const val TAG: String = "GalleryViewModel"
    }

    init {
        Log.d("$TAG/init", "Init GalleryViewModel")
    }

}