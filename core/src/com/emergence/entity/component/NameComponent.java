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

/**
 *
 * @author Nathan Templon
 */
public class NameComponent extends Component implements Serializable {

    // Constants
    private final String NAME_KEY = "name";
    
    
    // Fields
    private String name;
    
    
    // Properties
    public String getName() {
        return this.name;
    }
    
    
    // Initialization
    public NameComponent() {
        this.name = "default";
    }
    
    public NameComponent(String name) {
        this.name = name;
    }
    
    
    // Serializable (Json) implementation
    @Override
    public void write(Json json) {
        json.writeValue(NAME_KEY, this.getName());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(NAME_KEY)) {
            this.name = jsonData.getString(NAME_KEY);
        }
    }
    
    
    
}
