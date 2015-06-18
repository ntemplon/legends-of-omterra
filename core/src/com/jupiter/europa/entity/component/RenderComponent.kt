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
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import java.awt.Point

/**

 * @author Nathan Templon
 */
public class RenderComponent() : Component(), Serializable {


    // Fields
    public var sprite: Sprite = Sprite()
    public var offset: Point = Point()


    public constructor(sprite: Sprite) : this() {
        this.sprite = sprite
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(OFFSET_X_KEY, this.offset.x)
        json.writeValue(OFFSET_Y_KEY, this.offset.y)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        this.offset = Point(jsonData.getInt(OFFSET_X_KEY), jsonData.getInt(OFFSET_Y_KEY))
    }

    companion object {

        // Constants
        public val OFFSET_X_KEY: String = "offset-x"
        public val OFFSET_Y_KEY: String = "offset-y"
    }

}
