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
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import java.util.Collections
import java.util.EnumMap
import java.util.LinkedHashSet
import kotlin.properties.Delegates

/**

 * @author Nathan Templon
 */
public class AttributeSet : Serializable {

    // Enumerations
    public enum class Attributes {
        STRENGTH,
        CONSTITUTION,
        DEXTERITY,
        INTELLIGENCE,
        WISDOM,
        CHARISMA,
        FORTITUDE,
        REFLEXES,
        WILL,
        ATTACK_BONUS,
        ARMOR_CLASS,
        INITIATIVE,
        MOVEMENT_SPEED,
        SPELL_POWER,
        SPELL_RESISTANCE,
        SPELL_PENETRATION;


        // Fields
        public val displayName: String


        init {
            val words = this.toString().splitBy("_")
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

            // Static Fields
            private val byDisplayName: Map<String, Attributes> by Delegates.lazy {
                Attributes.values().map { attr ->
                    Pair(attr.displayName, attr)
                }.toMap()
            }


            // Static Methods
            public fun getByDisplayName(displayName: String): Attributes? {
                if (byDisplayName.containsKey(displayName)) {
                    return byDisplayName.get(displayName)
                }
                return null
            }
        }
    }


    // Fields
    private val attributes = EnumMap<Attributes, Int>(javaClass<Attributes>())


    // Properties
    public fun getAttribute(attribute: Attributes): Int {
        if (this.attributes.containsKey(attribute)) {
            return this.attributes.get(attribute)
        }
        return 0
    }

    public fun setAttribute(attribute: Attributes, value: Int) {
        this.attributes.put(attribute, value)
    }


    // Initialization
    public constructor() {
        for (attr in Attributes.values()) {
            this.attributes.put(attr, 0)
        }
    }

    public constructor(source: AttributeSet) {
        for (attr in Attributes.values()) {
            this.attributes.put(attr, source.getAttribute(attr))
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        this.attributes.keySet().forEach { attr ->
            json.writeValue(attr.toString(), this.attributes[attr])
        }
    }

    override fun read(json: Json, jsonData: JsonValue) {
        jsonData.forEach { value ->
            val attr = Attributes.valueOf(value.name())
            this.attributes[attr] = value.asInt()
        }
    }

    companion object {
        // Constants
        public val PRIMARY_ATTRIBUTES: Set<Attributes> = getPrimaryAttributes()


        // Static Methods
        private fun getPrimaryAttributes(): Set<Attributes> {
            val attrs = LinkedHashSet<Attributes>()

            attrs.add(Attributes.STRENGTH)
            attrs.add(Attributes.CONSTITUTION)
            attrs.add(Attributes.DEXTERITY)
            attrs.add(Attributes.INTELLIGENCE)
            attrs.add(Attributes.WISDOM)
            attrs.add(Attributes.CHARISMA)

            return Collections.unmodifiableSet(attrs)
        }
    }

}
