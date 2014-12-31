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
package com.emergence.entity.stats;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 *
 * @author Nathan Templon
 */
public class Attributes implements Serializable {
    
    // Constants
    private final String STRENGTH_KEY = "strength";
    private final String CONSTITUTION_KEY = "constitution";
    private final String DEXTERITY_KEY = "dexterity";
    private final String INTELLIGENCE_KEY = "intelligence";
    private final String WISDOM_KEY = "wisdom";
    private final String CHARISMA_KEY = "charisma";
    
    private final String FORTITUDE_KEY = "fortitude";
    private final String REFLEXES_KEY = "reflexes";
    private final String WILL_KEY = "will";
    
    private final String ATTACK_BONUS_KEY = "attack-bonus";
    private final String ARMOR_CLASS_KEY = "armor-class";
    private final String SPELL_POWER_KEY = "spell-power";
    private final String SPELL_RESISTANCE_KEY = "spell-resistance";
    
    
    // Fields
    //   These represent the "base" score of each ability - before equipment modifiers
    private int strength;
    private int constitution;
    private int dexterity;
    private int intelligence;
    private int wisdom;
    private int charisma;
    
    private int fortitude;
    private int reflexes;
    private int will;
    
    private int attackBonus;
    private int armorClass;
    private int spellPower;
    private int spellResistance;

    
    // Properties
    /**
     * @return the strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * @param strength the strength to set
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * @return the constitution
     */
    public int getConstitution() {
        return constitution;
    }

    /**
     * @param constitution the constitution to set
     */
    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    /**
     * @return the dexterity
     */
    public int getDexterity() {
        return dexterity;
    }

    /**
     * @param dexterity the dexterity to set
     */
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    /**
     * @return the intelligence
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * @param intelligence the intelligence to set
     */
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * @return the wisdom
     */
    public int getWisdom() {
        return wisdom;
    }

    /**
     * @param wisdom the wisdom to set
     */
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    /**
     * @return the charisma
     */
    public int getCharisma() {
        return charisma;
    }

    /**
     * @param charisma the charisma to set
     */
    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    /**
     * @return the fortitude
     */
    public int getFortitude() {
        return fortitude;
    }

    /**
     * @param fortitude the fortitude to set
     */
    public void setFortitude(int fortitude) {
        this.fortitude = fortitude;
    }

    /**
     * @return the reflexes
     */
    public int getReflexes() {
        return reflexes;
    }

    /**
     * @param reflexes the reflexes to set
     */
    public void setReflexes(int reflexes) {
        this.reflexes = reflexes;
    }

    /**
     * @return the will
     */
    public int getWill() {
        return will;
    }

    /**
     * @param will the will to set
     */
    public void setWill(int will) {
        this.will = will;
    }

    /**
     * @return the attackBonus
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * @param attackBonus the attackBonus to set
     */
    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }

    /**
     * @return the armorClass
     */
    public int getArmorClass() {
        return armorClass;
    }

    /**
     * @param armorClass the armorClass to set
     */
    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    /**
     * @return the spellPower
     */
    public int getSpellPower() {
        return spellPower;
    }

    /**
     * @param spellPower the spellPower to set
     */
    public void setSpellPower(int spellPower) {
        this.spellPower = spellPower;
    }

    /**
     * @return the spellResistance
     */
    public int getSpellResistance() {
        return spellResistance;
    }

    /**
     * @param spellResistance the spellResistance to set
     */
    public void setSpellResistance(int spellResistance) {
        this.spellResistance = spellResistance;
    }
    
    
    // Initialization
    public Attributes() {
        
    }
    
    public Attributes(Attributes source) {
        this.strength = source.getStrength();
        this.dexterity = source.getDexterity();
        this.constitution = source.getConstitution();
        this.intelligence = source.getIntelligence();
        this.wisdom = source.getWisdom();
        this.charisma = source.getCharisma();
        
        this.reflexes = source.getReflexes();
        this.fortitude = source.getFortitude();
        this.will = source.getWill();
        
        this.attackBonus = source.getAttackBonus();
        this.armorClass = source.getArmorClass();
        this.spellPower = source.getSpellPower();
        this.spellResistance = source.getSpellResistance();
    }

    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(STRENGTH_KEY, this.getStrength());
        json.writeValue(CONSTITUTION_KEY, this.getConstitution());
        json.writeValue(DEXTERITY_KEY, this.getDexterity());
        json.writeValue(INTELLIGENCE_KEY, this.getIntelligence());
        json.writeValue(WISDOM_KEY, this.getWisdom());
        json.writeValue(CHARISMA_KEY, this.getCharisma());
        
        json.writeValue(FORTITUDE_KEY, this.getFortitude());
        json.writeValue(REFLEXES_KEY, this.getReflexes());
        json.writeValue(WILL_KEY, this.getWill());
        
        json.writeValue(ATTACK_BONUS_KEY, this.getAttackBonus());
        json.writeValue(ARMOR_CLASS_KEY, this.getArmorClass());
        json.writeValue(SPELL_POWER_KEY, this.getSpellPower());
        json.writeValue(SPELL_RESISTANCE_KEY, this.getSpellResistance());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(STRENGTH_KEY)) {
            this.setStrength(jsonData.getInt(STRENGTH_KEY));
        }
        if (jsonData.has(CONSTITUTION_KEY)) {
            this.setConstitution(jsonData.getInt(CONSTITUTION_KEY));
        }
        if (jsonData.has(DEXTERITY_KEY)) {
            this.setDexterity(jsonData.getInt(DEXTERITY_KEY));
        }
        if (jsonData.has(INTELLIGENCE_KEY)) {
            this.setIntelligence(jsonData.getInt(INTELLIGENCE_KEY));
        }
        if (jsonData.has(WISDOM_KEY)) {
            this.setWisdom(jsonData.getInt(WISDOM_KEY));
        }
        if (jsonData.has(CHARISMA_KEY)) {
            this.setCharisma(jsonData.getInt(CHARISMA_KEY));
        }
        
        if (jsonData.has(FORTITUDE_KEY)) {
            this.setFortitude(jsonData.getInt(FORTITUDE_KEY));
        }
        if (jsonData.has(REFLEXES_KEY)) {
            this.setReflexes(jsonData.getInt(REFLEXES_KEY));
        }
        if (jsonData.has(WILL_KEY)) {
            this.setWill(jsonData.getInt(WILL_KEY));
        }
        
        if (jsonData.has(ATTACK_BONUS_KEY)) {
            this.setAttackBonus(jsonData.getInt(ATTACK_BONUS_KEY));
        }
        if (jsonData.has(ARMOR_CLASS_KEY)) {
            this.setArmorClass(jsonData.getInt(ARMOR_CLASS_KEY));
        }
        if (jsonData.has(SPELL_POWER_KEY)) {
            this.setSpellPower(jsonData.getInt(SPELL_POWER_KEY));
        }
        if (jsonData.has(SPELL_RESISTANCE_KEY)) {
            this.setSpellResistance(jsonData.getInt(SPELL_RESISTANCE_KEY));
        }
    }
    
}
