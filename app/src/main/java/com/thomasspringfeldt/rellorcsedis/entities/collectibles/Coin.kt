package com.thomasspringfeldt.rellorcsedis.entities.collectibles

import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.Player
import com.thomasspringfeldt.rellorcsedis.gamedata.GameEvent

const val COIN = "coin_yellow"
const val COIN_SIDE = 0.4f
const val COIN_TILE_OFFSET = 0.5f - COIN_SIDE / 2

/**
 * Coin collectible.
 * @author Thomas Springfeldt
 */
class Coin(spriteName: String, x: Float, y: Float) :
    Collectible(spriteName, x + COIN_TILE_OFFSET, y) {

    init {
        height = COIN_SIDE
        width = COIN_SIDE
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun onCollision(that: Entity) {
        if (that is Player) {
            engine.onGameEvent(GameEvent.CoinPickup, this)
            isDead = true
        }
        super.onCollision(that)
    }

}