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
package com.jupiter.europa.settings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.ganymede.property.Property;

/**
 *
 * @author Nathan Templon
 */
public class Settings implements Serializable {
    
    // Constants
    private static final String MUSIC_VOLUME_KEY = "music-volume";

    // Fields
//    private float musicVolume = 1.0f;
//    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    public Property<Float> musicVolume = new Property<>(1.0f);
    
    
    // Initialization
    public Settings() {
        
    }
    
    
    // Serializable (Json) implementation
    @Override
    public void write(Json json) {
        json.writeValue(MUSIC_VOLUME_KEY, this.musicVolume.get());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(MUSIC_VOLUME_KEY)) {
            this.musicVolume.set(jsonData.getFloat(MUSIC_VOLUME_KEY));
        }
    }
    
}
