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
import java.util.HashMap
import java.util.TreeSet

/**

 * @author Nathan Templon
 */
public class Magus : CharacterClass() {


    // Properties
    override val classSkills = Collections.unmodifiableSet(TreeSet(listOf(Skills.CRAFT, Skills.DIPLOMACY, Skills.PERCEPTION, Skills.SENSE_MOTIVE, Skills.SPELLCRAFT, Skills.USE_MAGIC_DEVICE)))
    override val healthPerLevel: Int = 3
    override val startingHealth: Int = 8
    override val skillPointsPerLevel: Int = 5
    override val attackBonus: Int
        get() = CharacterClass.poorAttackBonus(this.level)
    override val fortitude: Int
        get() = CharacterClass.poorSave(this.level)
    override val reflexes: Int
        get() = CharacterClass.poorSave(this.level)
    override val will: Int
        get() = CharacterClass.goodSave(this.level)
    override val textureSetName: String = "magus"


    // Public Methods
    override fun levelUp() {
        if (this.level < CharacterClass.MAX_LEVEL) {
            super.levelUp()
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        super.write(json)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        super.read(json, jsonData)
    }

    companion object {

        // Constants
        private val SPELL_PROGRESSION = Collections.unmodifiableMap(getSpellProgression())


        // Static Methods
        private fun getSpellProgression(): Map<Int, Map<Int, Int>> {
            val spells = arrayOf(intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0), intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0), intArrayOf(3, 1, 0, 0, 0, 0, 0, 0, 0), intArrayOf(3, 2, 0, 0, 0, 0, 0, 0, 0), intArrayOf(4, 2, 1, 0, 0, 0, 0, 0, 0), intArrayOf(4, 3, 2, 0, 0, 0, 0, 0, 0), intArrayOf(4, 3, 2, 1, 0, 0, 0, 0, 0), intArrayOf(4, 3, 3, 2, 0, 0, 0, 0, 0), intArrayOf(4, 4, 3, 2, 1, 0, 0, 0, 0), intArrayOf(4, 4, 3, 3, 2, 0, 0, 0, 0), intArrayOf(4, 4, 4, 3, 2, 1, 0, 0, 0), intArrayOf(4, 4, 4, 3, 3, 2, 0, 0, 0), intArrayOf(4, 4, 4, 4, 3, 2, 1, 0, 0), intArrayOf(4, 4, 4, 4, 3, 3, 2, 0, 0), intArrayOf(4, 4, 4, 4, 4, 3, 2, 1, 0), intArrayOf(4, 4, 4, 4, 4, 3, 3, 2, 0), intArrayOf(4, 4, 4, 4, 4, 4, 3, 2, 1), intArrayOf(4, 4, 4, 4, 4, 4, 3, 3, 2), intArrayOf(4, 4, 4, 4, 4, 4, 4, 3, 3), intArrayOf(4, 4, 4, 4, 4, 4, 4, 4, 4))

            val progression = HashMap<Int, Map<Int, Int>>()
            var characterLevel = 0
            for (levelProgression in spells) {
                val levelMap = HashMap<Int, Int>()
                var spellLevel = 1
                for (spellCount in levelProgression) {
                    levelMap.put(spellLevel, spellCount)
                    spellLevel++
                }

                progression.put(characterLevel, levelMap)

                characterLevel++
            }

            return progression
        }
    }

}
