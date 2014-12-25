/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emergence.entity.messaging;

/**
 *
 * @author Hortator
 */
public class FauxMessageSystem implements MessageSystem {

    @Override
    public void update(boolean blocking) {
        
    }

    @Override
    public void subscribe(MessageListener listener, Class<? extends Message>... messageTypes) {
        
    }

    @Override
    public void subscribe(MessageListener listener) {
        
    }

    @Override
    public void unsubscribe(MessageListener listener, Class<? extends Message>... messageTypes) {
        
    }

    @Override
    public void unsubscribe(MessageListener listener) {
        
    }

    @Override
    public void publish(Message message) {
        
    }
    
}
