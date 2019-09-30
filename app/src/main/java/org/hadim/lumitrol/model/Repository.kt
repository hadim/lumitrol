package org.hadim.lumitrol.model

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.hadim.lumitrol.base.BaseRepository
import org.hadim.lumitrol.model.api.ApiResponse
import org.hadim.lumitrol.model.api.ApiResponseCapability
import org.hadim.lumitrol.model.api.ApiService
import org.hadim.lumitrol.model.api.ApiServiceFactory
import retrofit2.Call
import java.util.*
import kotlin.concurrent.schedule


enum class WifiState {
    Disabled,               // WiFi is not enabled
    EnabledNotConnected,    // WiFi is enabled but we are not connected to any WiFi network
    Connected,              // Connected to a WiFi network
}

class Repository(
    var application: Application,
    var apiServiceFactory: ApiServiceFactory
) : BaseRepository() {

    companion object {
        const val TAG: String = "Repository"
        const val CHECK_ALIVE_PERIOD: Long = 2000  // ms
        const val IS_REACHABLE_TIMEOUT: Int = 1000  // ms

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: Repository? = null

        fun getRepository(application: Application): Repository {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val apiServiceFactory = ApiServiceFactory()
                val instance = Repository(application, apiServiceFactory)
                INSTANCE = instance
                return instance
            }
        }
    }

    private var apiService: ApiService? = null

    var wifiState: MutableLiveData<WifiState> = MutableLiveData(WifiState.Disabled)
    var ipAddress: MutableLiveData<String?> = MutableLiveData()
    var isIpManual: MutableLiveData<Boolean> = MutableLiveData(false)
    var isIpReachable: MutableLiveData<Boolean> = MutableLiveData(false)
    var isCameraDetected: MutableLiveData<Boolean> = MutableLiveData(false)
    var cameraModelName: MutableLiveData<String?> = MutableLiveData()

    val networkError: MutableLiveData<String?> = MutableLiveData()
    val networkFailure: MutableLiveData<String?> = MutableLiveData()
    val responseError: MutableLiveData<String?> = MutableLiveData()

    private var checkAliveTimer: Timer = Timer()

    init {
        Log.d("$TAG/init", "Init Repository")

        checkAlive()
        //setupCheckAlive()
    }

    fun buildApiService(force: Boolean? = false) {
        if (apiService == null || force == true) {
            ipAddress.value?.let { ipAddress ->
                apiService = apiServiceFactory.create("http://$ipAddress")
            }
        }
    }

    private fun runCall(
        call: Call<ApiResponseCapability>?,
        success: ((apiResponse: ApiResponse?) -> Unit)?,
        error: ((apiResponse: ApiResponse?) -> Unit)?
    ) {

        buildApiService()
        if (apiService != null && call != null) {
            call.makeCall {
                onSuccess = { apiResponse ->
                    networkError.postValue(null)
                    networkFailure.postValue(null)
                    success?.let {
                        apiResponse?.let { response ->
                            response.result?.let { result ->
                                if (result == "ok") {
                                    success(apiResponse)
                                } else {
                                    Log.e("$TAG/runCall", "Response Error: $result.")
                                    error?.let { error(apiResponse) }
                                }
                            } ?: run {
                                error?.let { error(apiResponse) }
                            }
                        } ?: run {
                            error?.let { error(apiResponse) }
                        }
                    }
                    onError = { _, response ->
                        networkError.postValue(response?.message())
                        error?.let { error(response?.body()) }
                    }
                    onFailure = { _, t ->
                        networkFailure.postValue(t?.message)
                        error?.let { error(null) }
                    }
                }
            }
        } else {
            Log.e("$TAG/runCall", "Can't make network call because `ipAddress` is null.")
        }

    }

    private fun setupCheckAlive() {
        checkAliveTimer.schedule(object : TimerTask() {
            override fun run() {
                checkAlive()
            }
        }, 0, CHECK_ALIVE_PERIOD)
    }

    fun checkAlive() {
        checkWifiState()
        if (wifiState.value == WifiState.Connected) {
            checkIpReachable()
        } else {
            isIpReachable.value = false
            isCameraDetected.value = false
            cameraModelName.value = null
        }

        if (isIpReachable.value == true) {
            var stateString = "Connection State:" +
                    "\n\tWifi: ${wifiState.value}" +
                    "\n\tIP: ${ipAddress.value}" +
                    "\n\tReachable: ${isIpReachable.value}" +
                    "\n\tCamera detected: ${isCameraDetected.value}"
            Log.d("$TAG/runCall", stateString)
        }
    }

    fun resetIpAddress() {
        ipAddress.value = null
        isIpReachable.value = false
        isCameraDetected.value = false
        cameraModelName.value = null
    }

    private fun checkCameraDetected() {
        Log.d("$TAG/checkCameraDetected", "IP: ${ipAddress.value}")
        runCall(apiService?.getCapability(),
            success = { response ->
                var rep = response as ApiResponseCapability
                isCameraDetected.postValue(true)
                cameraModelName.postValue(rep.info.modelName)
            },
            error = {
                isCameraDetected.postValue(false)
            })
    }

    private fun checkIpReachable() {
        ipAddress.value?.let {
            val inetAddress = java.net.InetAddress.getByName(it)
            Timer().schedule(0) {
                var isReachable = inetAddress.isReachable(IS_REACHABLE_TIMEOUT)
                isIpReachable.postValue(isReachable)
                if (isReachable) {
                    checkCameraDetected()
                }
            }
        } ?: run {
            isIpReachable.value = false
        }
    }

    private fun checkWifiState() {
        val wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        wifiManager?.let {

            if (wifiManager.isWifiEnabled) {
                if (wifiManager.connectionInfo.bssid != null) {
                    wifiState.value = WifiState.Connected
                } else {
                    wifiState.value = WifiState.EnabledNotConnected
                }
            } else {
                wifiState.value = WifiState.Disabled
            }
        }
    }
}