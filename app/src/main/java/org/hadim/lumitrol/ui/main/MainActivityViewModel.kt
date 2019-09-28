package org.hadim.lumitrol.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class MainActivityViewModel(private var savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val TAG: String = "MainActivityViewModel"

        private const val NETWORK_ERROR = "netWorkError"
        private const val NETWORK_FAILURE = "netWorkFailure"
    }

    val netWorkError: MutableLiveData<String?> = savedStateHandle.getLiveData(NETWORK_ERROR)
    val netWorkFailure: MutableLiveData<String?> = savedStateHandle.getLiveData(NETWORK_FAILURE)

    init {
        Log.d("$TAG/init", "Init MainActivityViewModel")
    }

    fun ping() {
        Log.d("$TAG/ping", "ping")
    }

}