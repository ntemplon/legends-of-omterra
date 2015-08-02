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
 *
 */

package com.jupiter.europa.entity.stats.race

import com.jupiter.europa.entity.stats.SkillSet

/**
 * Created by nathan on 7/4/15.
 */
public interface Race {
    val textureString: String
    val strengthBonus: Int
        get() = 0
    val constitutionBonus: Int
        get() = 0
    val dexterityBonus: Int
        get() = 0
    val intelligenceBonus: Int
        get() = 0
    val wisdomBonus: Int
        get() = 0
    val charismaBonus: Int
        get() = 0
    val firstLevelFeats: Int
        get() = 0
    val bonusSkillPoints: Int
        get() = 0
    val classSkills: Set<SkillSet.Skills>
        get() = setOf()
    val movementSpeed: Int
        get() = 6
}

public enum class PlayerRaces : Race {
    Human() {
        override val textureString: String = "human"
        override val firstLevelFeats: Int = 1
        override val bonusSkillPoints: Int = 5
        override val classSkills: Set<SkillSet.Skills> = setOf(*SkillSet.Skills.values())
    },
    Elf() {
        override val textureString: String = "elf"
        override val strengthBonus: Int = -5
        override val dexterityBonus: Int = 5
        override val wisdomBonus: Int = 5
    },
    Dwarf() {
        override val textureString: String = "dwarf"
        override val strengthBonus: Int = 5
        override val constitutionBonus: Int = 5
        override val charismaBonus: Int = -5
        override val movementSpeed: Int = 4
    },
    Goblin() {
        override val textureString: String = "goblin"
        override val dexterityBonus: Int = 5
        override val charismaBonus: Int = 5
        override val wisdomBonus: Int = -5
    }
}