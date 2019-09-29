package org.hadim.lumitrol.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.di.ActivityScope
import org.hadim.lumitrol.ui.connect.ConnectFragmentModule
import org.hadim.lumitrol.ui.control.ControlFragmentModule
import org.hadim.lumitrol.ui.gallery.GalleryFragmentModule

@Suppress("unused")
@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @Module(
        includes = [
            ConnectFragmentModule::class,
            ControlFragmentModule::class,
            GalleryFragmentModule::class
        ]
    )
    internal abstract class MainModule

}