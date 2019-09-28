package org.hadim.lumitrol.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.ui.connect.ConnectFragment
import org.hadim.lumitrol.ui.control.ControlFragment
import org.hadim.lumitrol.ui.gallery.GalleryFragment


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    internal abstract fun provideConnectFragment(): ConnectFragment

    @ContributesAndroidInjector
    internal abstract fun provideControlFragment(): ControlFragment

    @ContributesAndroidInjector
    internal abstract fun provideGalleryFragment(): GalleryFragment
}