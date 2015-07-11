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

package com.jupiter.europa.entity.stats

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import java.util.EnumMap
import kotlin.properties.Delegates

public class SkillSet : Json.Serializable {

    public enum class Skills : Comparable<Skills> {
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

        public val displayName: String

        init {
            val words: List<String> = this.toString().splitBy("_")
            val sb = StringBuilder()
            for ((index, word) in words.withIndex()) {
                sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase())
                if (index < words.size() - 1) {
                    sb.append(" ")
                }
            }
            this.displayName = sb.toString()
        }

        companion object {
            private val displayNameMap: Map<String, Skills> by Delegates.lazy {
                Skills.values().map { skill ->
                    Pair(skill.displayName, skill)
                }.toMap()
            }

            public fun getByDisplayName(displayName: String): Skills? {
                return if (this.displayNameMap.containsKey(displayName)) {
                    this.displayNameMap[displayName]
                } else {
                    null
                }
            }
        }
    }


    // Properties
    private val skills: MutableMap<Skills, Int> = EnumMap<Skills, Int>(javaClass<Skills>())


    // Initialization
    init {
        for (skill in Skills.values()) {
            this.skills[skill] = 0
        }
    }


    // Public Methods
    public fun getSkill(skill: Skills): Int {
        if (this.skills.containsKey(skill)) {
            return this.skills[skill] ?: 0
        }
        return 0
    }

    public fun setSkill(skill: Skills, value: Int) {
        this.skills[skill] = value
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        this.skills.keySet().forEach { skill ->
            json.writeValue(skill.toString(), this.skills[skill])
        }
    }

    override fun read(json: Json, jsonData: JsonValue) {
        jsonData.forEach { value ->
            val skill = Skills.valueOf(value.name())
            this.skills[skill] = value.asInt()
        }
    }

}