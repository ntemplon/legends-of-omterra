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
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Sprite
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.messaging.*
import com.jupiter.europa.util.Initializable
import com.jupiter.ganymede.event.Listener

/**

 * @author Nathan Templon
 */
public class RenderingMaintenanceSystem : IteratingSystem(Families.renderables, EuropaGame.RENDERING_SYSTEM_PRIORITY), Listener<Message>, EntityListener, SelfSubscribingListener, Initializable {


    // Public Methods
    override fun handle(message: Message) {
        if (message is PositionChangedMessage) {
            this.updateSpriteData(message.entity)
        } else if (message is OffsetUpdatedMessage) {
            this.updateSpriteData(message.entity)
        }
    }

    override fun entityAdded(entity: Entity) {
        this.updateSpriteData(entity)
    }

    override fun entityRemoved(entity: Entity) {
    }

    override fun subscribe(engine: Engine, system: MessageSystem) {
        engine.addEntityListener(Families.renderables, this)
        system.subscribe(this, javaClass<PositionChangedMessage>(), javaClass<OffsetUpdatedMessage>())
    }

    override fun initialize() {
        for (entity in this.getEntities()) {
            Mappers.render.get(entity).sprite = Sprite(Mappers.moveTexture.get(entity).standingTextureFor(Mappers.position.get(entity).facingDirection))
            this.updateSpriteData(entity)
        }
    }


    // Protected Methods
    override fun processEntity(entity: Entity, f: Float) {
    }


    // Private Methods
    private fun updateSpriteData(entity: Entity) {
        if (Families.positionables.matches(entity)) {
            val loc = Mappers.position.get(entity).pixelPosition
            val render = Mappers.render.get(entity)
            val sprite = render.sprite

            // Coordinates of the bottom right corner of the space, where the sprite's origin is
            sprite.setPosition((loc.x + render.offset.x).toFloat(), (loc.y + render.offset.y).toFloat())
        }
    }

}
