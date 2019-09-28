package org.hadim.lumitrol

import android.app.Activity
import android.app.Application
import org.hadim.lumitrol.di.ApplicationComponent
import org.hadim.lumitrol.di.DaggerApplicationComponent


class LumitrolApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }
}

val Activity.component get() = (application as LumitrolApplication).component
