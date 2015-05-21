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
package com.jupiter.europa.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.effects.Effect;
import com.jupiter.europa.entity.messaging.RequestEffectAddMessage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Nathan Templon
 */
public class EffectsComponent extends Component implements Serializable, OwnedComponent {

    // Constants
    private static final String EFFECTS_KEY = "effects";
    private static final String EFFECT_CLASS_KEY = "effect-class";
    private static final String EFFECT_DATA_KEY = "effect-data";


    // Fields
    public final Collection<Effect> effects = new ArrayList<>();
    private Collection<Effect> efQueue = new ArrayList<>();
    private Entity owner;


    // Properties Methods
    @Override
    public Entity getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(Entity entity) {
        this.owner = entity;
        this.efQueue.stream()
                .map(effect -> new RequestEffectAddMessage(entity, effect))
                .forEach(message -> EuropaGame.game.getMessageSystem().publish(message));
        this.efQueue = null;
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeArrayStart(EFFECTS_KEY);
        this.effects.stream().forEach((Effect effect) -> {
            json.writeObjectStart();
            json.writeValue(EFFECT_CLASS_KEY, effect.getClass().getName());
            json.writeValue(EFFECT_DATA_KEY, effect, effect.getClass());
            json.writeObjectEnd();
        });
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(EFFECTS_KEY)) {
            JsonValue selectedData = jsonData.get(EFFECTS_KEY);
            if (selectedData.isArray()) {
                selectedData.iterator().forEach((JsonValue value) -> {
                    if (value.has(EFFECT_CLASS_KEY) && value.has(EFFECT_DATA_KEY)) {
                        String typeName = value.getString(EFFECT_CLASS_KEY);
                        try {
                            Class<?> type = Class.forName(typeName);
                            if (Effect.class.isAssignableFrom(type)) {
                                Effect effect = (Effect) json.fromJson(type, value.get(EFFECT_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS));
                                this.efQueue.add(effect);
                            }
                        } catch (ClassNotFoundException ex) {

                        }
                    }
                });
            }
        }
    }
}
