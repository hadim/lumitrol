package org.hadim.lumitrol.stream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceView


/*
 * From https://github.com/silaslenz/Lumimote/blob/21ba1bf00ae5d39f7d583a3bb7327182a8371957/app/src/main/java/se/silenz/lumimote/ViewFinderView.kt
 */
class StreamViewer(context: Context?, st: AttributeSet) : SurfaceView(context, st) {

    private var currentImage: Bitmap? = null
    private var boxes: Array<Rect> = arrayOf()

    private var viewFinderWidth = 100
    private var viewFinderHeight = 0

    init {
        setWillNotDraw(false)
        addOnLayoutChangeListener { _, left, _, right, _, _, _, _, _ ->
            viewFinderWidth = right - left
        }
    }

    fun setCurrentImage(bitmap: Bitmap?) {
        if (bitmap == null)
            return
        currentImage = bitmap
        // compute the height of the view once we know the bitmap's width and height
        if (viewFinderHeight == 0) {
            val factor = viewFinderWidth.toFloat() / bitmap.width.toFloat()
            viewFinderHeight = (currentImage!!.height * factor).toInt()
        }
        currentImage = Bitmap.createScaledBitmap(currentImage!!, viewFinderWidth, viewFinderHeight, false)
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (currentImage != null) {
            canvas.drawBitmap(currentImage!!, 0f, 0f, Paint())
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.strokeWidth = 2f
        paint.color = android.graphics.Color.YELLOW
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        for (box in boxes) {
            canvas.drawRect(box, paint)
        }
    }


}