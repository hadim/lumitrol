package org.hadim.lumitrol.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import org.hadim.lumitrol.App
import org.hadim.lumitrol.di.viewmodel.ViewModelAssistedFactoriesModule
import org.hadim.lumitrol.ui.main.MainActivityModule
import javax.inject.Singleton

@Suppress("unused")
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class,
        ViewModelAssistedFactoriesModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<App>

}
