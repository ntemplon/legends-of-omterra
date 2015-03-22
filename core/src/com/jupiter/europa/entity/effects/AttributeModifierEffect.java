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
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.AttributeSet.Attributes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public abstract class AttributeModifierEffect implements Effect {
    
    
    // Properties
    public abstract Map<Attributes, Integer> getModifiers();
    
    
    // Public Methods
    @Override
    public void onAdd(Entity entity) {
        if (Families.attributed.matches(entity)) {
            AttributeSet attributes = Mappers.attributes.get(entity).getCurrentAttributes();
            
            Map<Attributes, Integer> modifiers = this.getModifiers();
            modifiers.keySet().stream().forEach((Attributes attribute) -> {
                attributes.setAttribute(attribute, attributes.getAttribute(attribute) + modifiers.get(attribute));
            });
        }
    }

    @Override
    public void onRemove(Entity entity) {
        if (Families.attributed.matches(entity)) {
            AttributeSet attributes = Mappers.attributes.get(entity).getCurrentAttributes();
            
            Map<Attributes, Integer> modifiers = this.getModifiers();
            modifiers.keySet().stream().forEach((Attributes attribute) -> {
                attributes.setAttribute(attribute, attributes.getAttribute(attribute) - modifiers.get(attribute));
            });
        }
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

    @Override
    public void write(Json json) {
        
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        
    }
    
}
