package org.hadim.lumitrol.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.network.CameraRequest

class CameraStateModel(state: SavedStateHandle) : ViewModel() {

    // Keep the key as a constant
    companion object {
        private const val IP_KEY = "ipAddress"
        private const val IS_IP_MANUAL_KEY = "isIPManual"
        private const val IS_IP_REACHABLE_KEY = "isIReachable"
        private const val IS_CAMERA_DETECTED_KEY = "isCameraDetected"
        private const val MODEL_NAME_KEY = "modelName"
    }

    private val savedStateHandle = state

    val ipAddress : MutableLiveData<String> = savedStateHandle.getLiveData(IP_KEY)
    val isIPManual : MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_IP_MANUAL_KEY)
    val isIReachable : MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_IP_REACHABLE_KEY)
    val isCameraDetected : MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_CAMERA_DETECTED_KEY)

    val modelName : MutableLiveData<String> = savedStateHandle.getLiveData(MODEL_NAME_KEY)

    lateinit var cameraRequest: CameraRequest
}