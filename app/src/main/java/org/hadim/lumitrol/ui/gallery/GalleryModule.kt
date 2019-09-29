package org.hadim.lumitrol.ui.gallery

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
abstract class GalleryModule {

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    internal abstract fun bindFactory(factory: GalleryViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    internal abstract fun bindSavedStateRegistryOwner(galleryFragment: GalleryFragment): SavedStateRegistryOwner

    @Module
    companion object {
        @Nullable
        @Provides
        @JvmStatic
        fun provideDefaultArgs(): Bundle? = null
    }
}