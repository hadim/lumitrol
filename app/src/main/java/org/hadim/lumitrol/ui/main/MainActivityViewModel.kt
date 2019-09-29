package org.hadim.lumitrol.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


//class MainActivityViewModel @AssistedInject constructor(
//    @Assisted savedStateHandle: SavedStateHandle,
//    repository: Repository
//) : BaseViewModel(savedStateHandle, repository) {

class MainActivityViewModel : ViewModel() {

    companion object {
        const val TAG: String = "MainActivityViewModel"

        private const val NETWORK_ERROR = "netWorkError"
        private const val NETWORK_FAILURE = "netWorkFailure"
    }

//    @Inject
//    lateinit var repository: Repository

    val netWorkError: MutableLiveData<String?> = MutableLiveData()  //savedStateHandle.getLiveData(NETWORK_ERROR)
    val netWorkFailure: MutableLiveData<String?> = MutableLiveData()  //savedStateHandle.getLiveData(NETWORK_FAILURE)

    init {
        Log.d("$TAG/init", "Init MainActivityViewModel")
    }

    fun ping() {
        Log.d("$TAG/ping", "ping")
        // Log.e("$TAG/ping", repository.toString())
    }
//
//    @AssistedInject.Factory
//    interface Factory : ViewModelAssistedFactory<MainActivityViewModel>
}