package org.hadim.lumitrol.ui.connect

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import org.hadim.lumitrol.base.BaseViewModel
import org.hadim.lumitrol.di.viewmodel.ViewModelAssistedFactory
import org.hadim.lumitrol.model.Repository


class ConnectViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    repository: Repository
) : BaseViewModel(savedStateHandle, repository) {

    companion object {
        const val TAG: String = "ConnectViewModel"
    }

    init {
        Log.d("$TAG/init", "Init ConnectViewModel")
    }

    fun ping() {
        Log.e(TAG, repository.toString())
    }

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<ConnectViewModel>
}