package org.hadim.lumitrol.model

import android.util.Log
import org.hadim.lumitrol.model.api.ApiResponse
import org.hadim.lumitrol.model.api.ApiService
import org.hadim.lumitrol.model.api.ApiServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    companion object {
        const val TAG: String = "Repository"
        const val MAX_RETRY_BEFORE_FAIL = 5
    }

    @Inject
    lateinit var apiServiceFactory: ApiServiceFactory

    private lateinit var apiService: ApiService

    init {
        Log.d("$TAG/init", "Init Repository")
    }

    fun test() {

        val ipAddress = "192.168.54.1"

        apiService = apiServiceFactory.create("http://$ipAddress")
        apiService.getState()
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>?, response: Response<ApiResponse>?) {
                    Log.d("$TAG/onResponse", "Request success.")
                    Log.d("$TAG/onResponse", response?.toString())

                    val apiResponse: ApiResponse? = response?.body()
                    Log.d("$TAG/onResponse", apiResponse?.result)
                }

                override fun onFailure(call: Call<ApiResponse>?, t: Throwable?) {
                    Log.d("$TAG/onResponse", "Request failure.")
                    Log.e("$TAG/onFailure", call.toString())
                    Log.e("$TAG/onFailure", t?.message)
                }
            })
    }
}