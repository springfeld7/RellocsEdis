package com.thomasspringfeldt.rellorcsedis.entities.collectibles

import com.thomasspringfeldt.rellorcsedis.entities.DynamicEntity

/**
 * Base class for collectibles.
 * @author Thomas Springfeldt
 */
abstract class Collectible(spriteName: String, x: Float, y: Float) : DynamicEntity(spriteName, x , y) {

    var isDead = false

}