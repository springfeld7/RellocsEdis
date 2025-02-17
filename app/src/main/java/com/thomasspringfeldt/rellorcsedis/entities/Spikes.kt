package com.thomasspringfeldt.rellorcsedis.entities

import com.thomasspringfeldt.rellorcsedis.engine

const val SPIKES = "spikes_down_brown"
const val SPIKES_TILE_OFFSET = 0.6f

/**
 * Spikes, a static enemy.
 * @author Thomas Springfeldt
 */
class Spikes(spriteName: String, x: Float, y: Float) :
    StaticEntity(spriteName, x, y + SPIKES_TILE_OFFSET) {

    var damage = 1.0f

    init {
        height = 0.4f
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }
}