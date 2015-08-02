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

package com.jupiter.europa.entity

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.component.*
import com.jupiter.europa.entity.effects.BasicAbilitiesEffect
import com.jupiter.europa.entity.messaging.RequestEffectAddMessage
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.stats.SkillSet
import com.jupiter.europa.entity.stats.SkillSet.Skills
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import com.jupiter.europa.entity.stats.race.Race
import com.jupiter.europa.geometry.Size
import com.jupiter.europa.io.FileLocations
import java.awt.Point
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.HashSet

/**

 * @author Nathan Templon
 */
public class Party : Serializable {


    // Fields
    private val player1: EuropaEntity? = null

    private val activePartyMembers = ArrayList<EuropaEntity>()
    private var partyMembers: MutableMap<String, EuropaEntity> = hashMapOf()


    // Properties
    public fun getActivePartyMembers(): Array<EuropaEntity> {
        return this.activePartyMembers.toArray<EuropaEntity>(arrayOfNulls<EuropaEntity>(this.activePartyMembers.size()))
    }


    // Public Methods
    public fun addPlayer(entity: EuropaEntity) {
        this.partyMembers.put(Mappers.name.get(entity).name, entity)
    }

    public fun selectPlayer(entity: EuropaEntity) {
        this.activePartyMembers.add(entity)
    }


    // Serializable (Json) implementation
    override fun write(json: Json) {
        json.writeValue(PARTY_MEMBERS_KEY, this.partyMembers, javaClass<HashMap<Any, Any>>())

        json.writeArrayStart(ACTIVE_PARTY_MEMBERS_KEY)
        for (entity in this.getActivePartyMembers()) {
            json.writeValue(Mappers.name.get(entity).name)
        }
        json.writeArrayEnd()
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(PARTY_MEMBERS_KEY)) {
            this.partyMembers = json.fromJson(javaClass<HashMap<String, EuropaEntity>>(), jsonData.get(PARTY_MEMBERS_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS))
        }

        val members = jsonData.get(ACTIVE_PARTY_MEMBERS_KEY)

        for (i in 0..members.size - 1) {
            this.activePartyMembers.add(this.partyMembers.get(members.getString(i)))
        }
    }

    companion object {

        // Constants
        public val PARTY_MEMBERS_KEY: String = "party-members"
        public val ACTIVE_PARTY_MEMBERS_KEY: String = "active-party-members"


        // Static Methods
        public fun createPlayer(name: String, charClass: Class<out CharacterClass>, race: Race, attributes: AttributeSet): EuropaEntity {
            // NOTE: Order of component creation is important!
            val entity = EuropaEntity()

            entity.add(NameComponent(name))

            // Effects
            val effectsComponent = EffectsComponent()
            entity.add(effectsComponent)
            EuropaGame.game.messageSystem.publish(RequestEffectAddMessage(entity, BasicAbilitiesEffect()))

            entity.add(RaceComponent(race))

            val classComponent = CharacterClassComponent(charClass, entity)
            classComponent.characterClass.featPool.increaseCapacity(race.firstLevelFeats) // First level bonus feats
            val textureSetName = race.textureString + "-" + classComponent.characterClass.textureSetName

            entity.add(MovementResourceComponent(FileLocations.CHARACTER_SPRITES, textureSetName))
            entity.add(PositionComponent(null, Point(19, 25), 0))
            entity.add(SizeComponent(Size(1, 1)))
            entity.add(CollisionComponent(Mappers.position.get(entity).tilePosition, Mappers.size.get(entity).size))
            entity.add(WalkComponent())
            entity.add(RenderComponent(Sprite(Mappers.moveTexture.get(entity).frontStandTexture)))
            entity.add(AttributesComponent(attributes).applyRace(race))
            val res = ResourceComponent()
            entity.add(res)

            // Skills
            val classSkills = HashSet<Skills>()
            classSkills.addAll(classComponent.characterClass.classSkills)
            classSkills.addAll(race.classSkills)
            val sorted = ArrayList(classSkills)
            Collections.sort(sorted)
            entity.add(SkillsComponent(SkillSet(), sorted))

            // Abilities
            val abilities = AbilityComponent()
            entity.add(abilities)

            entity.add(classComponent)

            entity.initializeComponents()

            // Final Initialization
            classComponent.characterClass.onFirstCreation()
            res.onCreateNew()

            return entity
        }
    }

}
