package org.hadim.lumitrol

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.hadim.lumitrol.di.DaggerAppComponent

class App : DaggerApplication() {
    private val component: AndroidInjector<App> by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return component
    }

}
