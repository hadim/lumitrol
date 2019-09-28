package org.hadim.lumitrol.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.squareup.inject.assisted.AssistedInject
import org.hadim.lumitrol.base.BaseRepository
import org.hadim.lumitrol.model.api.ApiService
import org.hadim.lumitrol.model.api.ApiServiceFactory


class Repository @AssistedInject constructor() : BaseRepository() {

    companion object {
        const val TAG: String = "Repository"
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): Repository
    }

    private lateinit var apiServiceFactory: ApiServiceFactory
    private lateinit var apiService: ApiService

    val netWorkError: MutableLiveData<String?> = MutableLiveData()
    val netWorkFailure: MutableLiveData<String?> = MutableLiveData()

    init {
        Log.d("$TAG/init", "Init Repository")
    }

    fun call() {

        val ipAddress = "192.168.54.1"

        apiService = apiServiceFactory.create("http://$ipAddress")

        apiService.getCapability()
            .makeCall {
                onSuccess = { apiResponse ->
                    netWorkError.postValue(null)
                    netWorkFailure.postValue(null)
                    Log.d(TAG, apiResponse?.info?.modelName)
                }
                onError = { call, response ->
                    netWorkError.postValue(response?.message())
                }
                onFailure = { call, t ->
                    netWorkFailure.postValue(t?.message)
                }
            }
    }
}