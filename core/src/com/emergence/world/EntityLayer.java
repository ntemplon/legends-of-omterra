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
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emergence.entity.Families;
import com.emergence.entity.Mappers;
import com.emergence.entity.PositionedEntity;
import com.emergence.geometry.Size;
import com.emergence.quadtree.Quadtree;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Nathan Templon
 */
public class EntityLayer implements Comparator<Entity> {

    // Fields
    private final Quadtree<PositionedEntity> positionalLookup;
    private final Set<Entity> entities;
    private final Set<EntityListener> listeners;
    private final Level level;


    // Properties
    public final Set<Entity> getEntities() {
        return this.entities;
    }


    // Initialization
    public EntityLayer(Size size, Level level) {
        this.positionalLookup = new Quadtree<>(new Rectangle(0, 0, size.width, size.height));
        this.entities = new TreeSet<>(this);
        this.listeners = new HashSet<>();
        this.level = level;
    }

    public EntityLayer(MapLayer layer, Size size, Level level) {
        this(size, level);
        this.addEntitiesFrom(layer);
    }


    // Public Methods
    public void addListener(EntityListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(EntityListener listener) {
        this.listeners.remove(listener);
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);

        if (Families.positionables.matches(entity)) {
            Mappers.position.get(entity).setLevel(this.level);
        }
        // Commented out until it can listen for changes to the collision (position and size)
//        if (Families.collidables.matches(entity)) {
//            Rectangle bounds = Mappers.collision.get(entity).getBounds();
//            this.positionalLookup.insert(new PositionedEntity(entity, bounds));
//        }
        // Dispatch the event
        this.listeners.stream().forEach((EntityListener listener) -> {
            listener.entityAdded(entity);
        });
    }

    public void removeEntity(Entity entity) {
        boolean removed = this.entities.remove(entity);

        if (removed) {
            // Dispatch the event
            this.listeners.stream().forEach((EntityListener listener) -> {
                listener.entityRemoved(entity);
            });
        }
    }


    // Comparator Implementation
    @Override
    public int compare(Entity first, Entity second) {
        int firstZ = 0;
        int secondZ = 0;

        if (Families.positionables.matches(first)) {
            firstZ = Mappers.position.get(first).getZOrder();
        }
        if (Families.positionables.matches(second)) {
            secondZ = Mappers.position.get(second).getZOrder();
        }

        return secondZ - firstZ;
    }


    // Private Methods
    private void addEntitiesFrom(MapLayer layer) {

    }

}
