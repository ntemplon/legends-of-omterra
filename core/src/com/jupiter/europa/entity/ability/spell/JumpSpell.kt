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

package com.jupiter.europa.entity.ability.spell

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.ability.AbilityCategory
import com.jupiter.europa.entity.ability.Action
import com.jupiter.europa.entity.ability.Cost
import com.jupiter.europa.entity.component.ResourceComponent
import com.jupiter.europa.entity.messaging.Message
import com.jupiter.europa.entity.messaging.TeleportCompleteMessage
import com.jupiter.europa.entity.messaging.TeleportRequestMessage
import com.jupiter.europa.entity.stats.characterclass.Magus
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.world.Level
import com.jupiter.ganymede.event.Listener
import java.awt.Point
import java.awt.Rectangle
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/25/15.
 */
Spell(characterClass = javaClass<Magus>(), level = 1)
public class JumpSpell(private val entity: Entity) : Ability {


    // Public Methods
    override val name: String = "Jump"
    override val description: String = "Jumps to any point within 30 feet."
    override val cost: Cost = COST
    override val category: AbilityCategory = SpellCategories.LEVEL_ONE
    override val icon: TextureRegion = INNER_ICON

    override val action: Action = object : Action(this.entity), Listener<Message> {
        override val targets: List<(Level, Point) -> Boolean> = listOf(
                { level, point ->
                    if (Families.positionables.matches(entity)) {
                        val currentPosition = Mappers.position[this.entity].tilePosition
                        val dist = Math.abs(currentPosition.x - point.x) + Math.abs(currentPosition.y - point.y)
                        if (dist <= RANGE) {
                            val rect = Rectangle(point.x, point.y, 1, 1)
                            !level.collides(rect)
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
        )

        override fun apply(targets: List<Point>) {
            EuropaGame.game.messageSystem.subscribe(this, javaClass<TeleportCompleteMessage>())
            EuropaGame.game.messageSystem.publish(TeleportRequestMessage(entity, targets[0]))
        }

        override fun handle(message: Message) {
            if (message is TeleportCompleteMessage && message.entity === entity) {
                EuropaGame.game.messageSystem.unsubscribe(this, javaClass<TeleportCompleteMessage>())
                this.complete()
            }
        }
    }

    companion object {

        // Static Members
        private val COST = Cost.constant(ResourceComponent.Resources.MANA, 1)
        private val INNER_ICON: TextureRegion by Delegates.lazy({ EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("jump-spell") })

        public val RANGE: Int = 6
    }
}
