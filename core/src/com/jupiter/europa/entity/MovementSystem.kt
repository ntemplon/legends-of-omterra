/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
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
 */
package com.jupiter.europa.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.component.WalkComponent
import com.jupiter.europa.entity.component.WalkComponent.WalkStates
import com.jupiter.europa.entity.messaging.*
import com.jupiter.ganymede.event.Listener
import java.awt.Point
import java.awt.Rectangle

/**

 * @author Nathan Templon
 */
public class MovementSystem : IteratingSystem(Families.positionables, EuropaGame.MOVEMENT_SYSTEM_PRIORITY), Listener<Message>, SelfSubscribingListener {


    // Enumerations
    public enum class MovementDirections(public val deltaX: Int, public val deltaY: Int) {

        UP(0, 1),
        LEFT(-1, 0),
        DOWN(0, -1),
        RIGHT(1, 0);


        companion object {

            public fun getSingleStepDirectionFor(x: Int, y: Int): MovementDirections? {
                return if (x == 0) {
                    when {
                        y > 0 -> UP
                        y < 0 -> DOWN
                        else -> null
                    }
                } else {
                    when {
                        x > 0 -> RIGHT
                        x < 0 -> LEFT
                        else -> null
                    }
                }
            }
        }
    }


    // Public Methods
    override fun handle(message: Message) {
        if (message is WalkRequestMessage) {
            this.handleWalkRequest(message)
        }
    }

    override fun subscribe(engine: Engine, system: MessageSystem) {
        system.subscribe(this, javaClass<WalkRequestMessage>())
    }


    // Protected Methods
    override fun processEntity(entity: Entity, deltaT: Float) {
        if (Families.walkables.matches(entity)) {
            this.updateWalkSprite(entity, deltaT)
        }
    }


    // Private Methods
    private fun moveEntityTo(entity: Entity, location: Point) {
        val position = Mappers.position.get(entity)
        val oldLocation = position.tilePosition
        position.tilePosition = location
        EuropaGame.game.getMessageSystem().publish(PositionChangedMessage(entity, oldLocation, location))
    }

    private fun handleWalkRequest(message: WalkRequestMessage) {
        val entity = message.entity
        val direction = message.direction
        if (Families.walkables.matches(entity)) {
            val position = Mappers.position.get(entity)
            val location = position.tilePosition
            val requested = Point(location.x + direction.deltaX, location.y + direction.deltaY)

            val level = position.level
            val size = Mappers.size.get(entity).size
            val destination = Rectangle(requested.x, requested.y, size.width, size.height)

            if (level != null) {
                if (!level.collides(destination) && level.contains(destination)) {
                    val walk = Mappers.walk.get(entity)
                    if (walk.getState() == WalkStates.STANDING) {
                        walk.startWalking(direction, requested)
                        Mappers.moveTexture.get(entity).incrementWalkingTexture(direction)
                    }
                } else {
                    Mappers.render.get(entity).sprite.setRegion(Mappers.moveTexture.get(entity).standingTextureFor(direction))
                    Mappers.position.get(entity).facingDirection = direction
                }
            }
        }
    }

    private fun updateWalkPosition(entity: Entity, walk: WalkComponent) {
        val percentComplete = walk.getCompletionPercentage()
        val xOff = (EuropaGame.game.getCurrentLevel().getTileWidth().toFloat() * percentComplete * walk.walkDirection.deltaX.toFloat()).toInt()
        val yOff = (EuropaGame.game.getCurrentLevel().getTileHeight().toFloat() * percentComplete * walk.walkDirection.deltaY.toFloat()).toInt()
        Mappers.render.get(entity).offset = Point(xOff, yOff)
        EuropaGame.game.getMessageSystem().publish(OffsetUpdatedMessage(entity))
    }

    private fun updateWalkSprite(entity: Entity, deltaT: Float) {
        val walk = Mappers.walk.get(entity)
        walk.update(deltaT)

        val percentComplete = walk.getCompletionPercentage()
        val render = Mappers.render.get(entity)
        val sprite = render.sprite
        val textures = Mappers.moveTexture.get(entity)

        when (walk.getState()) {
            WalkComponent.WalkStates.FINISHED_WALKING -> {
                walk.setState(WalkStates.STANDING)
                render.offset = Point(0, 0)
                sprite.setRegion(textures.standingTextureFor(walk.walkDirection))
                this.moveEntityTo(entity, walk.walkTarget)
                Mappers.position.get(entity).facingDirection = walk.walkDirection
            }
            WalkComponent.WalkStates.STANDING -> {
            }
            WalkComponent.WalkStates.WALK_LEFT, WalkComponent.WalkStates.WALK_RIGHT, WalkComponent.WalkStates.WALK_UP, WalkComponent.WalkStates.WALK_DOWN -> {
                if (FIRST_WALK_SPRITE_BREAKPOINT < percentComplete && percentComplete < SECOND_WALK_SPRITE_BREAKPOINT) {
                    sprite.setRegion(textures.walkingTextureFont(walk.walkDirection))
                } else {
                    sprite.setRegion(textures.standingTextureFor(walk.walkDirection))
                }
                this.updateWalkPosition(entity, walk)
            }
        }
    }

    companion object {

        // Constants
        private val FIRST_WALK_SPRITE_BREAKPOINT = 0.3f
        private val SECOND_WALK_SPRITE_BREAKPOINT = 0.7f
    }

}// Initialization
