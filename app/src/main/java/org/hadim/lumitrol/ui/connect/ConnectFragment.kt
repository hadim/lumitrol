package org.hadim.lumitrol.ui.connect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.hadim.lumitrol.R
import org.hadim.lumitrol.ui.control.ControlViewModel

class ConnectFragment : Fragment() {

    private lateinit var controlViewModel: ControlViewModel

    fun layoutRes(): Int {
        return R.layout.fragment_connect
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //var root = super.onCreateView(inflater, container, savedInstanceState)
        controlViewModel =
            ViewModelProviders.of(this).get(ControlViewModel::class.java)
        val root = inflater.inflate(layoutRes(), container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        controlViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}