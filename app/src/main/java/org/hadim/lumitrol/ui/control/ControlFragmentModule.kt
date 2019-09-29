package org.hadim.lumitrol.ui.control

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.di.FragmentScope


@Suppress("unused")
@Module
abstract class ControlFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [ControlModule::class])
    abstract fun contributeControlFragmentInjector(): ControlFragment
}