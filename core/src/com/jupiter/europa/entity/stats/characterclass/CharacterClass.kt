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

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.effects.Effect
import com.jupiter.europa.entity.messaging.RequestEffectAddMessage
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.stats.SkillSet.Skills
import com.jupiter.europa.entity.traits.EffectPool
import com.jupiter.europa.entity.traits.EffectPool.TraitPoolEvent
import com.jupiter.europa.entity.traits.feat.Feat
import com.jupiter.europa.entity.traits.feat.FeatPool
import com.jupiter.europa.util.Initializable
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener
import org.reflections.Reflections
import java.util.LinkedHashSet

/**

 * @author Nathan Templon
 */
public abstract class CharacterClass : Serializable, Initializable {


    // Properties
    private val levelUp = Event<LevelUpArgs>()

    private var ownerId: Long = -1

    protected val abilityPoolsInternal: MutableSet<EffectPool<out Effect>> = LinkedHashSet<EffectPool<out Effect>>()

    public var level: Int = 0
        private set

    public var owner: Entity? = null
        get() = this.$owner
        set(value: Entity?) {
            this.$owner = value

            this.abilityPoolsInternal.forEach { pool ->
                pool.owner = this.owner
                pool.autoQualify = true
                pool.reassesQualifications()
            }
        }

    public val abilityPools: Set<EffectPool<out Effect>>
        get() = this.abilityPoolsInternal

    public val maxPointsPerSkill: Int
        get() = 5 * (this.level + 3)

    public var featPool: FeatPool = FeatPool()
        private set

    public var availableSkillPoints: Int = 0
        private set

    public abstract val healthPerLevel: Int
    public abstract val startingHealth: Int
    public abstract val skillPointsPerLevel: Int
    public abstract val attackBonus: Int
    public abstract val fortitude: Int
    public abstract val reflexes: Int
    public abstract val will: Int
    public abstract val stamina: Int
    public abstract val mana: Int
    public abstract val manaAttribute: AttributeSet.Attributes
    public abstract val aether: Int
    public abstract val aetherAttribute: AttributeSet.Attributes
    public abstract val classSkills: Set<Skills>
    public abstract val textureSetName: String


    init {
        this.level = 1

        // Feats
        this.featPool.increaseCapacity(1)
        this.abilityPoolsInternal.add(this.featPool)
        this.featPool.addSelectionListener(Listener { args -> this.onFeatSelection(args) })
    }

    override fun initialize() {
        if (this.ownerId > 0) {
            this.owner = EuropaGame.game.lastIdMapping.get(this.ownerId)
        }

        // Skills
        this.computeAvailableSkillPoints()

        if (this.owner != null) {
            this.abilityPoolsInternal.forEach { pool ->
                pool.owner = this.owner
                pool.autoQualify = true
                pool.reassesQualifications()
            }
        }
    }

    public open fun onFirstCreation() {
    }

    // Public Methods
    public open fun levelUp() {
        if (this.level < MAX_LEVEL) {
            this.level++

            if (this.level % 3 == 0) {
                this.featPool!!.increaseCapacity(1)
            }

            this.levelUp.dispatch(LevelUpArgs(this, this.level))
        }
    }

    public fun addLevelUpListener(listener: Listener<LevelUpArgs>): Boolean {
        return this.levelUp.addListener(listener)
    }

    public fun removeLevelUpListener(listener: Listener<LevelUpArgs>): Boolean {
        return this.levelUp.removeListener(listener)
    }


    // Private Methods
    private fun computeAvailableSkillPoints() {
        val comp = Mappers.attributes[this.owner]
        val intelligence = comp.baseAttributes.getAttribute(AttributeSet.Attributes.INTELLIGENCE)
        val race = Mappers.race.get(this.owner).race
        val pointsPerLevel = this.skillPointsPerLevel +
                (race?.bonusSkillPoints ?: 0) +
                intelligence / 2
        this.availableSkillPoints = pointsPerLevel * (3 + this.level)
    }

