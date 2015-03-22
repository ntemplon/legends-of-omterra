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
public abstract class VariableAttributeModifierEffect extends AttributeModifierEffect {

    // Fields
    private final Map<Attributes, Integer> modifiers = new HashMap<>();
    private Entity attached = null;


    // Properties
    @Override
    public final Map<Attributes, Integer> getModifiers() {
        return this.modifiers;
    }


    // Initialization
    public VariableAttributeModifierEffect() {

    }


    // Public Methods
    @Override
    public void onAdd(Entity entity) {
        if (Families.attributed.matches(entity)) {
            this.attached = entity;
            this.recomputeAttributes();
        }
        else {
            this.attached = null;
        }
    }
    
    @Override
    public void onRemove(Entity entity) {
        this.attached = null;
    }
    
    @Override
    public void onCombatTurnStart() {
        this.recomputeAttributes();
    }

    @Override
    public void onCombatTurnEnd() {
        this.recomputeAttributes();
    }

    @Override
    public void onOutOfCombatTurn() {
        this.recomputeAttributes();
    }

    public abstract Map<Attributes, Integer> computeModifiers(Entity entity);


    // Private Methods
    private void recomputeAttributes() {
        if (this.attached != null) {
            Map<Attributes, Integer> oldMods = new HashMap<>(this.modifiers);

            this.modifiers.clear();
            this.modifiers.putAll(this.computeModifiers(this.attached));

            AttributeSet attributes = Mappers.attributes.get(this.attached).getCurrentAttributes();
            this.modifiers.keySet().stream().forEach((Attributes attribute) -> {
                attributes.setAttribute(attribute, attributes.getAttribute(attribute) + this.modifiers.get(attribute) - oldMods.get(attribute));
            });
        }
    }

}
