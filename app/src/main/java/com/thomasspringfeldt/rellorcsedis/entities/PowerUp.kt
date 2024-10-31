package com.thomasspringfeldt.rellorcsedis.entities

/**
 * Base class for PowerUps.
 * @author Thomas Springfeldt
 */
abstract class PowerUp(spriteName: String, x: Float, y: Float, val duration: Int) : DynamicEntity(spriteName, x, y) {


}