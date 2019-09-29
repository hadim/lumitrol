package org.hadim.lumitrol.ui.control

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.hadim.lumitrol.di.viewmodel.ViewModelAssistedFactory
import org.hadim.lumitrol.di.viewmodel.ViewModelKey


@Suppress("unused")
@Module
abstract class ControlModule {

    @Binds
    @IntoMap
    @ViewModelKey(ControlViewModel::class)
    internal abstract fun bindFactory(factory: ControlViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    internal abstract fun bindSavedStateRegistryOwner(controlFragment: ControlFragment): SavedStateRegistryOwner

    @Module
    companion object {
        @Nullable
        @Provides
        @JvmStatic
        fun provideDefaultArgs(): Bundle? = null
    }
}