package com.thomasspringfeldt.rellorcsedis

/**
 * A test level.
 * @author Thomas Springfeldt
 */
class TestLevel : LevelData() {
    init {
        tileToBitMap[NO_TILE] = "no_tile"
        tileToBitMap[1] = PLAYER
        tileToBitMap[2] = "ground_left"
        tileToBitMap[3] = "ground"
        tileToBitMap[4] = "ground_right"

        tiles = arrayOf(
            intArrayOf(2,3,4,0,1)
        )
    }
}