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

package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import java.util.EnumMap

/**
 * Created by nathan on 6/16/15.
 */
public class ResourceComponent() : Component(), Json.Serializable, OwnedComponent {

    // Enumerations
    public enum class Resources(public val drawColor: Color) {

        public val displayName: String = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase()

        HEALTH(Color(Color.RED)),
        MANA(Color(Color.BLUE)),
        STAMINA(Color(Color.GREEN)),
        AETHER(Color(0.5f, 0.5f, 0.5f, 0.0f));
    }


    // Properties
    private val maxAmounts = EnumMap<Resources, Int>(javaClass<Resources>())
    private val currentAmounts = EnumMap<Resources, Int>(javaClass<Resources>())

    public override var owner: Entity? = null
        public set(value) {
            this.$owner = value
            this.updateMaxResources()
        }

    public fun getCurrent(res: Resources): Int = this.currentAmounts.get(res)

    public fun setCurrent(res: Resources, amount: Int) {
        this.currentAmounts.put(res, amount)
    }

    public fun getMax(res: Resources): Int = this.maxAmounts.get(res)


    // Public Methods
    public fun updateMaxResources() {
        val entity = this.owner

        // TODO: Effects that add mana, health, etc.
        if (entity != null && Families.classed.matches(entity) && Families.attributed.matches(entity)) {
            val charClass: CharacterClass = Mappers.characterClass[entity].characterClass
            val attributes: AttributesComponent = Mappers.attributes[entity]

            this.maxAmounts[Resources.HEALTH] = charClass.startingHealth + charClass.healthPerLevel * (charClass.level - 1) + ((attributes.baseAttributes[AttributeSet.Attributes.CONSTITUTION] / 5) * (charClass.level + 1))
            this.maxAmounts[Resources.STAMINA] = charClass.stamina + bonusStamina(attributes.baseAttributes[AttributeSet.Attributes.CONSTITUTION])
            this.maxAmounts[Resources.MANA] = charClass.mana + bonusMana(attributes.baseAttributes[charClass.manaAttribute])
            this.maxAmounts[Resources.AETHER] = charClass.aether + bonusAether(attributes.baseAttributes[charClass.aetherAttribute])
        }
    }


    // Internal Methods
    internal fun onCreateNew(): ResourceComponent {
        for (res in this.maxAmounts.keySet()) {
            this.currentAmounts[res] = this.maxAmounts[res]
        }
        return this
    }


    public override fun read(json: Json, jsonValue: JsonValue) {
        if (jsonValue.hasChild(MAX_KEY)) {
            for (child in jsonValue.get(MAX_KEY)) {
                try {
                    val name = child.name()
                    val res = Resources.valueOf(name)
                    this.maxAmounts.put(res, child.asInt())
                } catch(ex: Exception) {

                }
            }
        }

        if (jsonValue.hasChild(CURRENT_KEY)) {
            for (child in jsonValue.get(CURRENT_KEY)) {
                try {
                    val name = child.name()
                    val res = Resources.valueOf(name)
                    this.currentAmounts.put(res, child.asInt())
                } catch(ex: Exception) {

                }
            }
        }
    }

    public override fun write(json: Json) {
        json.writeObjectStart(MAX_KEY)
        Resources.values().forEach { res -> json.writeValue(res.toString(), this.maxAmounts.get(res)) }
        json.writeObjectEnd()

        json.writeObjectStart(CURRENT_KEY)
        Resources.values().forEach { res -> json.writeValue(res.toString(), this.currentAmounts.get(res)) }
        json.writeObjectEnd()
    }


    // Initialization
    init {
        for (resource in Resources.values()) {
            this.maxAmounts.put(resource, 0)
            this.currentAmounts.put(resource, 0)
        }
    }


