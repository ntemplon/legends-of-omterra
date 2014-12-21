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

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.omterra.entity.messaging.MessageSystem;

/**
 *
 * @author Nathan Templon
 */
public class EmergenceEntity extends Entity {
    
    // Fields
    private final MessageSystem messageSystem;
    
    
    // Properties
    public MessageSystem getMessageSystem() {
        return this.messageSystem;
    }
    
    
    // Initialization
    public EmergenceEntity() {
        super();
        this.messageSystem = new MessageSystem();
    }
    
    
    // Public Methods
    @Override
    public Entity add(Component component) {
        this.onComponentAdded(component);
        return super.add(component);
    }
    
    @Override
    public Component remove(Class<? extends Component> componentClass) {
        Component removed = super.remove(componentClass);
        if (removed != null) {
            this.onComponentRemoved(removed);
        }
        return removed;
    }
    
    
    // Private Methods
    private void onComponentAdded(Component component) {
        
    }
    
    private void onComponentRemoved(Component component) {
        
    }
    
}