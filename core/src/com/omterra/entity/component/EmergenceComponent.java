/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omterra.entity.component;

import com.badlogic.ashley.core.Component;
import com.omterra.entity.messaging.FauxMessageSystem;
import com.omterra.entity.messaging.Message;
import com.omterra.entity.messaging.MessageListener;
import com.omterra.entity.messaging.MessageSystem;
import java.util.Set;

/**
 *
 * @author Hortator
 */
public abstract class EmergenceComponent extends Component implements MessageListener {
    
    // Fields
    private MessageSystem messageSystem;
    
    
    // Properties
    public abstract Set<Class<? extends Message>> getSubscribedMessageTypes();
    
    public MessageSystem getMessageSystem() {
        return this.messageSystem;
    }
    
    public void setMessageSystem(MessageSystem system) {
        this.messageSystem = system;
    }
    
    
    // Initialization
    public EmergenceComponent() {
        super();
        this.messageSystem = new FauxMessageSystem();
    }
    
    
    // MessageListener Implementation
    @Override
    public abstract void handleMessage(Message message);
    
}
