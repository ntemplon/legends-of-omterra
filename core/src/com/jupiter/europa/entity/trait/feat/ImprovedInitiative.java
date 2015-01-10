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
package com.jupiter.europa.entity.trait.feat;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.entity.effects.AttributeModifierEffect;
import com.jupiter.europa.entity.effects.Effect;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.trait.Qualifications;
import com.jupiter.europa.entity.trait.FeatNotPresentQualifications;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public class ImprovedInitiative implements Feat {

    // Fields
    private final Qualifications qualifications = new FeatNotPresentQualifications(ImprovedInitiative.class);
    private final Effect effect = new ImprovedInitiativeEffect();
    
    
    @Override
    public Qualifications getQualifications() {
        return this.qualifications;
    }
    
    @Override
    public Effect getEffect() {
        return this.effect;
    }

    @Override
    public Sprite getIcon() {
        return new Sprite();
    }

    @Override
    public String getName() {
        return "Improved Initiative";
    }

    @Override
    public String getDescription() {
        return "You gain a +4 bonus to your Initative";
    }

    @Override
    public void write(Json json) {
        
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        
    }
    
    
    // Effect
    private static class ImprovedInitiativeEffect extends AttributeModifierEffect {
        
        // Fields
        private final Map<AttributeSet.Attributes, Integer> modifiers = new HashMap<>();
        
        
        // Properties
        @Override
        public Map<AttributeSet.Attributes, Integer> getModifiers() {
            return this.modifiers;
        }
        
        
        // Initialization
        public ImprovedInitiativeEffect() {
            this.modifiers.put(AttributeSet.Attributes.INITIATIVE, 4);
        }
        
    }
    
}
