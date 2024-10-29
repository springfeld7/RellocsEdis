package com.thomasspringfeldt.rellorcsedis.levels


const val NULLSPRITE = "null_sprite"
const val NO_TILE = 0

/**
 * Base class for levels.
 * @author Thomas Springfeldt
 */
abstract class LevelData {

    var tiles: Array<IntArray> = emptyArray()
    var tileToBitMap = HashMap<Int, String>()

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