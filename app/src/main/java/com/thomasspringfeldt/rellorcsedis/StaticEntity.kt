package com.thomasspringfeldt.rellorcsedis

import android.graphics.Bitmap

class StaticEntity(spriteName: String, x: Float, y: Float) : Entity() {
    val bitmap : Bitmap

    init {
        this.x = x
        this.y = y
        width = 1.0f
        height = 1.0f
        val widthInPixel = engine.worldToScreenX(width)
        val heightInPixel = engine.worldToScreenY(height)
    }
}