package com.thomasspringfeldt.rellorcsedis

import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.os.SystemClock.uptimeMillis
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.thomasspringfeldt.rellorcsedis.audio.Jukebox
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.gamedata.GameEvent
import com.thomasspringfeldt.rellorcsedis.gamedata.LevelManager
import com.thomasspringfeldt.rellorcsedis.gamedata.levels.Level1
import com.thomasspringfeldt.rellorcsedis.gamedata.levels.Level2
import com.thomasspringfeldt.rellorcsedis.input.InputManager
import com.thomasspringfeldt.rellorcsedis.rendering.BitmapPool
import com.thomasspringfeldt.rellorcsedis.rendering.HUD
import com.thomasspringfeldt.rellorcsedis.rendering.Viewport
import kotlin.random.Random

const val PREFS = "com.thomasspringfeldt.rellorcsedis"
const val TARGET_FPS = 60f
var RNG = Random(uptimeMillis())
const val NANOS_TO_SECOND = 1f / 1000000000.0f
lateinit var engine: Game

/**
 * Game engine for Rellorcs Edis.
 * @author Thomas Springfeldt
 */
class Game(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), Runnable, SurfaceHolder.Callback {

    private val tag = "Game"
    init {
        engine = this
        holder?.addCallback(this)
        holder?.setFixedSize(screenWidth(), screenHeight())
    }
    private lateinit var gameThread : Thread
    @Volatile private var isRunning = false
    var isGameOver = false
    private val jukebox = Jukebox(this)
    private var inputs = InputManager()
    private val camera = Viewport(screenWidth(), screenHeight(), 0f, 12f)
    val bitmapPool = BitmapPool(this)
    private var level: LevelManager = LevelManager(Level1())
    var player = level.player
    private var hud = HUD(player, level)

    fun worldToScreenX(worldDistance: Float) = camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance: Float) = camera.worldToScreenY(worldDistance)
    fun screenHeight() = context.resources.displayMetrics.heightPixels
    fun screenWidth() = context.resources.displayMetrics.widthPixels
    fun levelHeight() = level.levelHeight
    fun setControls(control: InputManager) {
        inputs.onPause() //give the previous controller
        inputs.onStop() //a chance to clean up
        inputs = control
        inputs.onStart()
    }
    fun getControls() = inputs
    fun getActivity() = context as MainActivity
    fun getAssets() = context.assets
    fun getPreferences() = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    fun getPreferencesEditor() = getPreferences().edit()
    fun savePreference(key: String, v: Boolean) = getPreferencesEditor().putBoolean(key, v).commit()

    /**
     * Game loop.
     */
    override fun run() {
        Log.d(tag, "run()")
        var lastFrame = System.nanoTime()
        onGameEvent(GameEvent.LevelStart, player)
        while(isRunning) {
            val deltaTime = (System.nanoTime() - lastFrame) * NANOS_TO_SECOND
            lastFrame = System.nanoTime()
            inputs.update(deltaTime)
            update(deltaTime)
            render()
        }
    }

    /**
     * Updates game state.
     */
    private fun update(deltaTime: Float) {
        level.update(deltaTime)
        camera.lookAt(player)
        checkGameOver()
    }

    /**
     * Renders game state.
     */
    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.CYAN)
        val paint = Paint()
        val transform = Matrix()
        var position: PointF

        val visible = buildVisibleSet()

        visible.forEach {
            transform.reset()
            position = camera.worldToScreen(it)
            transform.postTranslate(position.x, position.y)
            it.render(canvas, transform, paint)
        }

        hud.render(canvas, transform, paint)

        holder.unlockCanvasAndPost(canvas)
    }


    fun loadNextLevel() {
        level = LevelManager(Level2())
        player = level.player
        hud = HUD(player, level)

        jukebox.resetBgMusicPlayer()
        jukebox.loadMusic("bgm/bgm_2.wav")
        jukebox.resumeBgMusic()
        onGameEvent(GameEvent.LevelGoal, null)

    }

    private fun checkGameOver() {
        if (player.health <= 0) {
            this.onGameEvent(GameEvent.GameOver, null)
            player.respawn()
        }
    }

    fun onGameEvent(event: GameEvent, e: Entity? /*can be null!*/) {
        //TODO: really should schedule these by adding to an list, avoiding duplicates, and then start all unique sounds once per frame.
        jukebox.playEventSound(event)
    }

    /**
     * Filters all level entities and returns only the visible ones.
     */
    private fun buildVisibleSet() : List<Entity> {
        return level.entities.filter { camera.inView(it) }
    }

    /**
     * Stops the game from processing.
     */
    fun onPause() {
        Log.d(tag, "OnPause()")
        jukebox.pauseBgMusic()
        inputs.onPause()
        isRunning = false
    }

    /**
     * Resumes the processing of the game.
     */
    fun onResume() {
        Log.d(tag, "OnResume()")
        jukebox.resumeBgMusic()
        inputs.onResume()
        isRunning = true
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
