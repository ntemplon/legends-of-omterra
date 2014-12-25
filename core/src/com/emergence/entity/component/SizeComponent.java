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
import com.emergence.geometry.Size;

/**
 *
 * @author Nathan Templon
 */
public class SizeComponent extends Component implements Serializable {
    
    // Constants
    public static final String WIDTH_KEY = "width";
    public static final String HEIGHT_KEY = "height";
    
    
    // Fields
    private Size size;
    
    
    // Properties
    public Size getSize() {
        return this.size;
    }
    
    public void setSize(Size size) {
        this.size = size;
    }
    
    
    // Initialization
    public SizeComponent() {
        this(new Size());
    }
    
    public SizeComponent(Size size) {
        this.size = size;
    }

    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(WIDTH_KEY, this.getSize().width);
        json.writeValue(HEIGHT_KEY, this.getSize().height);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.setSize(new Size(jsonData.getInt(WIDTH_KEY), jsonData.getInt(HEIGHT_KEY)));
    }
    
}
