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
package com.emergence.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.entity.stats.Attributes;

/**
 *
 * @author Nathan Templon
 */
public class AttributesComponent extends Component implements Serializable {

    // Constants
    private static final String BASE_ATTRIBUTES_KEY = "base-attributes";
    private static final String CURRENT_ATTRIBUTES_KEY = "current-attributes";
    
    
    // Fields
    private Attributes baseAttributes;
    private Attributes currentAttributes;
    
    
    // Properties
    public final Attributes getBaseAttributes() {
        return this.baseAttributes;
    }
    
    public final Attributes getCurrentAttributes() {
        return this.currentAttributes;
    }
    
    
    // Initialization
    public AttributesComponent() {
        this.baseAttributes = new Attributes();
        this.currentAttributes = new Attributes();
    }
    
    public AttributesComponent(Attributes baseAttributes) {
        this.baseAttributes = baseAttributes;
        this.currentAttributes = new Attributes(this.baseAttributes);
    }
    
    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(BASE_ATTRIBUTES_KEY, this.baseAttributes, Attributes.class);
        json.writeValue(CURRENT_ATTRIBUTES_KEY, this.currentAttributes, Attributes.class);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(BASE_ATTRIBUTES_KEY)) {
            this.baseAttributes = json.fromJson(Attributes.class, jsonData.get(BASE_ATTRIBUTES_KEY).toString());
        }
        if (jsonData.has(CURRENT_ATTRIBUTES_KEY)) {
            this.currentAttributes = json.fromJson(Attributes.class, jsonData.get(CURRENT_ATTRIBUTES_KEY).toString());
        }
    }
    
}
