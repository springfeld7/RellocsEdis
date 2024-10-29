package com.thomasspringfeldt.rellorcsedis

const val COIN = "coin_yellow"
const val COIN_SIDE_RATIO = 0.4f
const val COIN_TILE_OFFSET = 0.5f - COIN_SIDE_RATIO / 2

/**
 * Coin collectible.
 * @author Thomas Springfeldt
 */
class Coin(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x + COIN_TILE_OFFSET, y) {

    init {
        height = COIN_SIDE_RATIO
        width = COIN_SIDE_RATIO
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

}