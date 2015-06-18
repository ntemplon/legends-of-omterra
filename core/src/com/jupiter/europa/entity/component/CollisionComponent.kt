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
package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.messaging.PositionChangedMessage
import com.jupiter.europa.geometry.Size
import com.jupiter.europa.util.RectangularBoundedObject
import java.awt.Point
import java.awt.Rectangle

/**

 * @author Nathan Templon
 */
public class CollisionComponent : Component, RectangularBoundedObject, Serializable {


    // Fields
    private var bounds: Rectangle = Rectangle()


    // Properties
    override fun getBounds(): Rectangle {
        return this.bounds
    }

    public fun setLocation(location: Point) {
        this.bounds = Rectangle(location.x, location.y, this.bounds.width, this.bounds.height)
    }

    public fun setSize(size: Size) {
        this.bounds = Rectangle(this.bounds.x, this.bounds.y, size.width, size.height)
    }


    // Initialization
    public constructor() {
        this.bounds = Rectangle()
    }

    public constructor(size: Size) {
        this.bounds = Rectangle(0, 0, size.width, size.height)
    }

    public constructor(location: Point, size: Size) {
        this.bounds = Rectangle(location.x, location.y, size.width, size.height)
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(X_KEY, this.getBounds().x)
        json.writeValue(Y_KEY, this.getBounds().y)
        json.writeValue(WIDTH_KEY, this.getBounds().width)
        json.writeValue(HEIGHT_KEY, this.getBounds().height)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        this.bounds = Rectangle(jsonData.getInt(X_KEY), jsonData.getInt(Y_KEY), jsonData.getInt(WIDTH_KEY), jsonData.getInt(HEIGHT_KEY))
    }


    // Private Methods
    private fun handlePositionChanged(message: PositionChangedMessage) {
        this.setLocation(message.oldLocation)
    }

    companion object {

        // Constants
        public val X_KEY: String = "bounds-x"
        public val Y_KEY: String = "bounds-y"
        public val WIDTH_KEY: String = "bounds-width"
        public val HEIGHT_KEY: String = "bounds-height"
    }

}
