package org.hadim.lumitrol.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET(".")
    fun ping(): Call<ApiResponse>

    @GET("cam.cgi?mode=getstate")
    fun getState(): Call<ApiResponse>

    @GET("cam.cgi?mode=getinfo&type=capability")
    fun getCapability(): Call<ApiResponse>

    @GET("cam.cgi?mode=startstream&value={port}")
    fun startStream(@Path("port") port: Int): Call<ApiResponse>
}