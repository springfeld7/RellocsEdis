package com.thomasspringfeldt.rellorcsedis.rendering

import android.graphics.PointF
import android.graphics.RectF
import androidx.core.math.MathUtils.clamp
import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.isColliding
import kotlin.math.round

/**
 * Game camera.
 * @author Ulf Benjaminsson
 */
class Viewport(
    private val screenWidth: Int,
    private val screenHeight: Int,
    var metersToShowX: Float,
    var metersToShowY: Float
) : Entity() {

    private var pixelsPerMeterX: Float = 0f
    private var pixelsPerMeterY: Float = 0f
    private val screenCenterX = screenWidth / 2
    private val screenCenterY = screenHeight / 2
    private var bounds: RectF = RectF()

    init {
        require(metersToShowX > 0f || metersToShowY > 0f) { "One of the dimensions must be provided!" }
        setMetersToShow(metersToShowX, metersToShowY)
        lookAt(2f, 0f) //default view target
    }

    fun setMetersToShow(metersToShowX: Float, metersToShowY: Float) {
        width = metersToShowX.takeIf { it > 0f } ?: (screenWidth.toFloat() / screenHeight * metersToShowY)
        height = metersToShowY.takeIf { it > 0f } ?: (screenHeight.toFloat() / screenWidth * metersToShowX)
        pixelsPerMeterX = screenWidth / width
        pixelsPerMeterY = screenHeight / height
    }

    fun lookAtWithBounds(e: Entity) {
        centerX = clamp(e.centerX,width / 2, engine.levelWidth() - width / 2)
        centerY = clamp(e.centerY, height / 2, engine.levelHeight() - height / 2)
    }

    // Centers the viewport on the specified world position or entity
    fun lookAt(x: Float, y: Float) {
        centerX = x
        centerY = y
    }
    fun lookAt(e: Entity) = lookAt(e.centerX, e.centerY)
    fun lookAt(pos: PointF) = lookAt(pos.x, pos.y)

    // Converts world coordinates to screen coordinates and returns the screen X value
    fun worldToScreenX(worldDistance: Float): Float = worldDistance * pixelsPerMeterX

    // Converts world coordinates to screen coordinates and returns the screen Y value
    fun worldToScreenY(worldDistance: Float): Float = worldDistance * pixelsPerMeterY

    // Converts world position (x, y) to screen position and returns a new PointF
    fun worldToScreen(worldPosX: Float, worldPosY: Float): PointF {
        val screenX = screenCenterX - (centerX - worldPosX) * pixelsPerMeterX
        val screenY = screenCenterY - (centerY - worldPosY) * pixelsPerMeterY
        return PointF(round(screenX), round(screenY))
    }

    // Overloads for convenience
    fun worldToScreen(worldPos: PointF): PointF = worldToScreen(worldPos.x, worldPos.y)
    fun worldToScreen(e: Entity): PointF = worldToScreen(e.x, e.y)

    // Checks if the entity is within the view
    fun inView(e: Entity) = isColliding(this, e)

    fun setBounds(worldEdges: RectF) {
        bounds.set(worldEdges)
    }

    override fun toString() = "Viewport [$screenWidth px, $screenHeight px / $width m, $height m]"
}