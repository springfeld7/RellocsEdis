package com.thomasspringfeldt.rellorcsedis

import android.content.Context
import android.graphics.Color
import android.graphics.Paint

import android.os.Build
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val TARGET_FPS = 60f
var RNG = Random(uptimeMillis())
lateinit var engine: Game

/**
 * Game engine for Rellorcs Edis.
 * @author Thomas Springfeldt
 */
class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    private val tag = "Game"
    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(screenWidth() / 2, screenHeight() / 2)
    }
    private lateinit var gameThread : Thread
    @Volatile private var isRunning : Boolean = false
    private val camera = Viewport(screenWidth(), screenHeight(), 20.0f, 0.0f)
    private val level: LevelManager = LevelManager(TestLevel())

    /**
     * Game loop.
     */
    override fun run() {
        Log.d(tag, "run()")
        while(isRunning) {
            update()
            render()
        }
    }

    /**
     * Updates game state.
     */
    private fun update() {
        level.update(0.1f) //fix later
    }

    /**
     * Renders game state.
     */
    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.CYAN)
        val paint = Paint()

        level.entities.forEach { it.render(canvas, paint) }

        holder.unlockCanvasAndPost(canvas)
    }

    fun worldToScreenX(worldDistance: Float) = camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance: Float) = camera.worldToScreenY(worldDistance)
    fun screenHeight() = context.resources.displayMetrics.heightPixels
    fun screenWidth() = context.resources.displayMetrics.widthPixels

    /**
     * Stops the game from processing.
     */
    fun onPause() {
        Log.d(tag, "OnPause()")
    }

    /**
     * Resumes the processing of the game.
     */
    fun onResume() {
        Log.d(tag, "OnResume()")
    }

    /**
     * Destroys the game.
     */
    fun onDestroy() {
        Log.d(tag, "OnDestroy()")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(tag, "SurfaceCreated()")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            holder.surface.setFrameRate(TARGET_FPS, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT)
        }
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(tag, "SurfaceChanged(width: $width, height: $height)")
        Log.d(tag, "Screen width(width: ${screenWidth()}, height: ${screenHeight()})")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(tag, "SurfaceDestroyed()")
        isRunning = false
        gameThread.join()
    }
}
