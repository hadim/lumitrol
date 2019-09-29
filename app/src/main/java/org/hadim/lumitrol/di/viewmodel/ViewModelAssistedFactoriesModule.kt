package org.hadim.lumitrol.di.viewmodel

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module


@AssistedModule
@Module(includes = [AssistedInject_ViewModelAssistedFactoriesModule::class])
object ViewModelAssistedFactoriesModule
