package com.thomasspringfeldt.rellorcsedis.gamedata

import com.thomasspringfeldt.rellorcsedis.engine
import com.thomasspringfeldt.rellorcsedis.entities.GOAL
import com.thomasspringfeldt.rellorcsedis.entities.PLAYER
import com.thomasspringfeldt.rellorcsedis.entities.SPIKES
import com.thomasspringfeldt.rellorcsedis.entities.collectibles.COIN
import com.thomasspringfeldt.rellorcsedis.entities.collectibles.INVINCIBILITY
import com.thomasspringfeldt.rellorcsedis.entities.collectibles.SUPER_JUMP
import java.io.IOException


const val NULLSPRITE = "null_sprite"
const val NO_TILE = 0

private const val LEVEL_1 = "level_files/level1.txt"
private const val LEVEL_2 = "level_files/level2.txt"

/**
 * Base class for levels.
 * @author Thomas Springfeldt
 */
open class Level(levelNumber: Int) {

    private lateinit var tiles: ArrayList<IntArray>
    private var tileToBitMap = HashMap<Int, String>()
    var totalCoins = 0

    init {
        val levelPath = getLevelPath(levelNumber)
        loadLevel(levelPath)


        tileToBitMap[NO_TILE] = "no_tile"
        tileToBitMap[1] = PLAYER
        tileToBitMap[2] = "ground_left_round"
        tileToBitMap[3] = "ground_left"
        tileToBitMap[4] = "ground"
        tileToBitMap[5] = "ground_round"
        tileToBitMap[6] = "ground_right_round"
        tileToBitMap[7] = "ground_right"
        tileToBitMap[8] = "mud_left"
        tileToBitMap[9] = "mud_right"
        tileToBitMap[10] = "mud"
        tileToBitMap[11] = SPIKES
        tileToBitMap[12] = COIN
        tileToBitMap[13] = GOAL
        tileToBitMap[14] = INVINCIBILITY
        tileToBitMap[15] = SUPER_JUMP
        tileToBitMap[16] = "ground_yellow_left_round"
        tileToBitMap[17] = "ground_yellow_left"
        tileToBitMap[18] = "ground_yellow"
        tileToBitMap[19] = "ground_yellow_round"
        tileToBitMap[20] = "ground_yellow_right_round"
        tileToBitMap[21] = "ground_yellow_right"
        tileToBitMap[22] = "mud_yellow_left"
        tileToBitMap[23] = "mud_yellow_right"
        tileToBitMap[24] = "mud_yellow"
    }

    /**
     * Loads the level data from the provided path,
     * then populates the tile map.
     */
    private fun loadLevel(levelPath: String) {

        val levelData = ArrayList<List<String>>()

        try {
            val reader = engine.getAssets().open(levelPath).bufferedReader()
            reader.forEachLine {
            levelData.add(it.split(",")) }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        totalCoins = levelData[0][0].toInt()
        val levelHeight = levelData[1][0].toInt()
        tiles = ArrayList(levelHeight)

        for (i in 0 until levelHeight) {
            tiles.add(levelData[i+2].mapNotNull { it.toIntOrNull() }.toIntArray())
        }
    }

    private fun getLevelPath(levelNumber: Int) : String {
        return when (levelNumber) {
            1 -> { LEVEL_1 }
            2 -> { LEVEL_2 }
            else -> { LEVEL_1 }
        }
    }

    fun getRow(y: Int) : IntArray {
        return tiles[y]
    }

    fun getTile(x: Int, y: Int) : Int {
        return getRow(y)[x]
    }

    fun getSpriteName(tileType: Int) : String {
        val fileName = tileToBitMap[tileType]
        return fileName ?: NULLSPRITE
    }

    fun height() = tiles.size
    fun width() = getRow(0).size
}