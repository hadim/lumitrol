package org.hadim.lumitrol.di

import dagger.Module
import dagger.Provides
import org.hadim.lumitrol.model.Repository
import org.hadim.lumitrol.ui.connect.ConnectViewModel
import org.hadim.lumitrol.ui.control.ControlViewModel
import org.hadim.lumitrol.ui.gallery.GalleryViewModel
import javax.inject.Singleton


@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideConnectViewModel(repository: Repository): ConnectViewModel =
        ConnectViewModel(repository)

    @Singleton
    @Provides
    fun provideControlViewModel(repository: Repository): ControlViewModel =
        ControlViewModel(repository)

    @Singleton
    @Provides
    fun provideGalleryViewModel(repository: Repository): GalleryViewModel =
        GalleryViewModel(repository)
}