package com.thomasspringfeldt.rellorcsedis

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

const val PLAYER = "pink_left1"
const val PLAYER_RUN_SPEED = 6f //meters per second
const val PLAYER_JUMP_FORCE: Float = -(GRAVITY / 2f) //whatever feels good!
const val LEFT = 1f
const val RIGHT = -1f

class Player(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x, y) {

    private var facing = RIGHT
    private var health = 3.0f

    init {
        width = 0.9f
        height = 0.9f
        engine.bitmapPool.remove(bitmap)
        bitmap = engine.bitmapPool.createBitmap(spriteName, width, height)
    }

    override fun update(dt: Float) {
        val controls: InputManager = engine.getControls()
        val direction: Float = controls.horizontalFactor
        velX = direction * PLAYER_RUN_SPEED
        facing = getFacingDirection(direction)

        if (controls.isJumping && isOnGround) {
            velY = PLAYER_JUMP_FORCE
            engine.onGameEvent(GameEvent.Jump, this)
        }
        super.update(dt) //parent will integrate our velocity and time with our position
    }

    private fun getFacingDirection(direction: Float): Float {
        if (direction < 0f) {
            return LEFT
        } else if (direction > 0f) {
            return RIGHT
        }
        return facing
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        transform.preScale(facing, 1f)
        if (facing == RIGHT) {
            val offset = engine.worldToScreenX(width)
            transform.postTranslate(offset, 0.0f)
        }
        super.render(canvas, transform, paint)
    }

    override fun onCollision(that: Entity) {

        //Remove comment when Coin is implemented
        /*
        if(that is Coin) {
            engine.onGameEvent(GameEvent.CoinPickup, this)
            //the Game might count a score, play a sound effect, or render a graphical effect.
            //the Coin's onCollision should set isDead = true, so the entity will stop updating,
            // stop rendering, and can be removed at the end of the frame
        }
        */
        super.onCollision(that)
    }

}
