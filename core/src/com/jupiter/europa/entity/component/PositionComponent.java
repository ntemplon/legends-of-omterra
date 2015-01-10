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
package com.jupiter.europa.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.MovementSystem.MovementDirections;
import com.jupiter.europa.world.Level;
import static com.jupiter.europa.world.Level.DEFAULT_TILE_HEIGHT;
import static com.jupiter.europa.world.Level.DEFAULT_TILE_WIDTH;
import com.jupiter.europa.world.World;
import java.awt.Point;

/**
 *
 * @author Nathan Templon
 */
public class PositionComponent extends Component implements Serializable {

    // Constants
    public static final String WORLD_KEY = "world";
    public static final String LEVEL_KEY = "level";
    public static final String TILE_POSITION_X_KEY = "tile-position-x";
    public static final String TILE_POSITION_Y_KEY = "tile-position-y";
    public static final String Z_ORDER_KEY = "z-order";
    public static final String FACING_KEY = "facing";
    
    
    // Fields
    private Point tilePosition;
    private Point pixelPosition;
    private World world;
    private Level level;
    private int zOrder;
    private MovementDirections facing = MovementDirections.DOWN;


    // Properties
    public final World getWorld() {
        return this.world;
    }
    
    public final Level getLevel() {
        return this.level;
    }

    public final void setLevel(Level level) {
        this.level = level;
        if (this.level != null) {
            this.world = this.level.getWorld();
        }
    }

    public final void setTilePosition(Point position) {
        this.tilePosition = position;
        
        if (this.level != null) {
            this.pixelPosition = new Point(this.tilePosition.x * this.level.getTileWidth(),
                    this.tilePosition.y * this.level.getTileHeight());
        }
        else {
            this.pixelPosition = new Point(this.tilePosition.x * DEFAULT_TILE_WIDTH,
                    this.tilePosition.y * DEFAULT_TILE_HEIGHT);
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
    
    public final MovementDirections getFacingDirection() {
        return this.facing;
    }
    
    public final void setFacingDirection(MovementDirections direction) {
        this.facing = direction;
    }


    // Initialization
    public PositionComponent() {
        this(null, new Point(0, 0), 0);
    }
    
    public PositionComponent(Level level) {
        this(level, new Point(0, 0), 0);
    }
    
    public PositionComponent(Level level, Point position) {
        this(level, position, 0);
    }
    
    public PositionComponent(Level level, Point position, int zOrder) {
        this.setLevel(level);
        this.setTilePosition(position);
        this.setZOrder(zOrder);
    }
    
    
    // JsonSerializable implementation
    @Override
    public void write(Json json) {
        json.writeValue(WORLD_KEY, this.getWorld().getName());
        json.writeValue(LEVEL_KEY, this.getLevel().getName());
        json.writeValue(TILE_POSITION_X_KEY, this.getTilePosition().x);
        json.writeValue(TILE_POSITION_Y_KEY, this.getTilePosition().y);
        json.writeValue(Z_ORDER_KEY, this.getZOrder());
        json.writeValue(FACING_KEY, this.getFacingDirection().toString());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        String worldName = jsonData.getString(WORLD_KEY);
        String levelName = jsonData.getString(LEVEL_KEY);
        int tileX = jsonData.getInt(TILE_POSITION_X_KEY);
        int tileY = jsonData.getInt(TILE_POSITION_Y_KEY);
        int zOrderValue = jsonData.getInt(Z_ORDER_KEY);
        
        this.setLevel(EuropaGame.game.getWorld(worldName).getLevel(levelName));
        this.setTilePosition(new Point(tileX, tileY));
        this.zOrder = zOrderValue;
        
        this.facing = MovementDirections.valueOf(jsonData.getString(FACING_KEY));
    }

}
