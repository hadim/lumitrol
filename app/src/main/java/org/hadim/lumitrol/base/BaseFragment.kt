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
import org.hadim.lumitrol.R


abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(viewModelClass)
    }

    protected abstract val viewModelClass: Class<T>

    @get:LayoutRes
    protected abstract val layoutId: Int

    private var unbinder: Unbinder? = null

    private lateinit var root: View

    private var errorSnackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Get root view of the fragment/view
        root = inflater.inflate(layoutId, container, false)

        // Bind views
        unbinder = ButterKnife.bind(this, root)

        installErrorObservers()

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
        errorSnackBar = Snackbar.make(root, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(getString(R.string.dismiss)) {
                    dismiss()
                }
                show()
            }
    }

    protected fun hideError() {
        errorSnackBar?.dismiss()
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