package com.thomasspringfeldt.rellorcsedis

/**
 * Level manager.
 * @author Thomas Springfeldt
 */
class LevelManager(data: LevelData) {

    private val entities = ArrayList<Entity>()
    private val entitiesToAdd = ArrayList<Entity>()
    private val entitiesToRemove = ArrayList<Entity>()

    init {
        loadAssets(data)
    }

    fun update(deltaTime: Float) {
        entities.forEach { it.update(deltaTime) }
        //collision detection
        //add and remove entities
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
    }

    private fun createEntity(spriteName: String, x: Float, y: Float) {
        val entity: Entity = when (spriteName) {
            PLAYER -> //player entity
                else -> //static entity
        }
        addEntity(entity)
    }
}