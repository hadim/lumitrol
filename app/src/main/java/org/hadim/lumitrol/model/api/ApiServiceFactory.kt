package org.hadim.lumitrol.model.api

import retrofit2.Retrofit
import javax.inject.Inject

class ApiServiceFactory @Inject constructor(private val builder: Retrofit.Builder) {

    fun create(url: String): ApiService {
        return builder.baseUrl(url).build().create(ApiService::class.java)
    }

}