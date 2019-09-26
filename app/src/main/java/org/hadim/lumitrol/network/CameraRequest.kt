package org.hadim.lumitrol.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class CameraRequest(ip: String, context: Context) {

    private val ipAddress: String
    private val requestQueue: RequestQueue

    init {
        ipAddress = ip
        requestQueue = Volley.newRequestQueue(context)
    }

    private fun request(request: String, onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {

        doAsync {
            val url = "http://$ipAddress/$request"
            var stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response: String ->
                    onSuccess?.let { uiThread { onSuccess(response) } }
                },
                Response.ErrorListener { t ->
                    onFailure?.let { uiThread { onFailure(t) } }
                })
            requestQueue.add(stringRequest)
        }
    }

    fun getState(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=getstate"
        request(request, onSuccess, onFailure)
    }

    fun getInfo(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=getinfo&type=capability"
        request(request, onSuccess, onFailure)
    }

    fun getAllMenu(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=getinfo&type=allmenu"
        request(request, onSuccess, onFailure)
    }

    fun getCurrentMenu(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=getinfo&type=currmenu"
        request(request, onSuccess, onFailure)
    }

    fun capture(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=camcmd&value=capture"
        request(request, onSuccess, onFailure)
    }

    fun recMode(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=camcmd&value=recmode"
        request(request, onSuccess, onFailure)
    }

    fun playMode(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=camcmd&value=playmode"
        request(request, onSuccess, onFailure)
    }

    fun startRecord(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=camcmd&value=video_recstart"
        request(request, onSuccess, onFailure)
    }

    fun stopRecord(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=camcmd&value=video_recstop"
        request(request, onSuccess, onFailure)
    }

    fun startStream(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?, port: Int) {
        val request = "cam.cgi?mode=startstream&value=$port"
        request(request, onSuccess, onFailure)
    }

    fun stopStream(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=stopstream"
        request(request, onSuccess, onFailure)
    }

    fun autoReviewUnlock(onSuccess: ((String) -> Unit)?, onFailure: ((Throwable?) -> Unit)?) {
        val request = "cam.cgi?mode=camcmd?value=autoreviewunlock"
        request(request, onSuccess, onFailure)
    }


}
