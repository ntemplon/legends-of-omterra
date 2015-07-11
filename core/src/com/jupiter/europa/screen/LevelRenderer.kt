/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
 *
 */

package com.jupiter.europa.screen

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.world.EntityLayer
import com.jupiter.europa.world.Level

/**

 * @author Nathan Templon
 */
public class LevelRenderer public constructor(private val level: Level) : OrthogonalTiledMapRenderer(level.map) {

    // Properties
    private val entityLayerRenderer: EntityLayerRenderer = EntityLayerRenderer(this.level.entityLayer!!)


    // Custom OrthogonalTiledMapRenderer Implementation
    public override fun render() {
        beginRender() // Built - in method

        // Render each layer, in order
        for (layer in this.getMap().getLayers()) {
            if (layer.isVisible()) {
                if (layer is TiledMapTileLayer) {
                    this.renderTileLayer(layer)
                } else {
                    for (obj in layer.getObjects()) {
                        this.renderObject(obj)
                    }
                }
            }

            // Render Entities
            if (layer.getName() == Level.ENTITY_LAYER_NAME) {
                this.entityLayerRenderer.render(this.getBatch())
            }
        }

        endRender() // Built-in method
    }


    // Inner Classes
    public class EntityLayerRenderer(private val layer: EntityLayer) {

        // Public Methods
        public fun render(batch: Batch) {
            // Draw all renderable components, in z order (because the set is sorted)
            this.layer.getEntities()
                    .filter { entity -> Families.renderables.matches(entity) }
                    .forEach { entity ->
                        val sprite = Mappers.render[entity].sprite
                        sprite.setColor(batch.getColor() ?: Color.WHITE)
                        sprite.draw(batch)
                    }
        }

    }

}
