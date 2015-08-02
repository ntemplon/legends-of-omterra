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

package com.jupiter.europa.entity.ability

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.MovementSystem
import com.jupiter.europa.entity.behavior.Heuristics
import com.jupiter.europa.entity.messaging.InputRequestMessage
import com.jupiter.europa.entity.messaging.Message
import com.jupiter.europa.entity.messaging.MovementCompleteMessage
import com.jupiter.europa.entity.messaging.WalkRequestMessage
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.world.Level
import com.jupiter.ganymede.event.Listener
import java.awt.Point
import java.awt.Rectangle
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/21/15.
 */
public class MoveAbility(private val entity: Entity) : Ability {
    override val action: MoveAction

    init {
        this.action = MoveAction(this.entity)
    }


    // Public Methods
    override val name: String = NAME
    override val cost: Cost = COST
    override fun getCategory(): AbilityCategory {
        return BasicAbilityCategories.ALL_ABILITIES
    }

    override val icon: TextureRegion = ICON


    // Inner Classes
    public class MoveAction internal constructor(entity: Entity) : Action(entity) {
        override val targets: List<(Level, Point) -> Boolean> = listOf(
                { level, point ->
                    if (!level.contains(Rectangle(point.x, point.y, 1, 1))) {
                        false
                    } else {
                        val pointUnder = Mappers.position[this.entity].tilePosition
                        val pathFinder = IndexedAStarPathFinder<Level.LevelSquare>(level)
                        val path = DefaultGraphPath<Connection<Level.LevelSquare>>()
                        val found = pathFinder.searchConnectionPath(level.getSquare(pointUnder), level.getSquare(point),
                                Heuristics.CITY_BLOCK,
                                path
                        )
                        found && path.getCount() <= if (Families.attributed.matches(entity)) {
                            Mappers.attributes[entity].currentAttributes[AttributeSet.Attributes.MOVEMENT_SPEED]
                        } else {
                            1
                        }
                    }
                }
        )

        override fun apply(targets: List<Point>) {
            val walkTo = targets[0]

            val comp = Mappers.position[this.entity]
            val level = comp.level ?: return
            val currentPoint = comp.tilePosition
            val pathFinder = IndexedAStarPathFinder<Level.LevelSquare>(level)
            val path = DefaultGraphPath<Connection<Level.LevelSquare>>()
            val found = pathFinder.searchConnectionPath(level.getSquare(currentPoint), level.getSquare(walkTo), Heuristics.CITY_BLOCK, path)

            if (found && path.nodes.size > 0) {
                EuropaGame.game.messageSystem.publish(InputRequestMessage(InputRequestMessage.InputRequests.NONE))
                MoveManager(path, this.entity, { this.complete() }).start()
            } else {
                this.complete()
            }
        }

        protected class MoveManager(private val path: DefaultGraphPath<Connection<Level.LevelSquare>>, private val entity: Entity, private val onComplete: () -> Unit) : Listener<Message> {
            private var position: Int = 0

            public fun start() {
                EuropaGame.game.messageSystem.subscribe(this, javaClass<MovementCompleteMessage>())
                this.move(this.path.nodes[this.position])
            }

            public override fun handle(message: Message) {
                if (message is MovementCompleteMessage && message.entity === this.entity) {
                    this.position++
                    if (this.position < this.path.nodes.size) {
                        this.move(this.path.nodes[this.position])
                    } else {
                        this.complete()
                    }
                }
            }

            private fun move(connection: Connection<Level.LevelSquare>) {
                val first = connection.getFromNode()
                val second = connection.getToNode()
                val direction = MovementSystem.MovementDirections.getSingleStepDirectionFor(second.x - first.x, second.y - first.y) ?: return this.complete() // If we can't move, exit
                EuropaGame.game.messageSystem.publish(WalkRequestMessage(this.entity, direction))
            }

            private fun complete() {
                EuropaGame.game.messageSystem.unsubscribe(this, javaClass<MovementCompleteMessage>())
                this.onComplete.invoke()
            }
        }
    }

    companion object {

        private val NAME = "Move"
        private val ICON_NAME = "move"
        private val COST = Cost.NONE
        private val ICON by Delegates.lazy { EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion(ICON_NAME) }
    }
}
