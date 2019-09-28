package org.hadim.lumitrol.base

import android.util.Log
import org.hadim.lumitrol.model.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseRepository {

    companion object {
        const val TAG: String = "BaseRepository"
    }

    fun <T> Call<T>.makeCall(callback: CallBackKotlin<T>.() -> Unit) {
        val callBackKt = CallBackKotlin<T>()
        callback.invoke(callBackKt)
        this.enqueue(callBackKt)
    }

    class CallBackKotlin<T> : Callback<T> {
        var onSuccess: ((T?) -> Unit)? = null
        var onError: ((Call<T>?, Response<T>?) -> Unit)? = null
        var onFailure: ((Call<T>?, Throwable?) -> Unit)? = null

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                Log.d("$TAG/makeCall", "Request success.")
                Log.d("$TAG/makeCall", "Request: $response")
                Log.d("$TAG/makeCall", "Message: ${response.message()}")
                onSuccess?.invoke(response.body())
            } else {
                Log.d("$TAG/makeCall", "Request error.")
                Log.d("$TAG/makeCall", "Request: $response")
                Log.d("$TAG/makeCall", "Message: ${response.message()}")
                Log.d("$TAG/makeCall", "Body: ${response.errorBody()}")
                onError?.invoke(call, response)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d("${Repository.TAG}/makeCall", "Request failure.")
            Log.d("${Repository.TAG}/makeCall", "Request: ${call.request()}")
            Log.d("${Repository.TAG}/makeCall", "Message: ${t.message}")
            onFailure?.invoke(call, t)
        }
    }
}