/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jupiter.europa.entity.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nathan Templon
 */
public class MultiEffect implements Effect {

    // Constants
    private static final String EFFECTS_KEY = "effects";
    private static final String EFFECT_CLASS_KEY = "effect-class";
    private static final String EFFECT_DATA_KEY = "effect-data";


    // Fields
    private Effect[] effects;


    // Initialization
    public MultiEffect() {

    }

    public MultiEffect(Effect... effects) {
        this.effects = effects;
    }


    // Public Methods
    @Override
    public void onAdd(Entity entity) {
        for (Effect effect : this.effects) {
            effect.onAdd(entity);
        }
    }

    @Override
    public void onRemove(Entity entity) {
        for (Effect effect : this.effects) {
            effect.onRemove(entity);
        }
    }

    @Override
    public void update(float deltaT) {
        for (Effect effect : this.effects) {
            effect.update(deltaT);
        }
    }

    @Override
    public void onCombatTurnStart() {
        for (Effect effect : this.effects) {
            effect.onCombatTurnStart();
        }
    }

    @Override
    public void onCombatTurnEnd() {
        for (Effect effect : this.effects) {
            effect.onCombatTurnEnd();
        }
    }

    @Override
    public void onOutOfCombatTurn() {
        for (Effect effect : this.effects) {
            effect.onOutOfCombatTurn();
        }
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeArrayStart(EFFECTS_KEY);
        for (Effect effect : this.effects) {
            json.writeObjectStart();
            json.writeValue(EFFECT_CLASS_KEY, effect.getClass().getName());
            json.writeValue(EFFECT_DATA_KEY, effect, effect.getClass());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(EFFECTS_KEY)) {
            List<Effect> readEffects = new ArrayList<>();
            jsonData.get(EFFECTS_KEY).iterator().forEach((JsonValue value) -> {
                if (value.has(EFFECT_CLASS_KEY) && value.has(EFFECT_DATA_KEY)) {
                    try {
                        Class<?> type = Class.forName(value.getString(EFFECTS_KEY));
                        if (Effect.class.isAssignableFrom(type)) {
                            readEffects.add((Effect)json.fromJson(type, value.get(EFFECT_DATA_KEY).toString()));
                        }
                    }
                    catch (ClassNotFoundException ex) {

                    }
                }
            });
            this.effects = readEffects.toArray(new Effect[readEffects.size()]);
        }
    }

}
