/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omterra.entity.messaging;

import java.awt.Point;

/**
 *
 * @author Hortator
 */
public class PositionChangedMessage extends Message {
    
    // Fields
    public final Point location;
    
    
    // Initialization
    public PositionChangedMessage(Point location) {
        this.location = location;
    }
    
}
