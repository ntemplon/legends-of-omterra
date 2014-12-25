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
public interface MessageSystem {
    
    void update(boolean blocking);
    void subscribe(MessageListener listener, Class<? extends Message>... messageTypes);
    void subscribe(MessageListener listener);
    void unsubscribe(MessageListener listener, Class<? extends Message>... messageTypes);
    void unsubscribe(MessageListener listener);
    void publish(Message message);
    
}
