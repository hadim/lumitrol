package org.hadim.lumitrol.ui.connect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hadim.lumitrol.R
import org.hadim.lumitrol.base.BaseFragment
import javax.inject.Inject

class ConnectFragment : BaseFragment() {

    companion object {
        const val TAG: String = "ConnectFragment"
    }

    @Inject
    lateinit var connectViewModel: ConnectViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_connect
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = super.onCreateView(inflater, container, savedInstanceState)

        Log.d("$TAG/onCreateView", "Init ConnectFragment")

        return root
    }
}