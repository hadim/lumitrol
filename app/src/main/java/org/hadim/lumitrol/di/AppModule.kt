package org.hadim.lumitrol.di

import android.app.Application
import dagger.Module
import dagger.Provides
import org.hadim.lumitrol.App
import org.hadim.lumitrol.model.Repository
import org.hadim.lumitrol.model.api.ApiServiceFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
class AppModule {

    @Provides
    fun provideApplication(application: App): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideApiServiceFactory(): ApiServiceFactory {
        return ApiServiceFactory()
    }

    @Singleton
    @Provides
    fun provideRepository(apiServiceFactory: ApiServiceFactory): Repository {
        return Repository(apiServiceFactory)
    }
}
