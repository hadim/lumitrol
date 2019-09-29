package org.hadim.lumitrol.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel


// TODO: try to use this ViewModel.
class MainActivityViewModel : ViewModel() {
    companion object {
        const val TAG: String = "MainActivityViewModel"
    }

    init {
        Log.d("$TAG/init", "Init MainActivityViewModel")
    }
}