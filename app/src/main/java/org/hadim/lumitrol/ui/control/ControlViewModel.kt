package org.hadim.lumitrol.ui.control

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import org.hadim.lumitrol.base.BaseViewModel


class ControlViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val TAG: String = "ControlViewModel"
    }

    var isRecording: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        Log.d("$TAG/init", "Init ControlViewModel")
    }

    fun enableRecMode() {
        repository.recmode()
    }

    fun capture(onError: (error: Any?) -> Unit) {
        repository.capture(onError)
    }

    fun record(onError: (error: Any?) -> Unit) {
        if (isRecording.value == false) {
            repository.startRecord(onError)
            isRecording.value = true
        } else {
            repository.stopRecord(onError)
            isRecording.value = false
        }
    }
}