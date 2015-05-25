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

import java.util.*;

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
        MOVEMENT_SPEED,
        SPELL_POWER,
        SPELL_RESISTANCE,
        SPELL_PENETRATION;
        
        // Static Fields
        private static Map<String, Attributes> byDisplayName;
        
        
        // Static Methods
        public static Attributes getByDisplayName(String displayName) {
            if (byDisplayName == null) {
                byDisplayName = new HashMap<>();
                for (Attributes attr : Attributes.values()) {
                    byDisplayName.put(attr.getDisplayName(), attr);
                }
            }
            
            if (byDisplayName.containsKey(displayName)) {
                return byDisplayName.get(displayName);
            }
            return null;
        }
        
        
        // Fields
        private final String displayName;
        
        
        // Initialization
        Attributes() {
            String[] words = this.toString().split("_");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                sb.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase());
                if (i < words.length - 1) {
                    sb.append(" ");
                }
            }
            this.displayName = sb.toString();
        }
        
        
        // Public Methods
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    
    // Constants
    public static final Set<Attributes> PRIMARY_ATTRIBUTES = getPrimaryAttributes();
    
    
    // Static Methods
    private static Set<Attributes> getPrimaryAttributes() {
        Set<Attributes> attrs = new LinkedHashSet<>();
        
        attrs.add(Attributes.STRENGTH);
        attrs.add(Attributes.CONSTITUTION);
        attrs.add(Attributes.DEXTERITY);
        attrs.add(Attributes.INTELLIGENCE);
        attrs.add(Attributes.WISDOM);
        attrs.add(Attributes.CHARISMA);
        
        return Collections.unmodifiableSet(attrs);
    }
    
    
    // Fields
    private final Map<Attributes, Integer> attributes = new EnumMap<>(Attributes.class);

    
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
