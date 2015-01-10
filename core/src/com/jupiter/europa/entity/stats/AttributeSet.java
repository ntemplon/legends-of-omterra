/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
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
package com.jupiter.europa.entity.stats;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Nathan Templon
 */
public class AttributeSet implements Serializable {
    
    // Enumerations
    public enum Attributes {
        STRENGTH,
        CONSTITUTION,
        DEXTERITY,
        INTELLIGENCE,
        WISDOM,
        CHARISMA,
        FORTITUDE,
        REFLEXES,
        WILL,
        ATTACK_BONUS,
        ARMOR_CLASS,
        INITIATIVE,
        SPELL_POWER,
        SPELL_RESISTANCE;
    }
    
    
    // Fields
    private final Map<Attributes, Integer> attributes = new TreeMap<>();

    
    // Properties
    public int getAttribute(Attributes attribute) {
        if (this.attributes.containsKey(attribute)) {
            return this.attributes.get(attribute);
        }
        return 0;
    }
    
    public void setAttribute(Attributes attribute, int value) {
        this.attributes.put(attribute, value);
    }
    
    
    // Initialization
    public AttributeSet() {
        for (Attributes attr : Attributes.values()) {
            this.attributes.put(attr, 0);
        }
    }
    
    public AttributeSet(AttributeSet source) {
        for (Attributes attr : Attributes.values()) {
            this.attributes.put(attr, source.getAttribute(attr));
        }
    }

    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        this.attributes.keySet().stream().forEach((Attributes attr) -> {
            json.writeValue(attr.toString(), this.attributes.get(attr));
        });
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        jsonData.iterator().forEach((JsonValue value) -> {
            Attributes attr = Attributes.valueOf(value.name());
            if (attr != null) {
                this.attributes.put(attr, value.asInt());
            }
        });
    }
    
}
