package com.jupiter.europa.entity.effects

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

/**
 * Created by nathan on 5/21/15.
 */
public open class BaseEffect() : Effect {

    // Properties
    public var entity: Entity? = null
        private set


    // Public Methods
    override fun onAdd(entity: Entity) {
        this.entity = entity
    }

    override fun onRemove() {
        this.entity = null
    }

    override fun update(deltaT: Float) {
    }

    override fun onCombatTurnStart() {
    }

    override fun onCombatTurnEnd() {
    }

    override fun onOutOfCombatTurn() {
    }


    // Serializable Implementation
    override fun read(json: Json, jsonValue: JsonValue) {
    }

    override fun write(json: Json) {
    }
}
