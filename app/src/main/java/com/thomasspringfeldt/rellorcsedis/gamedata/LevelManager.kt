package com.thomasspringfeldt.rellorcsedis.gamedata

import com.thomasspringfeldt.rellorcsedis.entities.COIN
import com.thomasspringfeldt.rellorcsedis.entities.Coin
import com.thomasspringfeldt.rellorcsedis.entities.Collectible
import com.thomasspringfeldt.rellorcsedis.entities.Entity
import com.thomasspringfeldt.rellorcsedis.entities.GOAL
import com.thomasspringfeldt.rellorcsedis.entities.Goal
import com.thomasspringfeldt.rellorcsedis.entities.INVINCIBILITY
import com.thomasspringfeldt.rellorcsedis.entities.Invincibility
import com.thomasspringfeldt.rellorcsedis.entities.PLAYER
import com.thomasspringfeldt.rellorcsedis.entities.Player
import com.thomasspringfeldt.rellorcsedis.entities.SPIKES
import com.thomasspringfeldt.rellorcsedis.entities.SUPER_JUMP
import com.thomasspringfeldt.rellorcsedis.entities.Spikes
import com.thomasspringfeldt.rellorcsedis.entities.StaticEntity
import com.thomasspringfeldt.rellorcsedis.entities.SuperJump
import com.thomasspringfeldt.rellorcsedis.entities.isColliding

/**
 * Level manager.
 * @author Thomas Springfeldt
 */
class LevelManager(data: Level) {

    lateinit var player: Player
    var collectedCoins = 0
    val totalCoins = data.totalCoins
    val entities = ArrayList<Entity>()
    private val entitiesToAdd = ArrayList<Entity>()
    private val entitiesToRemove = ArrayList<Entity>()
    var levelHeight = 0f

    init {
        loadAssets(data)
    }

    fun update(deltaTime: Float) {
        entities.forEach { it.update(deltaTime) }
        checkCollisions()
        addAndRemoveEntities()
    }

    private fun checkCollisions() {
        for (e in entities) {
            if (e == player) {
                continue
            }
            if (isColliding(e, player)) {
                e.onCollision(player)
                player.onCollision(e)
            }
            for (e2 in entities) {
                if (e2 == e || e2 == player) {
                    continue
                }
                if (isColliding(e, e2)) {
                    e.onCollision(e2)
                }
            }
            if (e is Coin) {
                if (e.isDead) {
                    collectedCoins += 1
                }
            }
            if (e is Collectible) {
                if (e.isDead) {
                    removeEntity(e)
                }
            }

        }
    }

    fun addAndRemoveEntities() {
        entities.removeAll(entitiesToRemove.toSet())
        entitiesToRemove.clear()

        entities.addAll(entitiesToAdd)
        entitiesToAdd.clear()
    }

    fun addEntity(entity: Entity) {
        entitiesToAdd.add(entity)
    }

    fun removeEntity(entity: Entity) {
        entitiesToRemove.add(entity)
    }

    fun loadAssets(data: Level) {
        for (y in 0 until data.height()) {
            val row = data.getRow(y)
            for (x in 0 until row.size) {
                val tileId = row[x]
                if (tileId != NO_TILE) {
                    val spriteName = data.getSpriteName(tileId)
                    createEntity(spriteName, x.toFloat(), y.toFloat())
                }
            }
        }
        levelHeight = data.height().toFloat()
        addAndRemoveEntities()
    }

    private fun createEntity(spriteName: String, x: Float, y: Float) {
        when (spriteName) {
            PLAYER -> {
                player = Player(spriteName, x, y)
                addEntity(player)
            }
            SPIKES -> {
                addEntity(Spikes(spriteName, x, y))
            }
            COIN -> {
                addEntity(Coin(spriteName, x, y))
            }
            GOAL-> {
                addEntity(Goal(spriteName, x, y))
            }
            INVINCIBILITY -> {
                addEntity(Invincibility(spriteName, x, y))
            }
            SUPER_JUMP -> {
                addEntity(SuperJump(spriteName, x, y))
            }
            else -> {
                addEntity(StaticEntity(spriteName, x, y))
            }
        }
    }
}