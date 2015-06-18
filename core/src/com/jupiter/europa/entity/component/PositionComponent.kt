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
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.MovementSystem.MovementDirections
import com.jupiter.europa.world.Level
import com.jupiter.europa.world.Level.DEFAULT_TILE_HEIGHT
import com.jupiter.europa.world.Level.DEFAULT_TILE_WIDTH
import com.jupiter.europa.world.World
import java.awt.Point

/**

 * @author Nathan Templon
 */
public class PositionComponent @jvmOverloads constructor(level: Level? = null, position: Point = Point(0, 0), zOrder: Int = 0) : Component(), Serializable {


    // Fields
    public var tilePosition: Point = Point()
        set(value) {
            this.$tilePosition = value

            if (this.level != null) {
                this.pixelPosition = Point(this.tilePosition.x * this.level!!.getTileWidth(), this.tilePosition.y * this.level!!.getTileHeight())
            } else {
                this.pixelPosition = Point(this.tilePosition.x * DEFAULT_TILE_WIDTH, this.tilePosition.y * DEFAULT_TILE_HEIGHT)
            }
        }
    public var pixelPosition: Point = Point()
        private set
    public var world: World? = null
        private set
    public var level: Level? = null
        set(value) {
            this.$level = value
            if (this.level != null) {
                this.world = this.level!!.getWorld()
            }
        }
    public var zOrder: Int = 0
    public var facingDirection: MovementDirections = MovementDirections.DOWN

    init {
        this.level = level
        this.tilePosition = position
        this.zOrder = zOrder
    }


    // JsonSerializable implementation
    override fun write(json: Json) {
        json.writeValue(WORLD_KEY, this.world?.getName() ?: "")
        json.writeValue(LEVEL_KEY, this.level?.getName() ?: "")
        json.writeValue(TILE_POSITION_X_KEY, this.tilePosition.x)
        json.writeValue(TILE_POSITION_Y_KEY, this.tilePosition.y)
        json.writeValue(Z_ORDER_KEY, this.zOrder)
        json.writeValue(FACING_KEY, this.facingDirection.toString())
    }

    override fun read(json: Json, jsonData: JsonValue) {
        val worldName = jsonData.getString(WORLD_KEY)
        val levelName = jsonData.getString(LEVEL_KEY)
        val tileX = jsonData.getInt(TILE_POSITION_X_KEY)
        val tileY = jsonData.getInt(TILE_POSITION_Y_KEY)
        val zOrderValue = jsonData.getInt(Z_ORDER_KEY)

        this.world = EuropaGame.game.getWorld(worldName)
        this.level = this.world?.getLevel(levelName)
        this.tilePosition = Point(tileX, tileY)
        this.zOrder = zOrderValue

        this.facingDirection = MovementDirections.valueOf(jsonData.getString(FACING_KEY))
    }

    companion object {

        // Constants
        public val WORLD_KEY: String = "world"
        public val LEVEL_KEY: String = "level"
        public val TILE_POSITION_X_KEY: String = "tile-position-x"
        public val TILE_POSITION_Y_KEY: String = "tile-position-y"
        public val Z_ORDER_KEY: String = "z-order"
        public val FACING_KEY: String = "facing"
    }

}
