package org.hadim.lumitrol.ui.connect

import android.util.Log
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.model.Repository
import javax.inject.Inject


class ConnectViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    companion object {
        const val TAG: String = "ConnectViewModel"
    }

    init {
        Log.d("$TAG/init", "Init ConnectViewModel")
        repository.test()
    }

}