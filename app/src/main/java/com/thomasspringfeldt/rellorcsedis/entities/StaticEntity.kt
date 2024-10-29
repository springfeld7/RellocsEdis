package com.thomasspringfeldt.rellorcsedis.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.thomasspringfeldt.rellorcsedis.engine

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
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        canvas.drawBitmap(bitmap, transform, paint)
    }
}
