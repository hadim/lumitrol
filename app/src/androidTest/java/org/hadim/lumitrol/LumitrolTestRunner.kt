package org.hadim.lumitrol

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import org.junit.Assert.assertEquals
import org.junit.Test


class LumitrolTestRunner : AndroidJUnitRunner() {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("org.hadim.lumitrol", appContext.packageName)
    }

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, LumitrolApplication::class.java.name, context)
    }
}
