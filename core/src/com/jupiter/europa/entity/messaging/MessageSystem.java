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
public interface MessageSystem {
    
    void update(boolean blocking);
    void subscribe(Listener<Message> listener, Class<? extends Message>... messageTypes);
    void subscribe(Listener<Message> listener);
    void unsubscribe(Listener<Message> listener, Class<? extends Message>... messageTypes);
    void unsubscribe(Listener<Message> listener);
    void publish(Message message);
    
}
