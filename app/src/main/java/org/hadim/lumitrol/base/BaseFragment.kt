package org.hadim.lumitrol.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.snackbar.Snackbar
import org.hadim.lumitrol.utils.forEachChildView


abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(viewModelClass)
    }

    protected abstract val viewModelClass: Class<T>

    @get:LayoutRes
    protected abstract val layoutId: Int

    private var unbinder: Unbinder? = null

    protected lateinit var root: View

    private var errorSnackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Get root view of the fragment/view
        root = inflater.inflate(layoutId, container, false)

        // Bind views
        unbinder = ButterKnife.bind(this, root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }

    protected fun showError(errorMessage: String) {
        errorSnackBar?.dismiss()
        errorSnackBar = Snackbar.make(root, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(getString(org.hadim.lumitrol.R.string.dismiss)) {
                    dismiss()
                }
                show()
            }
    }

    protected fun hideError() {
        errorSnackBar?.dismiss()
    }

    protected fun disableFragment() {
        // TODO: gray out the whole fragment.
        root.forEachChildView { view -> view.isEnabled = false }
    }

    protected fun enableFragment() {
        root.forEachChildView { it.isEnabled = true }
    }

    private fun installErrorObservers() {
        viewModel.repository.networkError.observe(this, Observer {
            if (it != null) {
                showError("Network Error: $it")
            } else {
                hideError()
            }
        })
        viewModel.repository.networkFailure.observe(this, Observer {
            if (it != null) {
                showError("Network Failure: $it")
            } else {
                hideError()
            }
        })
        viewModel.repository.responseError.observe(this, Observer {
            if (it != null) {
                showError("Network Failure: $it")
            } else {
                hideError()
            }
        })

    }
}