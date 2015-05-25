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
package com.jupiter.europa.entity.stats.characterclass;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.entity.stats.SkillSet;
import com.jupiter.europa.entity.stats.SkillSet.Skills;

import java.util.*;

/**
 *
 * @author Nathan Templon
 */
public class Magus extends CharacterClass {

    // Constants
    private static final Map<Integer, Map<Integer, Integer>> SPELL_PROGRESSION = Collections.unmodifiableMap(getSpellProgression());


    // Static Methods
    private static Map<Integer, Map<Integer, Integer>> getSpellProgression() {
        int[][] spells = {
                {2, 0, 0, 0, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0, 0, 0, 0},
                {3, 1, 0, 0, 0, 0, 0, 0, 0},
                {3, 2, 0, 0, 0, 0, 0, 0, 0},
                {4, 2, 1, 0, 0, 0, 0, 0, 0},
                {4, 3, 2, 0, 0, 0, 0, 0, 0},
                {4, 3, 2, 1, 0, 0, 0, 0, 0},
                {4, 3, 3, 2, 0, 0, 0, 0, 0},
                {4, 4, 3, 2, 1, 0, 0, 0, 0},
                {4, 4, 3, 3, 2, 0, 0, 0, 0},
                {4, 4, 4, 3, 2, 1, 0, 0, 0},
                {4, 4, 4, 3, 3, 2, 0, 0, 0},
                {4, 4, 4, 4, 3, 2, 1, 0, 0},
                {4, 4, 4, 4, 3, 3, 2, 0, 0},
                {4, 4, 4, 4, 4, 3, 2, 1, 0},
                {4, 4, 4, 4, 4, 3, 3, 2, 0},
                {4, 4, 4, 4, 4, 4, 3, 2, 1},
                {4, 4, 4, 4, 4, 4, 3, 3, 2},
                {4, 4, 4, 4, 4, 4, 4, 3, 3},
                {4, 4, 4, 4, 4, 4, 4, 4, 4}
        };

        Map<Integer, Map<Integer, Integer>> progression = new HashMap<>();
        int characterLevel = 0;
        for (int[] levelProgression : spells) {
            Map<Integer, Integer> levelMap = new HashMap<>();
            int spellLevel = 1;
            for (int spellCount : levelProgression) {
                levelMap.put(spellLevel, spellCount);
                spellLevel++;
            }

            progression.put(characterLevel, levelMap);

            characterLevel++;
        }

        return progression;
    }


    // Fields
    private final Set<SkillSet.Skills> classSkills = new TreeSet<>(Arrays.asList(new SkillSet.Skills[] {
        Skills.CRAFT,
        Skills.DIPLOMACY,
        Skills.PERCEPTION,
        Skills.SENSE_MOTIVE,
        Skills.SPELLCRAFT,
        Skills.USE_MAGIC_DEVICE
    }));
    private final Set<SkillSet.Skills> classSkillsAccess = Collections.unmodifiableSet(this.classSkills);
    

    // Properties
    @Override
    public int getHealthPerLevel() {
        return 3;
    }

    @Override
    public int getStartingHealth() {
        return 8;
    }

    @Override
    public int getSkillPointsPerLevel() {
        return 5;
    }

    @Override
    public int getAttackBonus() {
        return CharacterClass.poorAttackBonus(this.getLevel());
    }

    @Override
    public int getFortitude() {
        return CharacterClass.poorSave(this.getLevel());
    }

    @Override
    public int getReflexes() {
        return CharacterClass.poorSave(this.getLevel());
    }

    @Override
    public int getWill() {
        return CharacterClass.goodSave(this.getLevel());
    }

    @Override
    public String getTextureSetName() {
        return "magus";
    }
    
    @Override
    public Set<Skills> getClassSkills() {
        return this.classSkillsAccess;
    }
    
    
    // Initialization
    public Magus() {
        
    }
    
    
    // Public Methods
    @Override
    public void levelUp() {
        if (this.getLevel() < MAX_LEVEL) {
            super.levelUp();
        }
    }
    
    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        super.write(json);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
    }
    
}
