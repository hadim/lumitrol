package org.hadim.lumitrol.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife


abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject content
        setContentView(layoutRes())

        // Bind views
        ButterKnife.bind(this)
    }
}