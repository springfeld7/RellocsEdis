package com.thomasspringfeldt.rellorcsedis.levels

import com.thomasspringfeldt.rellorcsedis.entities.PLAYER

/**
 * A test level.
 * @author Thomas Springfeldt
 */
class TestLevel : LevelData() {
    init {
        tileToBitMap[NO_TILE] = "no_tile"
        tileToBitMap[1] = PLAYER
        tileToBitMap[2] = "ground_left_round"
        tileToBitMap[3] = "ground"
        tileToBitMap[4] = "ground_right_round"

        tiles = arrayOf(
            intArrayOf(2, 3, 3, 3, 4),
            intArrayOf(2, 0, 1, 0, 4),
            intArrayOf(2, 0, 0, 0, 4),
            intArrayOf(2, 0, 3, 0, 4),
            intArrayOf(2, 0, 0, 0, 4),
            intArrayOf(2, 3, 3, 3, 4)
        )
    }
}
