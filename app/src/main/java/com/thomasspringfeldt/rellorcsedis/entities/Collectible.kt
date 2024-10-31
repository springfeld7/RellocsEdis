package com.thomasspringfeldt.rellorcsedis.entities

/**
 * Base class for collectibles.
 * @author Thomas Springfeldt
 */
abstract class Collectible(spriteName: String, x: Float, y: Float) : DynamicEntity(spriteName, x , y) {

    var isDead = false

}