package com.thomasspringfeldt.rellorcsedis

import androidx.core.math.MathUtils.clamp

const val MAX_DELTA = 0.49f
const val GRAVITY = 40f

/**
 * Entity that can move.
 * @author Thomas Springfeldt
 */
class DynamicEntity(spriteName: String, x: Float, y: Float) : StaticEntity(spriteName, x, y) {

    var velX = 0f
    var velY = 0f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        moveHorizontally(deltaTime)
        moveVertically(deltaTime)
    }

    private fun moveVertically(deltaTime: Float) {
        velY += GRAVITY * deltaTime
        val deltaY = clamp(velY * deltaTime, -MAX_DELTA, MAX_DELTA)
        y += deltaY
        if (top > engine.levelHeight()) {
            bottom = 1f
        }
    }

    private fun moveHorizontally(deltaTime: Float) {
        val deltaX = clamp(velX * deltaTime, -MAX_DELTA, MAX_DELTA)
        x += deltaX
    }
}