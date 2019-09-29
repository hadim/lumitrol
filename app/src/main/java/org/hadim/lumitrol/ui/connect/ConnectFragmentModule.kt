package org.hadim.lumitrol.ui.connect

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.di.FragmentScope


@Suppress("unused")
@Module
abstract class ConnectFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [ConnectModule::class])
    abstract fun contributeConnectFragmentInjector(): ConnectFragment
}