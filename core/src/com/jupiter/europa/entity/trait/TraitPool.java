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
package com.jupiter.europa.entity.trait;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public abstract class TraitPool<T extends Trait> implements Serializable {

    // Constants
    private static final String CAPACITY_KEY = "capacity";
    private static final String SELECTED_KEY = "selected";
    private static final String ITEM_CLASS_KEY = "item-class";
    private static final String ITEM_DATA_KEY = "item-data";


    // Fields
    private Entity owner;

    private final Set<T> source;
    private final Set<T> qualified;
    private final List<T> selected;
    private int capacity;

    private final Event<TraitPoolEvent<T>> selection = new Event<>();

    private boolean autoQualify = false;


    // Properties
    public int getCapacity() {
        return this.capacity;
    }

    public boolean isFull() {
        return this.getNumberOfSelections() >= this.getCapacity();
    }

    public int getNumberOfSelections() {
        return this.selected.size();
    }

    public Entity getOwner() {
        return this.owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public List<T> getSelections() {
        return this.selected;
    }

    public Set<T> getSources() {
        return this.source;
    }

    public boolean getAutoQualify() {
        return this.autoQualify;
    }

    public void setAutoQualify(boolean autoQualify) {
        this.autoQualify = autoQualify;
    }


    // Initialization
    public TraitPool() {
        this.source = new TreeSet<>();
        this.qualified = new TreeSet<>();
        this.selected = new ArrayList<>();

        this.capacity = 0;
    }

    public TraitPool(Entity owner) {
        this();
        this.owner = owner;
    }


    // Public Methods
    public void addSource(T source) {
        this.source.add(source);
        if (this.autoQualify && source.getQualifications().qualifies(this.owner)) {
            this.qualified.add(source);
        }
    }

    public void addSource(Collection<T> source) {
        source.stream().forEach((T instance) -> this.addSource(instance));
    }

    public void increaseCapacity(int additional) {
        if (additional <= 0) {
            return;
        }

        this.capacity += additional;
    }

    public void reassesQualifications() {
        this.qualified.clear();
        this.source.stream().forEach((T instance) -> {
            if (instance.getQualifications().qualifies(this.owner)) {
                this.qualified.add(instance);
            }
        });
    }

    public boolean select(T trait) {
        if (this.source.contains(trait)) {
            this.selected.add(trait);

            // Dispatch Message
            this.selection.dispatch(new TraitPoolEvent(this, trait));

            if (this.autoQualify) {
                this.reassesQualifications();
            }
            
            return true;
        }
        return false;
    }

    public boolean addSelectionListener(Listener<TraitPoolEvent<T>> listener) {
        return this.selection.addListener(listener);
    }

    public boolean removeSelectionListener(Listener<TraitPoolEvent<T>> listener) {
        return this.selection.removeListener(listener);
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(CAPACITY_KEY, this.capacity);

        json.writeArrayStart(SELECTED_KEY);
        this.selected.stream().forEach((T item) -> {
            json.writeObjectStart();
            json.writeValue(ITEM_CLASS_KEY, item.getClass().getName());
            json.writeValue(ITEM_DATA_KEY, item, item.getClass());
            json.writeObjectEnd();
        });
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(CAPACITY_KEY)) {
            this.capacity = jsonData.getInt(CAPACITY_KEY);
        }

        if (jsonData.has(SELECTED_KEY)) {
            JsonValue selectedData = jsonData.get(SELECTED_KEY);
            if (selectedData.isArray()) {
                selectedData.iterator().forEach((JsonValue value) -> {
                    if (value.has(ITEM_CLASS_KEY) && value.has(ITEM_DATA_KEY)) {
                        String typeName = value.getString(ITEM_CLASS_KEY);
                        try {
                            Class<?> type = Class.forName(typeName);
                            if (Trait.class.isAssignableFrom(type)) {
                                this.selected.add((T) json.fromJson(type, value.get(ITEM_DATA_KEY).toString()));
                            }
                        }
                        catch (ClassNotFoundException ex) {

                        }

                    }
                });
            }
        }
    }
    
    
    // Nested Classes
    public static class TraitPoolEvent<T extends Trait> {
        
        // Fields
        public final TraitPool<T> pool;
        public final T trait;
        
        
        // Intialization
        public TraitPoolEvent(TraitPool<T> pool, T trait) {
            this.pool = pool;
            this.trait = trait;
        }
        
    }

}
