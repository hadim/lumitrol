package org.hadim.lumitrol.ui.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import org.hadim.lumitrol.base.BaseViewModel
import org.hadim.lumitrol.di.viewmodel.ViewModelAssistedFactory
import org.hadim.lumitrol.model.Repository


class GalleryViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    repository: Repository
) : BaseViewModel(savedStateHandle, repository) {

    companion object {
        const val TAG: String = "GalleryViewModel"
    }

    init {
        Log.d("$TAG/init", "Init GalleryViewModel")
    }

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<GalleryViewModel>
}