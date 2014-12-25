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
package com.emergence.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.emergence.EmergenceGame;
import com.emergence.entity.component.RenderComponent;
import com.emergence.entity.messaging.Message;
import com.emergence.entity.messaging.MessageListener;
import com.emergence.entity.messaging.MessageSystem;
import com.emergence.entity.messaging.OffsetUpdatedMessage;
import com.emergence.entity.messaging.PositionChangedMessage;
import com.emergence.entity.messaging.SelfSubscribingListener;
import java.awt.Point;

/**
 *
 * @author Nathan Templon
 */
public class RenderingMaintenenceSystem extends IteratingSystem implements MessageListener, EntityListener, SelfSubscribingListener {

    // Initialization
    public RenderingMaintenenceSystem() {
        super(Families.renderables, EmergenceGame.RENDERING_SYSTEM_PRIORITY);
    }


    // Public Methods
    @Override
    public void handleMessage(Message message) {
        if (message instanceof PositionChangedMessage) {
            this.updateSpriteData(((PositionChangedMessage) message).entity);
        }
        else if (message instanceof OffsetUpdatedMessage) {
            this.updateSpriteData(((OffsetUpdatedMessage) message).entity);
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        if (Families.walkables.matches(entity)) {
            RenderComponent render = Mappers.render.get(entity);
            if (render.getSprite() == null) {
                render.setSprite(new Sprite(Mappers.moveTexture.get(entity).getFrontStandTexture()));
            }
        }
        
        this.updateSpriteData(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
    
    @Override
    public void subscribe(Engine engine, MessageSystem system) {
        engine.addEntityListener(Families.renderables, this);
        system.subscribe(this, PositionChangedMessage.class, OffsetUpdatedMessage.class);
    }


    // Protected Methods
    @Override
    protected void processEntity(Entity entity, float f) {

    }


    // Private Methods
    private void updateSpriteData(Entity entity) {
        if (Families.positionables.matches(entity)) {
            Point loc = Mappers.position.get(entity).getPixelPosition();
            RenderComponent render = Mappers.render.get(entity);
            Sprite sprite = render.getSprite();

            // Coordinates of the bottom right corner of the space, where the sprite's origin is
            sprite.setPosition(loc.x + render.getOffset().x, loc.y + sprite.getHeight() + render.getOffset().y);
        }
    }

}