    companion object {
        public val MAX_KEY: String = "max"
        public val CURRENT_KEY: String = "current"

        public val MINIMAL_STAMINA: Map<Int, Int> = mapOf(
                1 to 0,
                2 to 0,
                3 to 0,
                4 to 0,
                5 to 0,
                6 to 1,
                7 to 1,
                8 to 1,
                9 to 1,
                10 to 2,
                11 to 2,
                12 to 4,
                13 to 4,
                14 to 4,
                15 to 7,
                16 to 8,
                17 to 9,
                18 to 9,
                19 to 14,
                20 to 16
        )

        public val MODERATE_STAMINA: Map<Int, Int> = mapOf(
                1 to 1,
                2 to 1,
                3 to 2,
                4 to 3,
                5 to 3,
                6 to 4,
                7 to 7,
                8 to 8,
                9 to 10,
                10 to 12,
                11 to 14,
                12 to 16,
                13 to 19,
                14 to 22,
                15 to 25,
                16 to 30,
                17 to 34,
                18 to 40,
                19 to 46,
                20 to 48
        )

        public val HIGH_STAMINA: Map<Int, Int> = mapOf(
                1 to 1,
                2 to 2,
                3 to 4,
                4 to 6,
                5 to 8,
                6 to 11,
                7 to 15,
                8 to 19,
                9 to 24,
                10 to 30,
                11 to 35,
                12 to 41,
                13 to 46,
                14 to 51,
                15 to 56,
                16 to 61,
                17 to 65,
                18 to 70,
                19 to 78,
                20 to 78
        )

        public val EXTENSIVE_STAMINA: Map<Int, Int> = mapOf(
                1 to 2,
                2 to 4,
                3 to 5,
                4 to 9,
                5 to 12,
                6 to 17,
                7 to 21,
                8 to 28,
                9 to 33,
                10 to 42,
                11 to 48,
                12 to 56,
                13 to 62,
                14 to 69,
                15 to 74,
                16 to 80,
                17 to 85,
                18 to 91,
                19 to 96,
                20 to 100
        )

        public fun bonusStamina(constitution: Int): Int = tieredMap(BONUS_STAMINA, constitution, 0)

        private val BONUS_STAMINA: Map<Int, Int> = mapOf(
                0 to 0,
                5 to 1,
                10 to 2,
                15 to 3,
                20 to 6,
                25 to 9,
                30 to 14,
                35 to 20,
                40 to 27,
                45 to 36,
                50 to 41,
                55 to 46,
                60 to 54,
                65 to 63,
                70 to 67,
                75 to 74
        )

        public fun staminaRegen(constitution: Int): Float = tieredMap(STAMINA_REGEN, constitution, 0.02f)

        private val STAMINA_REGEN: Map<Int, Float> = mapOf(
                0 to 0.02f,
                5 to 0.05f,
                10 to 0.08f,
                15 to 0.11f,
                20 to 0.14f,
                25 to 0.17f,
                30 to 0.21f,
                35 to 0.24f,
                40 to 0.27f,
                45 to 0.30f,
                50 to 0.34f,
                55 to 0.37f,
                60 to 0.40f,
                65 to 0.43f,
                70 to 0.46f,
                75 to 0.50f
        )

        private fun tieredMap<T>(map: Map<Int, T>, value: Int, default: T): T {
            val nearestFive = (Math.floor(value.toDouble() / 5.0) * 5.0).toInt()
            return map[if (nearestFive < 0) {
                0
            } else if (nearestFive > 75) {
                75
            } else {
                nearestFive
            }] ?: default
        }

        public val MINIMAL_MANA: Map<Int, Int> = MINIMAL_STAMINA

        public val MODERATE_MANA: Map<Int, Int> = MODERATE_STAMINA

        public val HIGH_MANA: Map<Int, Int> = HIGH_STAMINA

        public val EXTENSIVE_MANA: Map<Int, Int> = EXTENSIVE_STAMINA

        public fun bonusMana(keyAttribute: Int): Int = tieredMap(BONUS_MANA, keyAttribute, 0)

        private val BONUS_MANA: Map<Int, Int> = BONUS_STAMINA

        public fun manaRegen(keyAttribute: Int): Float = tieredMap(MANA_REGEN, keyAttribute, 0.0f)

        private val MANA_REGEN: Map<Int, Float> = mapOf(
                0 to 0.0f,
                5 to 1.6f,
                10 to 2.2f,
                15 to 2.8f,
                20 to 3.4f,
                25 to 4f,
                30 to 4.6f,
                35 to 5.2f,
                40 to 5.8f,
                45 to 6.4f,
                50 to 7f,
                55 to 7.6f,
                60 to 8.2f,
                65 to 8.8f,
                70 to 9.4f,
                75 to 10f
        )

        public val MINIMAL_AETHER: Map<Int, Int> = mapOf(
                1 to 0,
                2 to 0,
                3 to 0,
                4 to 0,
                5 to 0,
                6 to 1,
                7 to 1,
                8 to 1,
                9 to 1,
                10 to 4,
                11 to 4,
                12 to 9,
                13 to 9,
                14 to 10,
                15 to 17,
                16 to 20,
                17 to 25,
                18 to 26,
                19 to 41,
                20 to 48
        )

        public val MODERATE_AETHER: Map<Int, Int> = mapOf(
                1 to 1,
                2 to 2,
                3 to 4,
                4 to 5,
                5 to 6,
                6 to 9,
                7 to 14,
                8 to 17,
                9 to 22,
                10 to 29,
                11 to 34,
                12 to 41,
                13 to 50,
                14 to 57,
                15 to 67,
                16 to 81,
                17 to 95,
                18 to 113,
                19 to 133,
                20 to 144
        )

        public val HIGH_AETHER: Map<Int, Int> = mapOf(
                1 to 2,
                2 to 4,
                3 to 7,
                4 to 11,
                5 to 16,
                6 to 24,
                7 to 33,
                8 to 44,
                9 to 56,
                10 to 72,
                11 to 88,
                12 to 104,
                13 to 120,
                14 to 136,
                15 to 152,
                16 to 168,
                17 to 184,
                18 to 200,
                19 to 216,
                20 to 232
        )

        public val EXTENSIVE_AETHER: Map<Int, Int> = mapOf(
                1 to 3,
                2 to 5,
                3 to 8,
                4 to 14,
                5 to 19,
                6 to 29,
                7 to 37,
                8 to 51,
                9 to 63,
                10 to 81,
                11 to 97,
                12 to 115,
                13 to 131,
                14 to 149,
                15 to 165,
                16 to 183,
                17 to 199,
                18 to 217,
                19 to 233,
                20 to 249
        )

        public fun bonusAether(keyAttribute: Int): Int = tieredMap(BONUS_AETHER, keyAttribute, 0)

        private val BONUS_AETHER: Map<Int, Int> = mapOf(
                0 to 0,
                5 to 1,
                10 to 4,
                15 to 9,
                20 to 16,
                25 to 26,
                30 to 40,
                35 to 58,
                40 to 80,
                45 to 107,
                50 to 121,
                55 to 136,
                60 to 161,
                65 to 188,
                70 to 200,
                75 to 220
        )
    }

}