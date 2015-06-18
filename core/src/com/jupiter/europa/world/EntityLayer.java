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
package com.jupiter.europa.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.maps.MapLayer;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.PositionedEntity;
import com.jupiter.europa.entity.component.PositionComponent;
import com.jupiter.europa.entity.component.SizeComponent;
import com.jupiter.europa.entity.messaging.Message;
import com.jupiter.europa.entity.messaging.PositionChangedMessage;
import com.jupiter.europa.geometry.Size;
import com.jupiter.europa.util.Quadtree;

import java.awt.*;
import java.util.*;

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

        EuropaGame.game.getMessageSystem().subscribe(this::onEntityPositionChanged, PositionChangedMessage.class);
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
            PositionComponent component = Mappers.position.get(entity);
            component.setLevel(this.level);
        }

        if (Families.collidables.matches(entity)) {
            Rectangle bounds = Mappers.collision.get(entity).getBounds();
            this.positionalLookup.insert(new PositionedEntity(entity, bounds));
        }
        // Dispatch the event
        this.listeners.stream().forEach((EntityListener listener) -> {
            listener.entityAdded(entity);
        });
    }

    public void removeEntity(Entity entity) {
        boolean removed = this.entities.remove(entity);

        if (Families.collidables.matches(entity)) {
            Rectangle bounds = Mappers.collision.get(entity).getBounds();
            this.positionalLookup.remove(new PositionedEntity(entity, bounds));
        }

        if (removed) {
            // Dispatch the event
            this.listeners.stream().forEach((EntityListener listener) -> {
                listener.entityRemoved(entity);
            });
        }
    }

    public Entity entityAt(Point location) {
        Collection<PositionedEntity> entities = this.positionalLookup.retrieve(new Rectangle(location.x, location.y, 1, 1));
        Optional<Entity> entity = entities.stream()
                .map(pos -> pos.getEntity())
                .findAny();

        return entity.isPresent() ? entity.get() : null;
    }


    // Comparator Implementation
    @Override
    public int compare(Entity first, Entity second) {
        int firstZ = 0;
        int secondZ = 0;

        if (Families.positionables.matches(first)) {
            firstZ = Mappers.position.get(first).getzOrder();
        }
        if (Families.positionables.matches(second)) {
            secondZ = Mappers.position.get(second).getzOrder();
        }

        return secondZ - firstZ;
    }


    // Private Methods
    private void addEntitiesFrom(MapLayer layer) {

    }

    private void onEntityPositionChanged(Message message) {
        if (message instanceof PositionChangedMessage) {
            PositionChangedMessage positionChangedMessage = (PositionChangedMessage) message;
            PositionComponent position = Mappers.position.get(positionChangedMessage.entity);
            SizeComponent size = Mappers.size.get(positionChangedMessage.entity);
            if (position.getLevel().equals(this.level)) {
                PositionedEntity oldPos = new PositionedEntity(positionChangedMessage.entity, new Rectangle(
                        positionChangedMessage.oldLocation.x, positionChangedMessage.oldLocation.y, size.getSize().width, size.getSize().height
                ));
                PositionedEntity newPos = new PositionedEntity(positionChangedMessage.entity, new Rectangle(
                        positionChangedMessage.newLocation.x, positionChangedMessage.newLocation.y, size.getSize().width, size.getSize().height
                ));

                this.positionalLookup.remove(oldPos);
                this.positionalLookup.insert(newPos);
            }
        }
    }

}
