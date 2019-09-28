package org.hadim.lumitrol.ui.connect

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class ConnectViewModel(private var savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val TAG: String = "ConnectViewModel"
    }

    init {
        Log.d("$TAG/init", "Init ConnectViewModel")
    }

    fun ping() {
        //Log.e(TAG, repositoryFactory.toString())
    }

}