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
import com.jupiter.europa.entity.stats.AttributeSet.Attributes;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Nathan Templon
 */
public abstract class VariableAttributeModifierEffect extends AttributeModifierEffect {
    
    // Fields
    private final Map<Attributes, Integer> modifiers = new HashMap<>();
    
    
    // Properties
    @Override
    public Map<Attributes, Integer> getModifiers() {
        return this.modifiers;
    }
    
    
    // Initialization
    public VariableAttributeModifierEffect() {
        
    }
    
    
    // Public Methods
    @Override
    public void onAdded(Entity entity) {
        // Trying to figure out what is going on here
        Map<Attributes, Integer> newMods = this.computeModifiers(entity);
        
        Map<Attributes, Integer> oldMods = this.modifiers.keySet().stream()
                .collect(Collectors.toMap((Attributes attr) -> attr, (Attributes attr) -> this.modifiers.get(attr)));
    }
    
    public abstract Map<Attributes, Integer> computeModifiers(Entity entity);
    
}
