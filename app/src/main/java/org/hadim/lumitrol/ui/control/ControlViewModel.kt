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

    fun enableRecMode(){
        repository.recmode()
    }

    fun capture() {
        repository.capture()
    }

    fun record() {
        if (isRecording.value == false) {
            repository.startRecord()
            isRecording.value = true
        } else {
            repository.stopRecord()
            isRecording.value = false
        }
    }
}