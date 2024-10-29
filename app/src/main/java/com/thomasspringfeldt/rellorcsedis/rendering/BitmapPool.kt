package com.thomasspringfeldt.rellorcsedis.rendering

import android.graphics.Bitmap
import android.util.Log
import com.thomasspringfeldt.rellorcsedis.Game
import com.thomasspringfeldt.rellorcsedis.rendering.BitmapUtils.loadScaledBitmap
import kotlin.math.ceil

class BitmapPool(private val engine: Game) {
    private val tag = "BitmapPool"
    private val bitmaps: HashMap<String, Bitmap> = HashMap()
    private var nullsprite: Bitmap = loadScaledBitmap(
        engine.context, "nullsprite",
        ceil(engine.worldToScreenX(1.0f)).toInt(),
        ceil(engine.worldToScreenY(1.0f)).toInt()
    )

    fun getBitmap(key: String) = bitmaps[key] ?: nullsprite

    fun createBitmap(spriteName: String, widthMeters: Float, heightMeters: Float): Bitmap {
        val key = makeKey(spriteName, widthMeters, heightMeters)
        if (bitmaps.containsKey(key)) {
            return getBitmap(key)
        }
        try {
            val bmp = loadScaledBitmap(
                engine.context,
                spriteName,
                ceil(engine.worldToScreenX(widthMeters)).toInt(),
                ceil(engine.worldToScreenY(heightMeters)).toInt()
            )
            put(key, bmp)
            return bmp
        } catch (e: Exception) {
            Log.w(tag, "Failed to createBitmap $spriteName! Returning nullsprite", e)
        }
        return nullsprite
    }

    fun clear() {
        for ((key, value) in bitmaps) {
            value.recycle()
        }
        bitmaps.clear()
    }

    fun size() = bitmaps.size

    fun contains(key: String) = bitmaps.containsKey(key)
    fun contains(bmp: Bitmap) = bitmaps.containsValue(bmp)

    fun getKey(bmp: Bitmap): String {
        for ((key, value) in bitmaps) {
            if (bmp == value) {
                return key
            }
        }
        return ""
    }

    fun remove(bmp: Bitmap) {
        for ((key, value) in bitmaps) {
            if (bmp == value) {
                value.recycle()
                bitmaps.remove(key)
                return
            }
        }
    }

    fun remove(key: String) {
        val bmp = bitmaps[key] ?: return
        bitmaps.remove(key)
        bmp.recycle()
    }

    private fun makeKey(name: String, widthMeters: Float, heightMeters: Float) =
        "${name}_${widthMeters}_${heightMeters}"

    private fun put(key: String, bmp: Bitmap) {
        bitmaps[key] = bmp
    }
}