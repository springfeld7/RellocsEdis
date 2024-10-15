package com.thomasspringfeldt.rellorcsedis

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent

/**
 * Class for handling physical gamepads.
 * @author Thomas Springfeldt
 */
class Gamepad(val activity: MainActivity) : InputManager(), GamepadListener {

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if (event.source and InputDevice.SOURCE_JOYSTICK != InputDevice.SOURCE_JOYSTICK) {
            return false //we don't consume this event
        }
        horizontalFactor = getInputFactor(event, MotionEvent.AXIS_X, MotionEvent.AXIS_HAT_X)
        verticalFactor = getInputFactor(event, MotionEvent.AXIS_Y, MotionEvent.AXIS_HAT_Y)
        clampInputs()
        return true //we did consume this event
    }

    private fun getInputFactor(event: MotionEvent, axis: Int, fallbackAxis: Int): Float {
        val device = event.device
        val source = event.source
        var result = event.getAxisValue(axis)
        var range = device.getMotionRange(axis, source)
        if (Math.abs(result) <= range.flat) {
            result = event.getAxisValue(fallbackAxis)
            range = device.getMotionRange(fallbackAxis, source)
            if (Math.abs(result) <= range.flat) {
                result = 0.0f
            }
        }
        return result
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        val keyCode = event.keyCode
        var wasConsumed = false

        if (action == MotionEvent.ACTION_DOWN) { // User started pressing a button
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                verticalFactor -= 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                verticalFactor += 1.0f
                wasConsumed = true
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                horizontalFactor -= 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                horizontalFactor += 1.0f
                wasConsumed = true
            }
            if (isJumpKey(keyCode)) {
                isJumping = true
                wasConsumed = true
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                verticalFactor += 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                verticalFactor -= 1.0f
                wasConsumed = true
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                horizontalFactor += 1.0f
                wasConsumed = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                horizontalFactor -= 1.0f
                wasConsumed = true
            }
            if (isJumpKey(keyCode)) {
                isJumping = false
                wasConsumed = true
            }
            if (keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                activity.onBackPressed() //backwards comp
            }
        }
        return wasConsumed
    }

    fun isJumpKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_BUTTON_A || keyCode == KeyEvent.KEYCODE_BUTTON_X || keyCode == KeyEvent.KEYCODE_BUTTON_Y
    }

    override fun onStart() {
        super.onStart()
        activity.setGamepadListener(this)
    }

    override fun onStop() {
        activity.setGamepadListener(null)
    }


}