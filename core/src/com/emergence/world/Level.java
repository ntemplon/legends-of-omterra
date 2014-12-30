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
package com.emergence.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.emergence.audio.AudioService;
import com.emergence.geometry.Size;
import com.emergence.util.Quadtree;
import java.awt.Rectangle;

/**
 * A class representing an individual level (map) in Legends of Omterra
 *
 * @author Nathan Templon
 */
public class Level implements Disposable {

    // Constants
    public static final String LEVEL_FOLDER = "levels";
    public static final String LEVEL_EXTENSION = "tmx";
    public static final String ENTITY_LAYER_NAME = "Entities";
    public static final String COLLISION_LAYER_NAME = "Collision";
    public static final String ZONE_LAYER_NAME = "Zones";

    public static final String TILE_WIDTH_KEY = "tilewidth";
    public static final String TILE_HEIGHT_KEY = "tileheight";
    public static final String WIDTH_KEY = "width";
    public static final String HEIGHT_KEY = "height";
    public static final String MAP_WIDTH_KEY = WIDTH_KEY;
    public static final String MAP_HEIGHT_KEY = HEIGHT_KEY;
    public static final String X_KEY = "x";
    public static final String Y_KEY = "y";
    
    public static final String MUSIC_PROPERTY = "music";
    
    public static final int DEFAULT_TILE_WIDTH = 16;
    public static final int DEFAULT_TILE_HEIGHT = 16;


    // Fields
    private World world;
    private TiledMap map;
    private String name;
    private EntityLayer entityLayer;
    private Quadtree<Zone> collision;
    private String musicType = AudioService.DEFAULT_MUSIC;

    private int tileWidth, tileHeight;
    private int mapWidth, mapHeight;
    private int pixelWidth, pixelHeight;
    
    private Entity controlledEntity;


    // Properties
    public World getWorld() {
        return this.world;
    }
    
    public TiledMap getMap() {
        return this.map;
    }

    private void setMap(TiledMap map) {
        this.map = map;

        // Get metrics
        this.tileWidth = map.getProperties().get(TILE_WIDTH_KEY, Integer.class);
        this.tileHeight = map.getProperties().get(TILE_HEIGHT_KEY, Integer.class);
        this.mapWidth = map.getProperties().get(MAP_WIDTH_KEY, Integer.class);
        this.mapHeight = map.getProperties().get(MAP_HEIGHT_KEY, Integer.class);
        this.pixelWidth = this.getTileWidth() * this.getMapWidth();
        this.pixelHeight = this.getTileHeight() * this.getMapHeight();

        // Set all layers visible, except for the informational layers
        for (MapLayer layer : this.map.getLayers()) {
            String layerName = layer.getName();
            if (layerName.equalsIgnoreCase(ENTITY_LAYER_NAME)) {
                // Get the entity layer from the map
                this.entityLayer = new EntityLayer(layer, new Size(this.getMapWidth(), this.getMapHeight()), this);
            }
            else if (layerName.equalsIgnoreCase(COLLISION_LAYER_NAME)) {
                this.collision = this.getCollisionFrom(layer);
            }
            else if (layerName.equalsIgnoreCase(ZONE_LAYER_NAME)) {

            }
            else {
                layer.setVisible(true);
            }
        }
        
        // Get the type of music from the map
        if (map.getProperties().containsKey(MUSIC_PROPERTY)) {
            this.musicType = map.getProperties().get(MUSIC_PROPERTY, String.class);
        }
    }

    public String getName() {
        return this.name;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    public int getPixelWidth() {
        return this.pixelWidth;
    }

    public int getPixelHeight() {
        return this.pixelHeight;
    }
    
    public EntityLayer getEntityLayer() {
        return this.entityLayer;
    }
    
    public Entity getControlledEntity() {
        return this.controlledEntity;
    }
    
    public void setControlledEntity(Entity entity) {
        this.controlledEntity = entity;
    }
    
    public String getMusicType() {
        return this.musicType;
    }


    // Initialization
    public Level() {
        
    }

    public Level(String name, TiledMap map, World world) {
        this();

        this.name = name;
        this.setMap(map);
        this.world = world;
    }
    
    
    // Public Methods
    public boolean collides(Rectangle rectangle) {
        return !this.collision.retrieve(rectangle).isEmpty();
    }
    
    public boolean contains(Rectangle rectangle) {
        return this.collision.getBounds().contains(rectangle);
    }


    // Disposable Imiplementation
    @Override
    public void dispose() {
        if (this.map != null) {
            this.map.dispose();
        }
    }
    
    
    // Private Methods
    private Quadtree<Zone> getCollisionFrom(MapLayer layer) {
        // The collision quadtree, in tile coordinates
        Quadtree<Zone> tree = new Quadtree<>(new Rectangle(0, 0, this.getMapWidth(), this.getMapHeight()));
        
        for(MapObject obj : layer.getObjects()) {
            if (obj instanceof RectangleMapObject) {
                com.badlogic.gdx.math.Rectangle pixelCoords = ((RectangleMapObject)obj).getRectangle();
                
                // Convert from pixel coordinates to tile coordinates.
                // Tmx pixel coordinates have the origin at the top left, libGDX and I use the bottom left for coordinates.
                int collisionX = (int)(pixelCoords.x / this.getTileHeight());
                int collisionY = (int)(pixelCoords.y / this.getTileHeight());
                int collisionWidth = (int)(pixelCoords.width / this.getTileWidth());
                int collisionHeight = (int)(pixelCoords.height / this.getTileHeight());
                Rectangle tileCoords = new Rectangle(collisionX, collisionY, collisionWidth, collisionHeight);
                
                tree.insert(new Zone(tileCoords));
            }
        }

        return tree;
    }

}
