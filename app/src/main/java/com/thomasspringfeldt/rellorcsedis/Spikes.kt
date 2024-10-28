package com.thomasspringfeldt.rellorcsedis

const val SPIKES = "spikes_down_brown"
const val SPIKES_BITMAP_OFFSET = 0.6f

/**
 * Spikes, a static enemy.
 * @author Thomas Springfeldt
 */
class Spikes(spriteName: String, x: Float, y: Float) :
    StaticEntity(spriteName, x, y + SPIKES_BITMAP_OFFSET) {

    var damage = 1.0f;

    init {
        height = 0.4f
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
    }
}