/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emergence.entity.messaging;

import com.badlogic.ashley.core.Entity;
import com.emergence.entity.MovementSystem.MovementDirections;

/**
 *
 * @author Hortator
 */
public class MovementCompleteMessage extends StateChangeMessage {
    
    // Fields
    public final Entity entity;
    public final MovementDirections direction;
    
    
    // Initialization
    public MovementCompleteMessage(Entity entity, MovementDirections direction) {
        this.entity = entity;
        this.direction = direction;
    }
    
}