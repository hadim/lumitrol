package org.hadim.lumitrol.ui.connect

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
abstract class ConnectModule {

    @Binds
    @IntoMap
    @ViewModelKey(ConnectViewModel::class)
    internal abstract fun bindFactory(factory: ConnectViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    internal abstract fun bindSavedStateRegistryOwner(connectFragment: ConnectFragment): SavedStateRegistryOwner

    @Module
    companion object {
        @Nullable
        @Provides
        @JvmStatic
        fun provideDefaultArgs(): Bundle? = null
    }
}