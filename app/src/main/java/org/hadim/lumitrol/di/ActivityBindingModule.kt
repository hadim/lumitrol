package org.hadim.lumitrol.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.ui.main.MainActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

}