    private fun onFeatSelection(event: TraitPoolEvent<Feat>) {
        if (this.owner != null) {
            EuropaGame.game.messageSystem.publish(RequestEffectAddMessage(this.owner!!, event.`trait`))
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(LEVEL_KEY, this.level)
        json.writeValue(OWNER_ID_KEY, this.owner!!.getId())
        json.writeValue(FEAT_POOL_KEY, this.featPool, this.featPool!!.javaClass)
        json.writeValue(AVAILABLE_SKILL_POINTS_KEY, this.availableSkillPoints)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(LEVEL_KEY)) {
            this.level = jsonData.getInt(LEVEL_KEY)
        }

        if (jsonData.has(OWNER_ID_KEY)) {
            this.ownerId = jsonData.getLong(OWNER_ID_KEY)
        }

        if (jsonData.has(FEAT_POOL_KEY)) {
            this.abilityPoolsInternal.remove(this.featPool)
            this.featPool = json.fromJson(javaClass<FeatPool>(), jsonData.get(FEAT_POOL_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS))
            this.featPool.addSelectionListener(Listener { args -> this.onFeatSelection(args) })
            this.abilityPoolsInternal.add(this.featPool)
        }

        if (jsonData.has(AVAILABLE_SKILL_POINTS_KEY)) {
            this.availableSkillPoints = jsonData.getInt(AVAILABLE_SKILL_POINTS_KEY)
        }
    }


    // Nested Classes
    public class LevelUpArgs(public val characterClass: CharacterClass, public val level: Int)

    companion object {

        // Constants
        public val MAX_LEVEL: Int = 20

        public val CLASS_LOOKUP: Map<String, Class<out CharacterClass>> = getClassMapping()
        public val AVAILABLE_CLASSES: List<String> = CLASS_LOOKUP.keySet().toList().sort()

        private val LEVEL_KEY = "level"
        private val OWNER_ID_KEY = "owner-id"
        private val FEAT_POOL_KEY = "feats"
        public val AVAILABLE_SKILL_POINTS_KEY: String = "skill-points"


        // Static Methods
        public fun getInstance(className: String): CharacterClass? {
            if (CLASS_LOOKUP.containsKey(className)) {
                val cc = CLASS_LOOKUP.get(className)
                try {
                    return cc?.newInstance()
                } catch (ex: IllegalAccessException) {
                    return null
                } catch (ex: InstantiationException) {
                    return null
                }


            } else {
                return null
            }
        }

        public fun goodSave(level: Int): Int {
            return (10.0 + (50.0 * ((level - 1.0) / (MAX_LEVEL - 1.0)))).toInt()
        }

        public fun poorSave(level: Int): Int {
            return (30.0 * ((level - 1.0) / (MAX_LEVEL - 1.0))).toInt()
        }

        public fun goodAttackBonus(level: Int): Int {
            var totalBonus = 0.0
            var currentBonus = level.toDouble()

            while (currentBonus > 0) {
                totalBonus += 5 * currentBonus
                currentBonus -= 5.0
            }

            return Math.round(totalBonus).toInt()
        }

        public fun averageAttackBonus(level: Int): Int {
            var totalBonus = 0.0
            var currentBonus = level * 0.75

            while (currentBonus > 0) {
                totalBonus += 5 * currentBonus
                currentBonus -= 5.0
            }

            return Math.round(totalBonus).toInt()
        }

        public fun poorAttackBonus(level: Int): Int {
            var totalBonus = 0.0
            var currentBonus = level * 0.5

            while (currentBonus > 0) {
                totalBonus += 5 * currentBonus
                currentBonus -= 5.0
            }

            return Math.round(totalBonus).toInt()
        }

        private fun getClassMapping(): Map<String, Class<out CharacterClass>> {
            val refl = Reflections("com.jupiter.europa")
            val classes = refl.getSubTypesOf(javaClass<CharacterClass>())

            return classes.map { type ->
                Pair(type.getSimpleName(), type)
            }.toMap()
        }
    }

}
