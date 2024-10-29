package com.thomasspringfeldt.rellorcsedis

/**
 * Level manager.
 * @author Thomas Springfeldt
 */
class LevelManager(data: LevelData) {

    lateinit var player: Player
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
        }
    }

    fun addAndRemoveEntities() {
        entities.removeAll(entitiesToRemove.toSet())
        entities.addAll(entitiesToAdd)
    }

    fun addEntity(entity: Entity) {
        entitiesToAdd.add(entity)
    }

    fun removeEntity(entity: Entity) {
        entitiesToRemove.add(entity)
    }

    fun loadAssets(data: LevelData) {
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
            else -> {
                addEntity(StaticEntity(spriteName, x, y))
            }
        }
    }
}