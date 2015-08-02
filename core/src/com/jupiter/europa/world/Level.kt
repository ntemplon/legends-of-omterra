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
package com.jupiter.europa.world

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultConnection
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.Disposable
import com.jupiter.europa.audio.AudioService
import com.jupiter.europa.geometry.Size
import com.jupiter.europa.util.Quadtree
import java.awt.Point
import java.awt.Rectangle
import java.util.ArrayList
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * A class representing an individual level (map) in Legends of Omterra

 * @author Nathan Templon
 */
public class Level(public val name: String, map: TiledMap, public val world: World) : Disposable, IndexedGraph<Level.LevelSquare> {

    // Properties
    public var map: TiledMap
        private set(value) {
            this.$map = value

            // Get metrics
            this.tileWidth = value.getProperties().get(TILE_WIDTH_KEY, javaClass<Int>())
            this.tileHeight = value.getProperties().get(TILE_HEIGHT_KEY, javaClass<Int>())
            this.mapWidth = value.getProperties().get(MAP_WIDTH_KEY, javaClass<Int>())
            this.mapHeight = value.getProperties().get(MAP_HEIGHT_KEY, javaClass<Int>())
            this.pixelWidth = this.tileWidth * this.mapWidth
            this.pixelHeight = this.tileHeight * this.mapHeight

            // Set all layers visible, except for the informational layers
            for (layer in value.getLayers()) {
                val layerName = layer.getName()
                if (layerName.equals(ENTITY_LAYER_NAME, ignoreCase = true)) {
                    // Get the entity layer from the map
                    this.entityLayer = EntityLayer(layer, Size(this.mapWidth, this.mapHeight), this)
                } else if (layerName.equals(COLLISION_LAYER_NAME, ignoreCase = true)) {
                    this.collision = this.getCollisionFrom(layer)
                } else if (layerName.equals(ZONE_LAYER_NAME, ignoreCase = true)) {
                } else {
                    layer.setVisible(true)
                }
            }

            // Get the type of music from the map
            if (value.getProperties().containsKey(MUSIC_PROPERTY)) {
                this.musicType = value.getProperties().get(MUSIC_PROPERTY, javaClass<String>())
            }
        }
    private val squares: Array<Array<LevelSquare>>
    public var entityLayer: EntityLayer? = null
        private set
    private var collision: Quadtree<Zone>? = null
    public var musicType: String = AudioService.DEFAULT_MUSIC
        private set

    public var tileWidth: Int = 0
        private set
    public var tileHeight: Int = 0
        private set
    public var mapWidth: Int = 0
        private set
    public var mapHeight: Int = 0
        private set
    public var pixelWidth: Int = 0
        private set
    public var pixelHeight: Int = 0
        private set

    public var controlledEntity: Entity? = null


    // Initialization
    init {
        this.$map = map
        this.map = map

        this.squares = Array(this.mapWidth, { Array<LevelSquare>(this.mapHeight, { LevelSquare(0, 0) }) })
        for (x in 0..(this.mapWidth - 1)) {
            for (y in 0..(this.mapHeight - 1)) {
                this.squares[x][y] = LevelSquare(x, y)
            }
        }
    }


    // Public Methods
    public fun collides(rectangle: Rectangle): Boolean {
        return !this.collision!!.retrieve(rectangle).isEmpty()
    }

    public fun contains(rectangle: Rectangle): Boolean {
        return this.collision!!.bounds.contains(rectangle)
    }

    public fun getSquare(point: Point): LevelSquare {
        return this.squares[point.x][point.y]
    }

    override fun getNodeCount(): Int = this.mapWidth * this.mapHeight

    override fun getConnections(outNode: LevelSquare): GdxArray<Connection<LevelSquare>> = outNode.getConnections()


    // Disposable Implementation
    override fun dispose() {
        this.map.dispose()
    }


    // Private Methods
    private fun getCollisionFrom(layer: MapLayer): Quadtree<Zone> {
        // The collision quadtree, in tile coordinates
        val tree = Quadtree<Zone>(Rectangle(0, 0, this.mapWidth, this.mapHeight))

        for (obj in layer.getObjects()) {
            if (obj is RectangleMapObject) {
                val pixelCoords = obj.getRectangle()

                // Convert from pixel coordinates to tile coordinates.
                // Tmx pixel coordinates have the origin at the top left, libGDX and I use the bottom left for coordinates.
                val collisionX = (pixelCoords.x / this.tileHeight).toInt()
                val collisionY = (pixelCoords.y / this.tileHeight).toInt()
                val collisionWidth = (pixelCoords.width / this.tileWidth).toInt()
                val collisionHeight = (pixelCoords.height / this.tileHeight).toInt()
                val tileCoords = Rectangle(collisionX, collisionY, collisionWidth, collisionHeight)

                tree.insert(Zone(tileCoords))
            }
        }

        return tree
    }

    companion object {

        // Constants
        public val LEVEL_FOLDER: String = "levels"
        public val LEVEL_EXTENSION: String = "tmx"
        public val ENTITY_LAYER_NAME: String = "Entities"
        public val COLLISION_LAYER_NAME: String = "Collision"
        public val ZONE_LAYER_NAME: String = "Zones"

        public val TILE_WIDTH_KEY: String = "tilewidth"
        public val TILE_HEIGHT_KEY: String = "tileheight"
        public val WIDTH_KEY: String = "width"
        public val HEIGHT_KEY: String = "height"
        public val MAP_WIDTH_KEY: String = WIDTH_KEY
        public val MAP_HEIGHT_KEY: String = HEIGHT_KEY

        public val MUSIC_PROPERTY: String = "music"

        public val DEFAULT_TILE_WIDTH: Int = 16
        public val DEFAULT_TILE_HEIGHT: Int = 16
    }


    // Nested Classes
    public inner class LevelSquare constructor(public val x: Int, public val y: Int) : IndexedNode<LevelSquare> {

        private val indexInternal = x + y * this@Level.mapWidth

        override fun getIndex(): Int = indexInternal
        override fun getConnections(): GdxArray<Connection<LevelSquare>> {
            val connections = ArrayList<Point>()
            for (i in -1..1 step 2) {
                val rect = Rectangle(this.x + i, this.y, 1, 1)
                if (!this@Level.collides(rect)) {
                    connections.add(Point(this.x + i, this.y))
                }
            }
            for (j in -1..1 step 2) {
                val rect = Rectangle(this.x, this.y + j, 1, 1)
                if (!this@Level.collides(rect)) {
                    connections.add(Point(this.x, this.y + j))
                }
            }
            return GdxArray(connections.map { point ->
                DefaultConnection<LevelSquare>(this, this@Level.getSquare(point))
            }.toTypedArray())
        }

    }

}
