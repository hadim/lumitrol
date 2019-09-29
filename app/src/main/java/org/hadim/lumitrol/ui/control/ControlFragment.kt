package org.hadim.lumitrol.ui.control

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment

class ControlFragment : BaseFragment<ControlViewModel>() {

    companion object {
        const val TAG: String = "ControlFragment"
    }

    override val viewModelClass: Class<ControlViewModel> = ControlViewModel::class.java
    override val layoutId: Int = R.layout.fragment_control

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        Log.d("$TAG/onCreateView", "Init ControlFragment")

        return view
    }
}