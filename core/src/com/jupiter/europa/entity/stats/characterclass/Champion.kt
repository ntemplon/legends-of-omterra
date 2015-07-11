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

package com.jupiter.europa.entity.stats.characterclass

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.stats.SkillSet.Skills
import java.util.Collections
import java.util.TreeSet

/**

 * @author Nathan Templon
 */
public class Champion : CharacterClass() {

    // Properties
    private val classSkillsInternal = TreeSet(listOf(Skills.INTIMIDATE, Skills.PERCEPTION, Skills.USE_MAGIC_DEVICE))

    override val textureSetName: String = "champion"
    override val healthPerLevel: Int = 6
    override val startingHealth: Int = 20
    override val skillPointsPerLevel: Int = 0
    override val attackBonus: Int
        get() = CharacterClass.goodAttackBonus(this.level)
    override val fortitude: Int
        get() = CharacterClass.goodSave(this.level)
    override val reflexes: Int
        get() = CharacterClass.poorSave(this.level)
    override val will: Int
        get() = CharacterClass.goodSave(this.level)
    override val classSkills: Set<Skills> = Collections.unmodifiableSet(this.classSkillsInternal)


    // Public Methods
    override fun initialize() {
        super.initialize()
    }

    override fun onFirstCreation() {
        this.featPool.increaseCapacity(1)
    }

    override fun levelUp() {
        if (this.level < CharacterClass.MAX_LEVEL) {
            super.levelUp()

            if (this.level % 4 == 0) {
                this.featPool.increaseCapacity(1)
            }
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        super.write(json)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        super.read(json, jsonData)
    }

}
