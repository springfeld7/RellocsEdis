package com.thomasspringfeldt.rellorcsedis

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import kotlin.math.ceil

/**
 * Entity class for static, concrete, sprites.
 * @author Thomas Springfeldt
 */
open class StaticEntity(spriteName: String, x: Float, y: Float) : Entity() {

    var bitmap: Bitmap

    init {
        this.x = x
        this.y = y
        width = 1f
        height = 1f
        bitmap = loadBitMap(spriteName)
    }

    protected fun loadBitMap(spriteName: String): Bitmap {
        val widthInPixels = ceil(engine.worldToScreenX(width))
        val heightInPixels = ceil(engine.worldToScreenY(height))

        return BitmapUtils.loadScaledBitmap(
            engine.context,
            spriteName, widthInPixels.toInt(), heightInPixels.toInt())
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        canvas.drawBitmap(bitmap, transform, paint)
    }
}
