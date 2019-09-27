package org.hadim.lumitrol.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import org.hadim.lumitrol.ui.connect.ConnectViewModel
import org.hadim.lumitrol.ui.control.ControlViewModel
import org.hadim.lumitrol.ui.gallery.GalleryViewModel
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    internal fun provideConnectViewModel(): ConnectViewModel {
        return ConnectViewModel()
    }

    @Singleton
    @Provides
    internal fun provideControlViewModel(): ControlViewModel {
        return ControlViewModel()
    }

    @Singleton
    @Provides
    internal fun provideGalleryViewModel(): GalleryViewModel {
        return GalleryViewModel()
    }

}