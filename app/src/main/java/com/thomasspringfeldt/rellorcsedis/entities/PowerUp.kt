package com.thomasspringfeldt.rellorcsedis.entities

import com.thomasspringfeldt.rellorcsedis.engine

const val POWER_UP_SIDE_RATIO = 0.5f
const val POWER_UP_TILE_OFFSET = 0.5f - POWER_UP_SIDE_RATIO / 2

/**
 * Base class for PowerUps.
 * @author Thomas Springfeldt
 */
abstract class PowerUp(spriteName: String, x: Float, y: Float, val duration: Int) : Collectible(spriteName, x + POWER_UP_TILE_OFFSET, y) {

    init {
        height = 0.5f
        width = 0.5f
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

}