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
import com.badlogic.ashley.systems.IteratingSystem;
import com.emergence.entity.messaging.Message;
import com.emergence.entity.messaging.MessageListener;
import com.emergence.entity.messaging.MessageSystem;
import com.emergence.entity.messaging.PositionChangedMessage;
import com.emergence.entity.messaging.SelfSubscribingListener;

/**
 *
 * @author Nathan Templon
 */
public class CollisionSystem extends IteratingSystem implements MessageListener, SelfSubscribingListener {

    // Initialization
    public CollisionSystem() {
        super(Families.collidables);
    }
    
    
    // Public Methods
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void subscribe(Engine engine, MessageSystem system) {
        system.subscribe(this, PositionChangedMessage.class);
    }

    @Override
    public void handleMessage(Message message) {
        if (message instanceof PositionChangedMessage) {
            Entity entity = ((PositionChangedMessage) message).entity;

            if (Families.collidables.matches(entity)) {
                Mappers.collision.get(entity).setLocation(Mappers.position.get(entity).getTilePosition());
            }
        }
    }

}
