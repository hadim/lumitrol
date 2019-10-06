package org.hadim.lumitrol.ui.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import org.hadim.lumitrol.base.BaseViewModel


class GalleryViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val TAG: String = "GalleryViewModel"
    }

    init {
        Log.d("$TAG/init", "Init GalleryViewModel")
    }

    fun enablePlayMode() {
        repository.playmode()
    }

    fun getGallery() {
        Log.d("$TAG/getGallery", "dddddddddd")

        Log.d("$TAG/getGallery", repository.upnpManager.toString())
    }

}