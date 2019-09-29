package org.hadim.lumitrol.ui.gallery

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.di.FragmentScope


@Suppress("unused")
@Module
abstract class GalleryFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [GalleryModule::class])
    abstract fun contributeGalleryFragmentInjector(): GalleryFragment
}