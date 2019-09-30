package org.hadim.lumitrol.ui.connect

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import org.hadim.lumitrol.base.BaseViewModel


class ConnectViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val TAG: String = "ConnectViewModel"
    }

    init {
        Log.d("$TAG/init", "Init ConnectViewModel")
    }

    fun ping() {
    }
}