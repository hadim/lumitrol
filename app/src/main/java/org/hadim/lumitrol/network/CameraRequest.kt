package org.hadim.lumitrol.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class CameraRequest {

    private val ipAddress: String
    private val context: Context

    constructor(ip: String, ctx: Context) {
        ipAddress = ip
        context = ctx
    }

    private fun request(request: String, onSuccess: (String) -> Unit, onFailure: (Throwable?) -> Unit): StringRequest {
        val queue = Volley.newRequestQueue(context)
        val url = "http://$ipAddress/$request"
        var stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response: String ->
                onSuccess(response)
            },
            Response.ErrorListener { t ->
                onFailure(t.cause)
            })
        queue.add(stringRequest)
        return stringRequest
    }

    fun getState(onSuccess: (String) -> Unit, onFailure: (Throwable?) -> Unit) {
        val request = "cam.cgi??mode=getstate"
        request(request, onSuccess, onFailure)
    }

    fun getInfo(onSuccess: (String) -> Unit, onFailure: (Throwable?) -> Unit) {
        val request = "cam.cgi??mode=getinfo&type=capability"
        request(request, onSuccess, onFailure)
    }

}
