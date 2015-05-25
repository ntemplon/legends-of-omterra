package com.jupiter.europa.entity.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by nathan on 5/21/15.
 */
public class BaseEffect implements Effect {

    // Fields
    private Entity entity;


    // Properties
    public final Entity getEntity() {
        return this.entity;
    }


    // Public Methods
    @Override
    public void onAdd(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void onRemove() {
        this.entity = null;
    }

    @Override
    public void update(float deltaT) {

    }

    @Override
    public void onCombatTurnStart() {

    }

    @Override
    public void onCombatTurnEnd() {

    }

    @Override
    public void onOutOfCombatTurn() {

    }


    // Serializable Implementation
    @Override
    public void read(Json json, JsonValue jsonValue) {

    }

    @Override
    public void write(Json json) {

    }
}
