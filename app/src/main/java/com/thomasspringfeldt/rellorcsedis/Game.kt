package com.thomasspringfeldt.rellorcsedis

import android.content.Context
import android.graphics.Color

import android.os.Build
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672
const val TARGET_FPS = 60f
const val PIXELS_PER_METER = 100
var RNG = Random(uptimeMillis())
lateinit var engine: Game

/**
 * Game engine for Rellorcs Edis.
 * @author Thomas Springfeldt
 */
class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    private val tag = "Game"
    private lateinit var gameThread : Thread
    @Volatile private var isRunning : Boolean = false
    private val level: LevelManager = LevelManager(TestLevel())

    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
    }

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
        val random = RNG.nextInt(2)
        if (random == 0) {
            canvas.drawColor(Color.MAGENTA)
        } else {
            canvas.drawColor(Color.CYAN)
        }

        holder.unlockCanvasAndPost(canvas)
    }

    fun worldToScreenX(worldDistance: Float) = (worldDistance * PIXELS_PER_METER).toInt()
    fun worldToScreenY(worldDistance: Float) = (worldDistance * PIXELS_PER_METER).toInt()
    fun screenToWorldX(pixelDistance: Float) = pixelDistance / PIXELS_PER_METER
    fun screenToWorldY(pixelDistance: Float) = pixelDistance / PIXELS_PER_METER


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
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(tag, "SurfaceDestroyed()")
        isRunning = false
        gameThread.join()
    }
}
