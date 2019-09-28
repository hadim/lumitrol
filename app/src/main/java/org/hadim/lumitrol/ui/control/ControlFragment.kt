package org.hadim.lumitrol.ui.control

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment

class ControlFragment : BaseFragment() {

    companion object {
        const val TAG: String = "ControlFragment"
    }

//    @Inject
//    lateinit var controlViewModel: ControlViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_control
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = super.onCreateView(inflater, container, savedInstanceState)

        Log.d("$TAG/onCreateView", "Init ControlFragment")

        return root
    }
}