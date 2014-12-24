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
package com.omterra.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.omterra.EmergenceGame;
import com.omterra.entity.component.PositionComponent;
import com.omterra.entity.messaging.Message;
import com.omterra.entity.messaging.MessageListener;
import com.omterra.entity.messaging.MessageSystem;
import com.omterra.entity.messaging.MovementRequestMessage;
import com.omterra.entity.messaging.PositionChangedMessage;
import com.omterra.entity.messaging.SelfSubscribingListener;
import java.awt.Point;

/**
 *
 * @author Nathan Templon
 */
public class MovementSystem extends IteratingSystem implements MessageListener, SelfSubscribingListener {

    // Enumerations
    public enum MovementDirections {
        UP(0, 1),
        LEFT(-1, 0),
        DOWN(0, -1),
        RIGHT(1, 0);
        
        public final int deltaX;
        public final int deltaY;
        MovementDirections(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }
    }
    

    // Initialization
    public MovementSystem() {
        super(Families.positionables, EmergenceGame.MOVEMENT_SYSTEM_PRIORITY);
    }
    
    
    // Public Methods
    @Override
    public void handleMessage(Message message) {
        if (message instanceof MovementRequestMessage) {
            this.handleMovementRequest((MovementRequestMessage)message);
        }
    }
    
    @Override
    public void subscribe(Engine engine, MessageSystem system) {
        system.subscribe(this, MovementRequestMessage.class);
    }


    // Protected Methods
    @Override
    protected void processEntity(Entity entity, float f) {
        
    }
    
    
    // Private Methods
    private void handleMovementRequest(MovementRequestMessage message) {
        Entity entity = message.entity;
        MovementDirections direction = message.direction;
        if (Families.positionables.matches(entity)) {
            PositionComponent position = Mappers.position.get(entity);
            Point location = position.getTilePosition();
            
            // Debug code - instantly moves to new location
            this.moveEntityTo(entity, new Point(location.x + direction.deltaX, location.y + direction.deltaY));
        }
    }
    
    private void moveEntityTo(Entity entity, Point location) {
        PositionComponent position = Mappers.position.get(entity);
        position.setTilePosition(location);
        EmergenceGame.game.getMessageSystem().publish(new PositionChangedMessage(entity, location));
    }

}
