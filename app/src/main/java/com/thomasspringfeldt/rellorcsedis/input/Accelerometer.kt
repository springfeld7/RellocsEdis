package com.thomasspringfeldt.rellorcsedis.input

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.Surface
import androidx.core.math.MathUtils.clamp
import com.thomasspringfeldt.rellorcsedis.MainActivity
import kotlin.math.sqrt

const val MAX_ANGLE = 30
const val LENGTH = 3 //azimuth (z), pitch (z), roll (y)
const val DEGREES_PER_RADIAN = 57.2957795f
const val SHAKE_THRESHOLD = 3.25f // m/S^2
const val SHAKE_COOLDOWN: Long = 300 //ms

/**
 * Accelerometered game input.
 * @author Thomas Springfeldt
 */
class Accelerometer(val activity: MainActivity) : InputManager() {

    private val lastAccels = FloatArray(LENGTH)
    private val lastMagFields = FloatArray(LENGTH)
    private var lastShake: Long = 0 //avoid constant jumping for minor movements of the device
    private val rotationMatrix = FloatArray(4 * 4)
    private val orientation = FloatArray(LENGTH)
    private val rotation: Int //default orientation of the device

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            rotation = activity.display?.rotation ?: 0
        } else {
            rotation = activity.windowManager.defaultDisplay.rotation
        }
    }

    private fun registerListeners() {
        val sm = activity.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sm.registerListener(
            accelerometerListener,
            sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
        sm.registerListener(
            magneticListener,
            sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    private fun unregisterListeners() {
        val sm = activity.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sm.unregisterListener(accelerometerListener)
        sm.unregisterListener(magneticListener)
    }

    private val accelerometerListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            System.arraycopy(event.values, 0, lastAccels, 0, LENGTH)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private val magneticListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            System.arraycopy(event.values, 0, lastMagFields, 0, LENGTH)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun getHorizontalAxis(): Float {
        if (!SensorManager.getRotationMatrix(rotationMatrix, null, lastAccels, lastMagFields)) {
            // Case for devices that DO NOT have magnetic sensors
            if (rotation == Surface.ROTATION_0) {
                return -lastAccels[0] * 5
            } else {
                return -lastAccels[1] * -5
            }
        } else { //we have a geomagnetic sensor and are not in free fall! Jay! :D
            if (rotation == Surface.ROTATION_0) {
                SensorManager.remapCoordinateSystem(
                    rotationMatrix,
                    SensorManager.AXIS_Y,
                    SensorManager.AXIS_MINUS_X,
                    rotationMatrix
                )
                SensorManager.getOrientation(rotationMatrix, orientation)
                return orientation[1] * DEGREES_PER_RADIAN
            } else {
                SensorManager.getOrientation(rotationMatrix, orientation)
                return -orientation[1] * DEGREES_PER_RADIAN
            }
        }
    }

    private fun isJumpingAccel(): Boolean {
        if (System.currentTimeMillis() - lastShake < SHAKE_COOLDOWN) {
            return isJumping //return the old value until cooldown time has passed.
        }
        val x = lastAccels[0]
        val y = lastAccels[1]
        val z = lastAccels[2]
        val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
        if (acceleration > SHAKE_THRESHOLD) {
            lastShake = System.currentTimeMillis()
            return true
        }
        return false
    }

    override fun update(dt: Float) {
        horizontalFactor = clamp(getHorizontalAxis() / MAX_ANGLE, getMinInput(), getMaxInput())
        verticalFactor = 0.0f
        isJumping = isJumpingAccel()
    }

    override fun onResume() {
        registerListeners()
    }

    override fun onPause() {
        unregisterListeners()
    }
}