package com.thomasspringfeldt.rellorcsedis.input

import androidx.core.math.MathUtils.clamp

const val MIN_INPUT = -1f
const val MAX_INPUT = 1f

open class InputManager {

    var verticalFactor = 0f
    var horizontalFactor = 0f
    var isJumping = false

    protected fun clampInputs() {
        verticalFactor = clamp(verticalFactor, MIN_INPUT, MAX_INPUT)
        horizontalFactor = clamp(horizontalFactor, MIN_INPUT, MAX_INPUT)
    }

    fun getMinInput(): Float {
        return MIN_INPUT
    }

    fun getMaxInput(): Float {
        return MAX_INPUT
    }

    open fun update(deltaTime: Float) {}
    open fun onStart() {}
    open fun onStop() {}
    open fun onPause() {}
    open fun onResume() {}
}