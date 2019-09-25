package org.hadim.lumitrol.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.network.CameraRequest
import org.jsoup.Jsoup
import java.util.*

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

    private var aliveTimer: Timer = Timer()
    private var aliveTimerRunning: Boolean = false

    fun parseInfo(info: String): Boolean {

        Log.d("CameraStateModel/parseInfo", info)

        val doc = Jsoup.parse(info)
        val modelNameValue = doc.select("camrply productinfo modelName").text()

        if (modelNameValue == null) {
            return false
        }

        modelName.postValue(modelNameValue)

        return true
    }

    fun isValid(info: String): Boolean {
        val doc = Jsoup.parse(info)
        val modelNameValue = doc.select("camrply productinfo modelName").text()

        if (modelNameValue == null) {
            return false
        }
        return true
    }

    fun checkAlive(isAliveCallback: ((Boolean) -> Unit)?) {

        if (::cameraRequest.isInitialized) {

            val period: Long = 1000  // ms

            cancelCheckAlive()
            aliveTimerRunning = true

            aliveTimer = Timer()
            aliveTimer.schedule(object : TimerTask() {
                override fun run() {

                    cameraRequest.getInfo(
                        onSuccess = { response: String ->

                            val isValidResponse = isValid(response)
                            if (!isValidResponse) {
                                Log.e("CameraStateModel/checkAlive", "Can't parse the camera response.")
                                Log.e("CameraStateModel/checkAlive", "Response:")
                                Log.e("CameraStateModel/checkAlive", response)
                            }

                            Log.d("CameraStateModel/checkAlive", "Connection alive: $isValidResponse")
                            if (isAliveCallback != null) {
                                isAliveCallback(isValidResponse)
                            }
                        },
                        onFailure = { t: Throwable? ->
                            Log.e("CameraStateModel/checkAlive", "Camera detection failed.")

                            if (t != null && t.message != null) {
                                Log.e("CameraStateModel/checkAlive", t.message as String)
                            }

                            val isValidResponse = false
                            Log.d("CameraStateModel/checkAlive", "Connection alive: $isValidResponse")
                            if (isAliveCallback != null) {
                                isAliveCallback(isValidResponse)
                            }
                        })


                }
            }, 0, period)
        }
    }

    fun cancelCheckAlive() {
        if (aliveTimerRunning) {
            aliveTimerRunning = false
            aliveTimer.cancel()
            aliveTimer.purge()
        }
    }

}