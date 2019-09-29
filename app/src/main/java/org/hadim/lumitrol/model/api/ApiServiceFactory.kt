package org.hadim.lumitrol.model.api

import okhttp3.OkHttpClient
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


class ApiServiceFactory {

    var timeout: Long = 10  // seconds

    private fun getClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun create(url: String): ApiService {

        @Suppress("DEPRECATION")
        return Retrofit.Builder()
            .baseUrl(url)
            .client(getClient())
            .addConverterFactory(
                SimpleXmlConverterFactory.createNonStrict(
                    Persister(AnnotationStrategy())
                )
            )
            // TODO: RxJavaCallAdapter doesn't work with SimpleXML.
            //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}