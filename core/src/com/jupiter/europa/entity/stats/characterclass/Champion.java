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
package com.jupiter.europa.entity.stats.characterclass;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 *
 * @author Nathan Templon
 */
public class Champion extends CharacterClass {
    
    // Properties
    @Override
    public String getTextureSetName() {
        return "champion";
    }

    @Override
    public int getHealthPerLevel() {
        return 6;
    }

    @Override
    public int getStartingHealth() {
        return 20;
    }

    @Override
    public int getSkillPointsPerLevel() {
        return 0;
    }

    @Override
    public int getAttackBonus() {
        return CharacterClass.goodAttackBonus(this.getLevel());
    }

    @Override
    public int getFortitude() {
        return CharacterClass.goodSave(this.getLevel());
    }

    @Override
    public int getReflexes() {
        return CharacterClass.poorSave(this.getLevel());
    }

    @Override
    public int getWill() {
        return CharacterClass.goodSave(this.getLevel());
    }


    // Initialization
    public Champion() {

    }
    
    @Override
    public void create() {
        super.create();
        this.getFeatPool().increaseCapacity(1);
    }
    
    @Override
    public void initialize() {
        super.initialize();
    }


    // Public Methods
    @Override
    public void levelUp() {
        if (this.getLevel() < MAX_LEVEL) {
            super.levelUp();
            
            if (this.getLevel() % 4 == 0) {
                this.getFeatPool().increaseCapacity(1);
            }
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
