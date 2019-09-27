package org.hadim.lumitrol

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import org.hadim.lumitrol.di.DaggerApplicationComponent


class LumitrolApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)

        return component
    }

}

