package com.thomasspringfeldt.rellorcsedis

/**
 * Data for Level 1.
 * @author Thomas Springfeldt
 */
class Level1 : LevelData() {
    init {
        tileToBitMap[NO_TILE] = "no_tile"
        tileToBitMap[1] = PLAYER
        tileToBitMap[2] = "ground_left_round"
        tileToBitMap[3] = "ground_left"
        tileToBitMap[4] = "ground"
        tileToBitMap[5] = "ground_right_round"
        tileToBitMap[6] = "ground_right"
        tileToBitMap[7] = "mud_left"
        tileToBitMap[8] = "mud_right"
        tileToBitMap[9] = "mud"

        tiles = arrayOf(
            intArrayOf(3, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 4, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9),
            intArrayOf(9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9),
            intArrayOf(9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9),
            intArrayOf(9, 9, 0, 0, 1, 0, 0, 0, 0, 3, 4, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9),
            intArrayOf(9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9),
            intArrayOf(9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9),
            intArrayOf(7, 9, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 8)
        )
    }
}