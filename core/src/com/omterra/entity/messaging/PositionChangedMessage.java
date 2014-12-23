/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omterra.entity.messaging;

import com.badlogic.ashley.core.Entity;
import java.awt.Point;

/**
 *
 * @author Hortator
 */
public class PositionChangedMessage extends Message {
    
    // Fields
    public final Point location;
    public final Entity entity;
    
    
    // Initialization
    public PositionChangedMessage(Entity entity, Point location) {
        this.entity = entity;
        this.location = location;
    }
    
}
