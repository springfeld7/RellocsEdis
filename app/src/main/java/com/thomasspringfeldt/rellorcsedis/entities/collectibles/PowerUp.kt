package com.thomasspringfeldt.rellorcsedis.entities.collectibles

import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.Player
import com.thomasspringfeldt.rellorcsedis.gamedata.GameEvent

const val POWERUP_SIDE = 0.5f
const val POWER_UP_TILE_OFFSET = 0.5f - POWERUP_SIDE / 2

/**
 * Base class for PowerUps.
 * @author Thomas Springfeldt
 */
abstract class PowerUp(spriteName: String, x: Float, y: Float, val pwrUpDuration: Int) : Collectible(spriteName, x + POWER_UP_TILE_OFFSET, y) {

    lateinit var owner: Player
    var timer: Long = 0
    var isActive = false

    init {
        height = POWERUP_SIDE
        width = POWERUP_SIDE
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun onCollision(that: Entity) {
        if (that is Player) {
            engine.onGameEvent(GameEvent.PowerUpPickup, this)
            timer = System.currentTimeMillis()
            y = engine.levelHeight()*2
        }
        super.onCollision(that)
    }
}