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

package com.jupiter.europa.entity

import com.badlogic.ashley.core.Entity
import com.jupiter.europa.geometry.Size
import com.jupiter.europa.util.RectangularBoundedObject
import java.awt.Point
import java.awt.Rectangle

/**
 * A wrapper class encapsulating an entity, its size, and its position
 * @author Nathan Templon
 */
data public class PositionedEntity
private constructor(public val entity: Entity, private val container: RectangularBoundedObject) : RectangularBoundedObject by container {

    // Initialization
    public constructor(entity: Entity, bounds: Rectangle) : this(entity, RectContainer(bounds))

    public constructor(entity: Entity, position: Point, size: Size) : this(entity, Rectangle(position.x, position.y, size.width, size.height))

    private class RectContainer(private val internalBounds: Rectangle) : RectangularBoundedObject {
        override fun getBounds(): Rectangle {
            return this.internalBounds
        }
    }
}
