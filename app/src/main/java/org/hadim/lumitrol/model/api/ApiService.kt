package org.hadim.lumitrol.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/*
 * See:
 * https://github.com/cleverfox/lumixproto
 *
 */

interface ApiService {

    @GET(".")
    fun ping(): Call<String>

    @GET("cam.cgi?mode=getstate")
    fun getState(): Call<ApiResponseSimple>

    // mode = getinfo

    @GET("cam.cgi?mode=getinfo&type=capability")
    fun getCapability(): Call<ApiResponseCapability>

    @GET("cam.cgi?mode=getinfo&type=allmenu")
    fun getAllMenu(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=getinfo&type=currmenu")
    fun getCurrentMenu(): Call<ApiResponseSimple>

    // mode = camcmd

    @GET("cam.cgi?mode=camcmd&value=capture")
    fun capture(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=camcmd&value=recmode")
    fun recmode(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=camcmd&value=playmode")
    fun playmode(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=camcmd&value=video_recstart")
    fun startRecord(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=camcmd&value=video_recstop")
    fun stopRecord(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=camcmd&value=autoreviewunlock")
    fun autoreviewUnlock(): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=camcmd&value=menu_entry")
    fun menuEntry(): Call<ApiResponseSimple>

    // mode = stream

    @GET("cam.cgi?mode=startstream")
    fun startStream(@Query("value") port: Int): Call<ApiResponseSimple>

    @GET("cam.cgi?mode=stopstream")
    fun stopStream(): Call<ApiResponseSimple>

    // Focus (mode = camctrl)

    // `value` can be { start, continue, stop }
    // `value2` must be `X/Y`
    @GET("cam.cgi?mode=camctrl&type=touch_trace")
    fun touchFocus(
        @Query("value") value: String,
        @Query("value2") value2: String
    ): Call<ApiResponseSimple>

}