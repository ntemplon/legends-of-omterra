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
package com.jupiter.europa.entity.stats;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Nathan Templon
 */
public class SkillSet implements Serializable {

    // Enumerations
    public enum Skills {

        BLUFF,
        CRAFT,
        DIPLOMACY,
        DISABLE_DEVICE,
        HEAL,
        INTIMIDATE,
        LOCKPICKING,
        PERCEPTION,
        SENSE_MOTIVE,
        SPELLCRAFT,
        STEALTH,
        USE_MAGIC_DEVICE;

        // Fields
        private final String displayName;


        // Initialization
        Skills() {
            String[] words = this.toString().split("_");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                sb.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase());
                if (i < words.length - 1) {
                    sb.append(" ");
                }
            }
            this.displayName = sb.toString();
        }


        // Public Methods
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    
    // Fields
    private final Map<Skills, Integer> skills = new TreeMap<>();
    
    
    // Properties
    public int getSkill(Skills skill) {
        if (this.skills.containsKey(skill)) {
            return this.skills.get(skill);
        }
        return 0;
    }
    
    public void setSkill(Skills skill, int value) {
        this.skills.put(skill, value);
    }
    
    
    // Initialization
    public SkillSet() {
        for (Skills skill : Skills.values()) {
            this.skills.put(skill, 0);
        }
    }
    
    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        this.skills.keySet().stream().forEach((SkillSet.Skills skill) -> {
            json.writeValue(skill.toString(), this.skills.get(skill));
        });
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        jsonData.iterator().forEach((JsonValue value) -> {
            SkillSet.Skills skill = SkillSet.Skills.valueOf(value.name());
            if (skill != null) {
                this.skills.put(skill, value.asInt());
            }
        });
    }

}
