package com.thomasspringfeldt.rellorcsedis.entities

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.gamedata.GameEvent
import com.thomasspringfeldt.rellorcsedis.input.InputManager

const val PLAYER = "pink_left1"
const val PLAYER_RUN_SPEED = 6f //meters per second
const val PLAYER_JUMP_FORCE: Float = -(GRAVITY / 2f) //whatever feels good!
const val LEFT = 1f
const val RIGHT = -1f
const val IFRAMES_DURATION = 1500
const val BLINK_LENGTH = 150

class Player(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x, y) {

    private var facing = RIGHT
    var health = 3.0f
    var maxHealth = 3.0f
    var jumpForce = PLAYER_JUMP_FORCE
    var isInvincible = false
    var iFramesIsActive = false
    var iFramesTimer : Long = 0
    var isBlinking = false
    var blinkTimer : Long = 0
    private var startPosX = 0f
    private var startPosY = 0f


    init {
        startPosX = x
        startPosY = y
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
            velY = jumpForce
            engine.onGameEvent(GameEvent.Jump, this)
        }

        if (iFramesIsActive) {
            handleIFrames(IFRAMES_DURATION)
        }

        super.update(dt) //parent will integrate our velocity and time with our position
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        transform.preScale(facing, 1f)
        if (facing == RIGHT) {
            val offset = engine.worldToScreenX(width)
            transform.postTranslate(offset, 0.0f)
        }
        if (!isBlinking) {super.render(canvas, transform, paint)}
    }

    override fun onCollision(that: Entity) {

        if (!iFramesIsActive && !isInvincible) {
            if (that is Spikes) {
                engine.onGameEvent(GameEvent.TakeDamage, this)
                health -= that.damage
                iFramesIsActive = true
                iFramesTimer = System.currentTimeMillis()
                isBlinking = true
                blinkTimer = System.currentTimeMillis()
            }
        }

        super.onCollision(that)
    }

    fun respawn() {
        x = startPosX
        y = startPosY
        health = maxHealth
        iFramesIsActive = false
        isBlinking = false
    }

    private fun getFacingDirection(direction: Float): Float {
        if (direction < 0f) {
            return LEFT
        } else if (direction > 0f) {
            return RIGHT
        }
        return facing
    }

    fun handleIFrames(duration: Int) {
        if (iFramesIsActive && System.currentTimeMillis() - iFramesTimer > duration) {
            iFramesIsActive = false
            isBlinking = false
        }
        if (System.currentTimeMillis() - blinkTimer >= BLINK_LENGTH) {
            isBlinking = !isBlinking
            blinkTimer = System.currentTimeMillis()
        }
    }
}
