package com.thomasspringfeldt.rellorcsedis.rendering

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.Player
import com.thomasspringfeldt.rellorcsedis.gamedata.LevelManager

const val HUD_TEXT_SIZE = 64f
const val HUD_TEXT_POS = 16f

/**
 * HUD for game.
 * @author Thomas Springfeldt
 */
class HUD(private val player: Player, private val level: LevelManager) : Entity() {

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {

        val textSize = HUD_TEXT_SIZE
        val textPosition = HUD_TEXT_POS
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.LEFT

        if (!engine.isGameOver) {
            val health = player.health
            val collected = level.collectedCoins
            val totalCoins = level.totalCoins
            canvas.drawText("Health: $health", textPosition, textSize, paint)
            canvas.drawText("Coins:  $collected/$totalCoins", textPosition, textSize * 2, paint)
        }

        super.render(canvas, transform, paint)
    }
}