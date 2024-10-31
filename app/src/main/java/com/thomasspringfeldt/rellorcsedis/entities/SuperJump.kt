package com.thomasspringfeldt.rellorcsedis.entities

import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.gamedata.GameEvent

const val SUPER_JUMP = "super_jump_pwrup"
const val SUPER_JUMP_DURATION = 10000

/**
 * PowerUp that makes the player jump really high.
 * @author Thomas Springfeldt
 */
class SuperJump(spriteName: String, x: Float, y: Float) : PowerUp(spriteName, x, y, SUPER_JUMP_DURATION) {

    override fun update(dt: Float) {

    }

    override fun onCollision(that: Entity) {
        engine.onGameEvent(GameEvent.PowerUpPickup, this)
        super.onCollision(that)
    }
}