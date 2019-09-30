package org.hadim.lumitrol.ui.control

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import org.hadim.lumitrol.base.BaseViewModel


class ControlViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val TAG: String = "ControlViewModel"
    }

    init {
        Log.d("$TAG/init", "Init ControlViewModel")
    }
}