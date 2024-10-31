package com.thomasspringfeldt.rellorcsedis.entities

import androidx.core.math.MathUtils.clamp
import com.thomasspringfeldt.rellorcsedis.engine

const val MAX_DELTA = 0.49f
const val GRAVITY = 40f

/**
 * Entity that can move.
 * @author Thomas Springfeldt
 */
open class DynamicEntity(spriteName: String, x: Float, y: Float) : StaticEntity(spriteName, x, y) {

    var velX = 0f
    var velY = 0f
    var isOnGround = false

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        moveHorizontally(deltaTime)
        moveVertically(deltaTime)
        isOnGround = false
    }

    override fun onCollision(that: Entity) {
        val overlap = getOverlap(this, that) ?: return
        if (overlap.x != 0f) {
            x += overlap.x
            velX = 0f
        }
        if (overlap.y != 0f) {
            y += overlap.y
            velY = 0f
            isOnGround = (overlap.y < 0)
        }
    }

    private fun moveVertically(deltaTime: Float) {
        if (!isOnGround) {
            velY += GRAVITY * deltaTime
        }
        val deltaY = clamp(velY * deltaTime, -MAX_DELTA, MAX_DELTA)
        y += deltaY
        if (top > engine.levelHeight()) {
            if (this is Player) {
                health = 0f
            }
        }
    }

    private fun moveHorizontally(deltaTime: Float) {
        val deltaX = clamp(velX * deltaTime, -MAX_DELTA, MAX_DELTA)
        x += deltaX
    }
}