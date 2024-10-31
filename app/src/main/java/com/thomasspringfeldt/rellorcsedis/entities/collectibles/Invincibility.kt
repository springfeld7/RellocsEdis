package com.thomasspringfeldt.rellorcsedis.entities.collectibles

import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.Player

const val INVINCIBILITY = "invincibility_pwrup"
const val INVINCIBILITY_DURATION = 10000

/**
 * PowerUp that makes the player jump really high.
 * @author Thomas Springfeldt
 */
class Invincibility(spriteName: String, x: Float, y: Float) : PowerUp(spriteName, x, y, INVINCIBILITY_DURATION) {

    override fun update(deltaTime: Float) {

        super.update(deltaTime)
    }

    override fun onCollision(that: Entity) {
        if (that is Player) {
            isActive = true
            owner = that
        }

        super.onCollision(that)
    }
}