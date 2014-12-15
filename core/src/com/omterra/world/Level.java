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
package com.omterra.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

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

    public static final String TILE_WIDTH_KEY = "tilewidth";
    public static final String TILE_HEIGHT_KEY = "tileheight";
    public static final String MAP_WIDTH_KEY = "width";
    public static final String MAP_HEIGHT_KEY = "height";
    

    // Fields
    private TiledMap map;
    private String name;

    private int tileWidth, tileHeight;
    private int mapWidth, mapHeight;
    private int pixelWidth, pixelHeight;


    // Properties
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
            layer.setVisible(!(layer.getName().equalsIgnoreCase(ENTITY_LAYER_NAME) || layer.getName().equalsIgnoreCase(
                    COLLISION_LAYER_NAME)));
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


    // Initialization
    public Level() {

    }
    
    public Level(String name, TiledMap map) {
        this();
        
        this.name = name;
        this.setMap(map);
    }


    // Disposable Imiplementation
    @Override
    public void dispose() {
        if (this.map != null) {
            this.map.dispose();
        }
    }

}
