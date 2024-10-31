package com.thomasspringfeldt.rellorcsedis.entities

const val INVINCIBILITY = "invincibility_pwrup"
const val INVINCIBILITY_DURATION = 10000

/**
 * PowerUp that makes the player jump really high.
 * @author Thomas Springfeldt
 */
class Invincibility(spriteName: String, x: Float, y: Float) : PowerUp(spriteName, x, y, INVINCIBILITY_DURATION) {



    override fun onCollision(that: Entity) {

        super.onCollision(that)
    }
}