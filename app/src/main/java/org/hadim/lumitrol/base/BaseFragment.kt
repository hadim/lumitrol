package org.hadim.lumitrol.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder


abstract class BaseFragment : Fragment() {

    private var unbinder: Unbinder? = null
    private var baseActivity: AppCompatActivity? = null

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Get root view of the fragment/view
        val root = inflater.inflate(layoutRes(), container, false)

        // Bind views
        unbinder = ButterKnife.bind(this, root)

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as AppCompatActivity
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }
}