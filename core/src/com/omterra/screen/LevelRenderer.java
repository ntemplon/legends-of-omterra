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
package com.omterra.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.omterra.entity.Families;
import com.omterra.entity.Mappers;
import com.omterra.world.EntityLayer;
import com.omterra.world.Level;

/**
 *
 * @author Nathan Templon
 */
public class LevelRenderer extends OrthogonalTiledMapRenderer {

    // Fields
    private final Level level;
    private final EntityLayerRenderer entityLayerRenderer;


    // Initialization
    public LevelRenderer(Level level) {
        super(level.getMap());
        this.level = level;
        this.entityLayerRenderer = new EntityLayerRenderer(this.level.getEntityLayer());
    }


    // Custom OrthogonalTiledMapRenderer Implementation
    @Override
    public void render() {
        beginRender(); // Built - in method

        // Render each layer, in order
        for (MapLayer layer : this.map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    this.renderTileLayer((TiledMapTileLayer) layer);
                }
                else {
                    for (MapObject object : layer.getObjects()) {
                        this.renderObject(object);
                    }
                }
            }
            
            // Render Entities
            if (layer.getName().equals(Level.ENTITY_LAYER_NAME)) {
                this.entityLayerRenderer.render(this.getBatch());
            }
        }

        endRender(); // Built-in method
    }


    // Inner Classes
    private static class EntityLayerRenderer {

        // Fields
        private final EntityLayer layer;


        // Initialization
        public EntityLayerRenderer(EntityLayer layer) {
            this.layer = layer;
        }


        // Public Methods
        public void render(Batch batch) {
            // Draw all renderable components, in z order (because the set is sorted)
            this.layer.getEntities().stream().filter((Entity entity) -> {
                return Families.renderables.matches(entity);
            }).forEach((Entity entity) -> {
                Mappers.render.get(entity).getSprite().draw(batch);
            });
        }

    }

}
