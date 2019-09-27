package org.hadim.lumitrol.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.hadim.lumitrol.ui.main.MainActivity




@Module
abstract class MainActivityBindingModule {

    @ContributesAndroidInjector(modules = [ApplicationModule::class])
    internal abstract fun bindMainActivity(): MainActivity

}
