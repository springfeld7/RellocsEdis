package com.thomasspringfeldt.rellorcsedis

const val PLAYER_RUN_SPEED = 6f //meters per second
val PLAYER_JUMP_FORCE: Float = -(GRAVITY / 2f) //whatever feels good!
const val LEFT = 1f
const val RIGHT = -1f

class Player(spriteName: String, x: Float, y: Float) :
    DynamicEntity(spriteName, x, y) {

    init {
        width = 0.9f
        height = 0.9f
        bitmap = loadBitMap(spriteName)
    }

    override fun update(dt: Float) {
        val controls: InputManager = engine.getControls()
        val direction: Float = controls.horizontalFactor
        velX = direction * PLAYER_RUN_SPEED

        if (controls.isJumping && isOnGround) {
            velY = PLAYER_JUMP_FORCE
        }
        super.update(dt) //parent will integrate our velocity and time with our position
    }
}
