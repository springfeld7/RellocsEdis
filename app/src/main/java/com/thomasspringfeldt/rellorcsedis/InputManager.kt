package com.thomasspringfeldt.rellorcsedis

import androidx.core.math.MathUtils.clamp

open class InputManager {

    private val MIN = -1f
    private val MAX = 1f

    var verticalFactor = 0f
    var horizontalFactor = 0f
    var isJumping = false

    protected fun clampInputs() {
        verticalFactor = clamp(verticalFactor, MIN, MAX)
        horizontalFactor = clamp(horizontalFactor, MIN, MAX)
    }

    fun onStart() {}
    fun onStop() {}
    fun onPause() {}
    fun onResume() {}
}