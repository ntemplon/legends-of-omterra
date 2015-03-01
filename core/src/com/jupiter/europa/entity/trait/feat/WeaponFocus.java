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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.effects.AttributeModifierEffect;
import com.jupiter.europa.entity.effects.Effect;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.AttributeSet.Attributes;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass.LevelUpArgs;
import com.jupiter.europa.entity.trait.Qualifications;
import com.jupiter.europa.entity.trait.FeatNotPresentQualifications;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public class WeaponFocus implements Feat {

    // Fields
    private final Qualifications qualifications = new FeatNotPresentQualifications(WeaponFocus.class);
    private final Effect effect = new WeaponFocusEffect();
    
    
    // Properties
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
        return "Weapon Focus";
    }

    @Override
    public String getDescription() {
        return "Your attack bonus increases by 10.";
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
    
    
    // Effect
    private static class WeaponFocusEffect extends AttributeModifierEffect {
        
        // Fields
        private final Map<Attributes, Integer> modifiers = new HashMap<>();
        
        
        // Properties
        @Override
        public Map<Attributes, Integer> getModifiers() {
            return this.modifiers;
        }
        
        
        // Initialization
        private WeaponFocusEffect() {
            this.modifiers.put(Attributes.ATTACK_BONUS, 10);
        }
        
        
        // Public Methods
        @Override
        public void onAdded(Entity entity) {
            if (Families.classed.matches(entity)) {
                CharacterClass charClass = Mappers.characterClass.get(entity).getCharacterClass();
                
                this.modifiers.put(Attributes.ATTACK_BONUS, 5 + charClass.getLevel());
                
                charClass.addLevelUpListener((LevelUpArgs args) -> {
                    int oldValue = this.modifiers.get(Attributes.ATTACK_BONUS);
                    int newValue = 5 + charClass.getLevel();
                    if (newValue != oldValue) {
                        this.modifiers.put(Attributes.ATTACK_BONUS, newValue);
                        if (Families.attributed.matches(entity)) {
                            AttributeSet attributes = Mappers.attributes.get(entity).getCurrentAttributes();
                            attributes.setAttribute(Attributes.ATTACK_BONUS, attributes.getAttribute(Attributes.ATTACK_BONUS) + (newValue - oldValue));
                        }
                    }
                });
            }
            
            super.onAdded(entity);
        }
        
    }

}
