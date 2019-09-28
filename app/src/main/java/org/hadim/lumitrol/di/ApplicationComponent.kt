package org.hadim.lumitrol.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.hadim.lumitrol.model.Repository
import javax.inject.Singleton


@Singleton
@Component(modules = [AssistedInjectModule::class, ApplicationModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    val repositoryFactory: Repository.Factory
}

