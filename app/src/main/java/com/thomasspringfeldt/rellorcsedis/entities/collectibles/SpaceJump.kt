package com.thomasspringfeldt.rellorcsedis.entities.collectibles

import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.PLAYER
import com.thomasspringfeldt.rellorcsedis.entities.Player

const val SUPER_JUMP = "super_jump_pwrup"
const val SUPER_JUMP_SUIT = "white_left1"
const val SUPER_JUMP_DURATION = 10000
const val SUPER_JUMP_MODIFIER = 2f


/**
 * PowerUp that makes the player jump really high.
 * @author Thomas Springfeldt
 */
class SpaceJump(spriteName: String, x: Float, y: Float, ) : PowerUp(spriteName, x, y, SUPER_JUMP_DURATION) {

    override fun update(deltaTime: Float) {
        if (isActive && System.currentTimeMillis() - timer >= SUPER_JUMP_DURATION) {
            owner.jumpForce /= SUPER_JUMP_MODIFIER
            owner.bitmap = engine.bitmapPool.createBitmap(PLAYER, owner.width, owner.height)
            isDead = true
        }
        super.update(deltaTime)
    }

    override fun onCollision(that: Entity) {
        if (that is Player) {
            isActive = true
            owner = that
            owner.jumpForce *= SUPER_JUMP_MODIFIER
            owner.bitmap = engine.bitmapPool.createBitmap(SUPER_JUMP_SUIT, owner.width, owner.height)
        }
        super.onCollision(that)
    }
}