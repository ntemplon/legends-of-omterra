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
package com.omterra.entity.component;

import com.badlogic.ashley.core.Component;
import com.omterra.world.Level;
import java.awt.Point;

/**
 *
 * @author Nathan Templon
 */
public class PositionComponent extends Component {

    // Fields
    private Point tilePosition;
    private Point pixelPosition;
    private Level level;
    private int zOrder;


    // Properties
    public final Level getLevel() {
        return this.level;
    }

    public final void setLevel(Level level) {
        this.level = level;
    }

    public final void setTilePosition(Point position) {
        this.tilePosition = position;
        
        if (this.level != null) {
            this.pixelPosition = new Point(this.tilePosition.x * this.level.getTileWidth(),
                    this.tilePosition.y * this.level.getTileHeight());
        }
        else {
            this.pixelPosition = this.tilePosition;
        }
    }
    
    public final void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
    
    public final Point getTilePosition() {
        return this.tilePosition;
    }
    
    public final Point getPixelPosition() {
        return this.pixelPosition;
    }
    
    public final int getZOrder() {
        return this.zOrder;
    }


    // Initialization
    public PositionComponent() {

    }
    
    public PositionComponent(Level level) {
        this();
        this.setLevel(level);
    }
    
    public PositionComponent(Level level, Point position) {
        this(level);
        this.setTilePosition(position);
    }
    
    public PositionComponent(Level level, Point position, int zOrder) {
        this(level, position);
        this.setZOrder(zOrder);
    }

}
