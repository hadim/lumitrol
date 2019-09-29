package org.hadim.lumitrol.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<T : ViewModel> : DaggerAppCompatActivity() {

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(viewModelClass)
    }

    protected abstract val viewModelClass: Class<T>

    @get:LayoutRes
    protected abstract val layoutId: Int

    private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId)

        // Bind views
        ButterKnife.bind(this)
    }
}