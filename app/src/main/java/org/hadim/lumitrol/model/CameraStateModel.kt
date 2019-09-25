package org.hadim.lumitrol.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.network.CameraRequest
import org.jsoup.Jsoup

class CameraStateModel(state: SavedStateHandle) : ViewModel() {

    // Keep the key as a constant
    companion object {
        private const val IS_WIFI_ENABLED_KEY = "isWifiEnabled"
        private const val IP_KEY = "ipAddress"
        private const val IS_IP_MANUAL_KEY = "isIPManual"
        private const val IS_IP_REACHABLE_KEY = "isIReachable"
        private const val IS_CAMERA_DETECTED_KEY = "isCameraDetected"
        private const val MODEL_NAME_KEY = "modelName"
        private const val IS_RECORDING_KEY = "isRecording"
    }

    private val savedStateHandle = state

    val isWifiEnabled: MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_WIFI_ENABLED_KEY)
    val ipAddress: MutableLiveData<String> = savedStateHandle.getLiveData(IP_KEY)
    val isIPManual: MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_IP_MANUAL_KEY)
    val isIReachable: MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_IP_REACHABLE_KEY)
    val isCameraDetected: MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_CAMERA_DETECTED_KEY)

    val modelName: MutableLiveData<String> = savedStateHandle.getLiveData(MODEL_NAME_KEY)

    val isRecording: MutableLiveData<Boolean> = savedStateHandle.getLiveData(IS_RECORDING_KEY)

    lateinit var cameraRequest: CameraRequest

    fun parseInfo(info: String): Boolean {

        Log.d("HOME/cameraDetection", info)

        val doc = Jsoup.parse(info)
        val modelNameValue = doc.select("camrply productinfo modelName").text()

        if (modelNameValue == null) {
            return false
        }

        modelName.postValue(modelNameValue)

        return true
    }
}