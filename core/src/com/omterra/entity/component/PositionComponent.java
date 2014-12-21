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
package com.omterra.entity.component;

import com.omterra.entity.messaging.Message;
import com.omterra.entity.messaging.PositionChangedMessage;
import com.omterra.world.Level;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public class PositionComponent extends EmergenceComponent {

    // Fields
    private Point tilePosition;
    private Point pixelPosition;
    private Level level;


    // Properties
    @Override
    public Set<Class<? extends Message>> getSubscribedMessageTypes() {
        Set<Class<? extends Message>> types = new HashSet<>();
        
        return types;
    }
    
    public final Level getLevel() {
        return this.level;
    }

    public final void setLevel(Level level) {
        this.level = level;
    }

    public final void setTilePosition(Point position) {
        this.tilePosition = position;
        
        if (this.level != null) {
            this.pixelPosition = new Point(this.tilePosition.x * this.level.getTileWidth(),
                    this.tilePosition.y * this.level.getPixelHeight());
        }
        else {
            this.pixelPosition = this.tilePosition;
        }
        
        this.getMessageSystem().publish(new PositionChangedMessage(this.tilePosition));
    }
    
    public final Point getTilePosition() {
        return this.tilePosition;
    }
    
    public final Point getPixelPosition() {
        return this.pixelPosition;
    }


    // Initialization
    public PositionComponent() {

    }
    
    public PositionComponent(Level level) {
        this();
        this.setLevel(level);
    }
    
    public PositionComponent(Level level, Point position) {
        this(level);
        this.setTilePosition(position);
    }
    
    
    // Public Methods
    @Override
    public void handleMessage(Message message) {
        
    }

}
