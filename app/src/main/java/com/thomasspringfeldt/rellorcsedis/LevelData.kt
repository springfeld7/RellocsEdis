package com.thomasspringfeldt.rellorcsedis

const val PLAYER = "pink_left1"
const val NULLSPRITE = "null_sprite"
const val NO_TILE = 0

/**
 * Base class for levels.
 * @author Thomas Springfeldt
 */
abstract class LevelData {

    var tiles: Array<IntArray> = emptyArray()
    var tileToBitMap = HashMap<Int, String>()
    var height = 0
    var width = 0

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

    fun updateLevelDimension() {
        height = tiles.size
        width = getRow(0).size
    }
}