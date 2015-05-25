/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.entity.messaging;

import com.badlogic.ashley.core.Entity;

import java.awt.*;

/**
 *
 * @author Hortator
 */
public class PositionChangedMessage extends StateChangeMessage {
    
    // Fields
    public final Point oldLocation;
    public final Point newLocation;
    public final Entity entity;
    
    
    // Initialization
    public PositionChangedMessage(Entity entity, Point oldLocation, Point newLocation) {
        this.entity = entity;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }
    
}
