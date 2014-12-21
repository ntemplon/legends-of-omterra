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
import com.omterra.geometry.Size;
import com.omterra.quadtree.RectangularBoundedObject;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public class CollisionComponent extends EmergenceComponent implements RectangularBoundedObject {
    
    // Fields
    private Rectangle bounds;
    
    
    // Properties
    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }
    
    public void setLocation(Point location) {
        this.bounds = new Rectangle(location.x, location.y, this.bounds.width, this.bounds.height);
    }
    
    public void setSize(Size size) {
        this.bounds = new Rectangle(this.bounds.x, this.bounds.y, size.width, size.height);
    }
    
    @Override
    public Set<Class<? extends Message>> getSubscribedMessageTypes() {
        Set<Class<? extends Message>> types = new HashSet<>();
        
        types.add(PositionChangedMessage.class);
        
        return types;
    }
    
    
    // Initialization
    public CollisionComponent(Size size) {
        this.bounds = new Rectangle(0, 0, size.width, size.height);
    }
    
    public CollisionComponent(Point location, Size size) {
        this.bounds = new Rectangle(location.x, location.y, size.width, size.height);
    }
    
    
    // Public Methods
    @Override
    public void handleMessage(Message message) {
        if (message instanceof PositionChangedMessage) {
            this.handlePositionChanged((PositionChangedMessage)message);
        }
    }
    
    
    // Private Methods
    private void handlePositionChanged(PositionChangedMessage message) {
        this.setLocation(message.location);
    }
    
}
