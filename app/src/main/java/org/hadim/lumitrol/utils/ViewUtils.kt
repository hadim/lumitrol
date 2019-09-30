package org.hadim.lumitrol.utils

import android.view.View
import android.view.ViewGroup

fun View.forEachChildView(closure: (View) -> Unit) {
    closure(this)
    val groupView = this as? ViewGroup ?: return
    val size = groupView.childCount - 1
    for (i in 0..size) {
        groupView.getChildAt(i).forEachChildView(closure)
    }
}
