package org.hadim.lumitrol.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import org.hadim.lumitrol.model.api.ApiServiceFactory
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton


@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    @Suppress("DEPRECATION")
    fun provideRetrofitFactory(): ApiServiceFactory {

        val builder = Retrofit.Builder()
            .addConverterFactory(
                SimpleXmlConverterFactory.createNonStrict(
                    Persister(AnnotationStrategy())
                )
            )
            // TODO: RxJavaCallAdapter doesn't work with SimpleXML.
            //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        return ApiServiceFactory((builder))
    }

}