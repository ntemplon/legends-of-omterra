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
package com.emergence.entity.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.entity.Families;
import com.emergence.entity.Mappers;
import com.emergence.entity.stats.Attributes;

/**
 *
 * @author Nathan Templon
 */
public abstract class AttributeModifierEffect implements Effect {

    // Properties
    public int getStrengthModifier() {
        return 0;
    }
    
    public int getDexterityModifier() {
        return 0;
    }
    
    public int getConstitutionModifier() {
        return 0;
    }
    
    public int getIntelligenceModifier() {
        return 0;
    }
    
    public int getWisdomModifier() {
        return 0;
    }
    
    public int getCharismaModifier() {
        return 0;
    }
    
    public int getFortitudeModifier() {
        return 0;
    }
    
    public int getReflexesModifier() {
        return 0;
    }
    
    public int getWillModifier() {
        return 0;
    }
    
    public int getAttackBonusModifier() {
        return 0;
    }
    
    public int getArmorClassModifier() {
        return 0;
    }
    
    public int getInitiativeModifier() {
        return 0;
    }
    
    public int getSpellPowerModifier() {
        return 0;
    }
    
    public int getSpellResistanceModifier() {
        return 0;
    }
    
    
    // Public Methods
    @Override
    public void onAdded(Entity entity) {
        if (Families.attributed.matches(entity)) {
            Attributes attributes = Mappers.attributes.get(entity).getCurrentAttributes();
            
            attributes.setStrength(attributes.getStrength() + this.getStrengthModifier());
            attributes.setDexterity(attributes.getDexterity() + this.getDexterityModifier());
            attributes.setConstitution(attributes.getConstitution() + this.getConstitutionModifier());
            attributes.setIntelligence(attributes.getIntelligence() + this.getIntelligenceModifier());
            attributes.setWisdom(attributes.getWisdom() + this.getWisdomModifier());
            attributes.setCharisma(attributes.getCharisma() + this.getCharismaModifier());
            
            attributes.setFortitude(attributes.getFortitude() + this.getFortitudeModifier());
            attributes.setReflexes(attributes.getReflexes() + this.getReflexesModifier());
            attributes.setWill(attributes.getWill() + this.getWillModifier());
            
            attributes.setAttackBonus(attributes.getAttackBonus() + this.getAttackBonusModifier());
            attributes.setArmorClass(attributes.getArmorClass() + this.getArmorClassModifier());
            attributes.setInitiative(attributes.getInitiative() + this.getInitiativeModifier());
            attributes.setSpellPower(attributes.getSpellPower() + this.getSpellPowerModifier());
            attributes.setSpellResistance(attributes.getSpellResistance() + this.getSpellResistanceModifier());
        }
    }

    @Override
    public void onRemove(Entity entity) {
        if (Families.attributed.matches(entity)) {
            Attributes attributes = Mappers.attributes.get(entity).getCurrentAttributes();
            
            attributes.setStrength(attributes.getStrength() - this.getStrengthModifier());
            attributes.setDexterity(attributes.getDexterity() - this.getDexterityModifier());
            attributes.setConstitution(attributes.getConstitution() - this.getConstitutionModifier());
            attributes.setIntelligence(attributes.getIntelligence() - this.getIntelligenceModifier());
            attributes.setWisdom(attributes.getWisdom() - this.getWisdomModifier());
            attributes.setCharisma(attributes.getCharisma() - this.getCharismaModifier());
            
            attributes.setFortitude(attributes.getFortitude() - this.getFortitudeModifier());
            attributes.setReflexes(attributes.getReflexes() - this.getReflexesModifier());
            attributes.setWill(attributes.getWill() - this.getWillModifier());
            
            attributes.setAttackBonus(attributes.getAttackBonus() - this.getAttackBonusModifier());
            attributes.setArmorClass(attributes.getArmorClass() - this.getArmorClassModifier());
            attributes.setInitiative(attributes.getInitiative() - this.getInitiativeModifier());
            attributes.setSpellPower(attributes.getSpellPower() - this.getSpellPowerModifier());
            attributes.setSpellResistance(attributes.getSpellResistance() - this.getSpellResistanceModifier());
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
