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
import com.omterra.entity.component.EmergenceComponent;
import com.omterra.entity.messaging.FauxMessageSystem;
import com.omterra.entity.messaging.Message;
import com.omterra.entity.messaging.MessageSystem;
import com.omterra.entity.messaging.SimpleMessageSystem;
import java.util.Set;

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
        this.messageSystem = new SimpleMessageSystem();
    }
    
    
    // Public Methods
    @Override
    public Entity add(Component component) {
        if (component instanceof EmergenceComponent) {
            this.onComponentAdded((EmergenceComponent)component);
        }
        return super.add(component);
    }
    
    @Override
    public Component remove(Class<? extends Component> componentClass) {
        Component removed = super.remove(componentClass);
        if (removed != null && removed instanceof EmergenceComponent) {
            this.onComponentRemoved((EmergenceComponent)removed);
        }
        return removed;
    }
    
    
    // Private Methods
    private void onComponentAdded(EmergenceComponent component) {
        component.setMessageSystem(this.messageSystem);
        Set<Class<? extends Message>> types = component.getSubscribedMessageTypes();
        this.messageSystem.subscribe(component, types.toArray(new Class[types.size()]));
    }
    
    private void onComponentRemoved(EmergenceComponent component) {
        component.setMessageSystem(new FauxMessageSystem());
        this.messageSystem.unsubscribe(component);
    }
    
}
