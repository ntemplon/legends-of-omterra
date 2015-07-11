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

package com.jupiter.europa.world

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.maps.MapLayer
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.PositionedEntity
import com.jupiter.europa.entity.messaging.Message
import com.jupiter.europa.entity.messaging.PositionChangedMessage
import com.jupiter.europa.geometry.Size
import com.jupiter.europa.util.Quadtree
import com.jupiter.ganymede.event.Listener
import java.awt.Point
import java.awt.Rectangle
import java.util.Comparator
import java.util.HashSet
import java.util.TreeSet

/**

 * @author Nathan Templon
 */
public class EntityLayer// Initialization
(size: Size, private val level: Level) : Comparator<Entity> {

    // Fields
    private val positionalLookup: Quadtree<PositionedEntity>
    private val entities: MutableSet<Entity>
    private val listeners: MutableSet<EntityListener>


    // Properties
    public fun getEntities(): Set<Entity> {
        return this.entities
    }


    init {
        this.positionalLookup = Quadtree<PositionedEntity>(Rectangle(0, 0, size.width, size.height))
        this.entities = TreeSet(this)
        this.listeners = HashSet<EntityListener>()

        EuropaGame.game.messageSystem.subscribe(Listener { args -> this.onEntityPositionChanged(args) }, javaClass<PositionChangedMessage>())
    }

    public constructor(layer: MapLayer, size: Size, level: Level) : this(size, level) {
        this.addEntitiesFrom(layer)
    }


    // Public Methods
    public fun addListener(listener: EntityListener) {
        this.listeners.add(listener)
    }

    public fun removeListener(listener: EntityListener) {
        this.listeners.remove(listener)
    }

    public fun addEntity(entity: Entity) {
        this.entities.add(entity)

        if (Families.positionables.matches(entity)) {
            val component = Mappers.position.get(entity)
            component.level = this.level
        }

        if (Families.collidables.matches(entity)) {
            val bounds = Mappers.collision.get(entity).getBounds()
            this.positionalLookup.insert(PositionedEntity(entity, bounds))
        }

        // Dispatch the event
        for (listener in listeners) {
            listener.entityAdded(entity)
        }
    }

    public fun removeEntity(entity: Entity) {
        val removed = this.entities.remove(entity)

        if (Families.collidables.matches(entity)) {
            val bounds = Mappers.collision.get(entity).getBounds()
            this.positionalLookup.remove(PositionedEntity(entity, bounds))
        }

        if (removed) {
            // Dispatch the event
            for (listener in listeners) {
                listener.entityAdded(entity)
            }
        }
    }

    public fun entityAt(location: Point): Entity? {
        val entities = this.positionalLookup.retrieve(Rectangle(location.x, location.y, 1, 1))
        val entList = entities.map { pos -> pos.entity }.toList()
        return if (entList.size() > 0) {
            entList[0]
        } else {
            null
        }
    }


    // Comparator Implementation
    override fun compare(first: Entity, second: Entity): Int {
        var firstZ = 0
        var secondZ = 0

        if (Families.positionables.matches(first)) {
            firstZ = Mappers.position.get(first).zOrder
        }
        if (Families.positionables.matches(second)) {
            secondZ = Mappers.position.get(second).zOrder
        }

        return secondZ - firstZ
    }


    // Private Methods
    private fun addEntitiesFrom(layer: MapLayer) {
    }

    private fun onEntityPositionChanged(message: Message) {
        if (message is PositionChangedMessage) {
            val position = Mappers.position.get(message.entity)
            val size = Mappers.size.get(message.entity)
            if (position.level == this.level) {
                val oldPos = PositionedEntity(message.entity, Rectangle(message.oldLocation.x, message.oldLocation.y, size.size.width, size.size.height))
                val newPos = PositionedEntity(message.entity, Rectangle(message.newLocation.x, message.newLocation.y, size.size.width, size.size.height))

                this.positionalLookup.remove(oldPos)
                this.positionalLookup.insert(newPos)
            }
        }
    }

}
