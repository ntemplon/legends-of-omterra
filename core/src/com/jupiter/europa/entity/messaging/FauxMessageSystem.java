/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.entity.messaging;

import com.jupiter.ganymede.event.Listener;

/**
 *
 * @author Hortator
 */
public class FauxMessageSystem implements MessageSystem {

    @Override
    public void update(boolean blocking) {
        
    }

    @Override
    public void subscribe(Listener<Message> listener, Class<? extends Message>... messageTypes) {
        
    }

    @Override
    public void subscribe(Listener<Message> listener) {
        
    }

    @Override
    public void unsubscribe(Listener<Message> listener, Class<? extends Message>... messageTypes) {
        
    }

    @Override
    public void unsubscribe(Listener<Message> listener) {
        
    }

    @Override
    public void publish(Message message) {
        
    }
    
}
