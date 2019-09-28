package org.hadim.lumitrol.ui.control

import android.util.Log
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.model.Repository
import javax.inject.Inject


class ControlViewModel @Inject constructor(repository: Repository) : ViewModel() {

    companion object {
        const val TAG: String = "ControlViewModel"
    }

    init {
        Log.d("$TAG/init", "Init ControlViewModel")
    }

}