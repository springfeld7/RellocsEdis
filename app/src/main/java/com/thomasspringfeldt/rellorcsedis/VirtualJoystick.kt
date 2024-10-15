package com.thomasspringfeldt.rellorcsedis

import android.content.res.Resources
import android.util.Log
import android.view.MotionEvent
import android.view.View

const val MAX_STICK_TRAVEL = 64

/**
 * Virtual joystick.
 * @author Thomas Springfeldt
 */
class VirtualJoystick(view: View) : InputManager() {

    private val tag = "VirtualJoystick"
    protected var maxStickTravel =
        dpToPixels(MAX_STICK_TRAVEL).toFloat() //arbritrary! 96DP = twice the minimum hit target.
    protected var stickCenterX =
        0f //the stick center is placed wherever the user put their finger down
    protected var stickCenterY =
        0f //we can then calculate how far they've slide the stick from that position

    init {
        view.findViewById<View>(R.id.joystick_region)
            .setOnTouchListener(JoystickTouchListener())
        view.findViewById<View>(R.id.button_region)
            .setOnTouchListener(ActionButtonTouchListener())

        Log.d(tag, "Max joystick travel (pixels): $maxStickTravel")
    }

    fun pixelsToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPixels(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private inner class JoystickTouchListener : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_DOWN) {
                stickCenterX = event.getX(0)
                stickCenterY = event.getY(0)
            } else if (action == MotionEvent.ACTION_UP) {
                horizontalFactor = 0.0f
                verticalFactor = 0.0f
            } else if (action == MotionEvent.ACTION_MOVE) {
                //get the proportion to the maxStickTravel
                horizontalFactor = (event.getX(0) - stickCenterX) / maxStickTravel
                verticalFactor = (event.getY(0) - stickCenterY) / maxStickTravel
                clampInputs() //nothing stops the user from pulling the joystick across the entire screen. So lets clamp the inputs. :)
            }
            return true
        }
    }

    private inner class ActionButtonTouchListener : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_DOWN) {
                isJumping = true
            } else if (action == MotionEvent.ACTION_UP) {
                isJumping = false
            }
            return true
        }
    }
}


