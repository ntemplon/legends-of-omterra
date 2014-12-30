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
import com.emergence.entity.messaging.PositionChangedMessage;
import com.emergence.geometry.Size;
import com.emergence.util.RectangularBoundedObject;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Nathan Templon
 */
public class CollisionComponent extends Component implements RectangularBoundedObject, Serializable {

    // Constants
    public static final String X_KEY = "bounds-x";
    public static final String Y_KEY = "bounds-y";
    public static final String WIDTH_KEY = "bounds-width";
    public static final String HEIGHT_KEY = "bounds-height";


    // Fields
    private Rectangle bounds;


    // Properties
    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    public void setLocation(Point location) {
        this.bounds = new Rectangle(location.x, location.y, this.bounds.width, this.bounds.height);
    }

    public void setSize(Size size) {
        this.bounds = new Rectangle(this.bounds.x, this.bounds.y, size.width, size.height);
    }


    // Initialization
    public CollisionComponent() {
        this.bounds = new Rectangle();
    }
    
    public CollisionComponent(Size size) {
        this.bounds = new Rectangle(0, 0, size.width, size.height);
    }

    public CollisionComponent(Point location, Size size) {
        this.bounds = new Rectangle(location.x, location.y, size.width, size.height);
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(X_KEY, this.getBounds().x);
        json.writeValue(Y_KEY, this.getBounds().y);
        json.writeValue(WIDTH_KEY, this.getBounds().width);
        json.writeValue(HEIGHT_KEY, this.getBounds().height);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.bounds = new Rectangle(jsonData.getInt(X_KEY), jsonData.getInt(Y_KEY), jsonData.getInt(WIDTH_KEY),
                jsonData.getInt(HEIGHT_KEY));
    }


    // Private Methods
    private void handlePositionChanged(PositionChangedMessage message) {
        this.setLocation(message.location);
    }

}
