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
package com.jupiter.europa.entity.stats.race;

import com.jupiter.europa.entity.stats.SkillSet.Skills;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public interface Race {
   
    public enum PlayerRaces implements Race {
        Human() {
            @Override
            public String getTextureString() { return "human"; }
            
            @Override
            public int getNumberOfFeatsAtFirstLevel() {return 1;}
            
            @Override
            public int getBonusSkillPoints() { return 1; }
            
            @Override
            public Set<Skills> getClassSkills() {
                return new HashSet<>(Arrays.asList(Skills.values()));
            }
        },
        Elf() {
            @Override
            public String getTextureString() { return "elf"; }
            
            @Override
            public int getStrengthBonus() { return -1; }
            
            @Override
            public int getDexterityBonus() { return 1; }
            
            @Override
            public int getWisdomBonus() { return 1; }
        },
        Dwarf() {
            @Override
            public String getTextureString() { return "dwarf"; }
            
            @Override
            public int getStrengthBonus() { return 1; }
            
            @Override
            public int getConstitutionBonus() { return 0; }
            
            @Override
            public int getCharismaBonus() { return -1; }
        },
        Goblin() {
            @Override
            public String getTextureString() { return "goblin"; }
            
            @Override
            public int getDexterityBonus() { return 1; }
            
            @Override
            public int getCharismaBonus() { return 1; }
            
            @Override
            public int getWisdomBonus() { return -1; }
        }
    }
    
    // Public Methods
    public String getTextureString();
    
    public default int getStrengthBonus() {
        return 0;
    }
    
    public default int getConstitutionBonus() {
        return 0;
    }
    
    public default int getDexterityBonus() {
        return 0;
    }
    
    public default int getIntelligenceBonus() {
        return 0;
    }
    
    public default int getWisdomBonus() {
        return 0;
    }
    
    public default int getCharismaBonus() {
        return 0;
    }
    
    public default int getNumberOfFeatsAtFirstLevel() {
        return 0;
    }
    
    public default int getBonusSkillPoints() {
        return 0;
    }
    
    public default Set<Skills> getClassSkills() {
        return new HashSet<>();
    }
    
}
