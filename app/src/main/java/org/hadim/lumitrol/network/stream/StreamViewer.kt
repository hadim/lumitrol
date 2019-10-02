package org.hadim.lumitrol.network.stream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceView


/*
 * From https://github.com/silaslenz/Lumimote/blob/21ba1bf00ae5d39f7d583a3bb7327182a8371957/app/src/main/java/se/silenz/lumimote/ViewFinderView.kt
 */
class StreamViewer(context: Context?, st: AttributeSet) : SurfaceView(context, st) {

    private var currentImage: Bitmap? = null

    private var viewWidth = 100
    private var viewHeight = 0

    init {
        setWillNotDraw(false)
        addOnLayoutChangeListener { _, left, _, right, _, _, _, _, _ ->
            viewWidth = right - left
            resize()
        }
        resize()
    }

    fun setCurrentImage(bitmap: Bitmap?) {
        bitmap?.let { it ->
            this.post {
                // Compute the height of the view once we know the bitmap's width and height
                if (viewHeight == 0) {
                    val factor = viewWidth.toFloat() / it.width.toFloat()
                    viewHeight = (it.height * factor).toInt()
                    resize()
                }
                currentImage = Bitmap.createScaledBitmap(it, viewWidth, viewHeight, true)
                invalidate()
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        currentImage?.let { currentImage ->
            canvas.drawBitmap(currentImage, 0f, 0f, Paint())
        }
    }

    private fun resize() {
        this.layoutParams = android.widget.FrameLayout.LayoutParams(viewWidth, viewHeight)
    }


}