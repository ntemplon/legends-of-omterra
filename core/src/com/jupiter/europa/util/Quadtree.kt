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
package com.jupiter.europa.util

import java.awt.Rectangle
import java.util.HashSet

/**
 * This class is still mostly untested; that will have to wait until there are enough entities running around to do
 * something about it.

 * @author Nathan Templon
 * *
 * @param
 */
public class Quadtree<T : RectangularBoundedObject>(public val bounds: Rectangle) {


    // Fields
    private val maxObjectsPerLeaf: Int
    private val objects: MutableCollection<T>
    private val children: Array<Quadtree<T>?>


    init {

        this.maxObjectsPerLeaf = DEFAULT_MAX_OBJECTS_PER_LEAF
        this.objects = HashSet<T>()
        this.children = arrayOfNulls<Quadtree<T>>(4)
    }


    // Public Methods
    /**
     * Temporary implementation, should change to include more cases to prevent reordering children so often.

     * @param obj
     */
    public fun insert(obj: T) {
        val insertionBounds = obj.getBounds()

        // Check if we are already split
        if (this.children[0] != null) {
            val index = this.getIndexOfContainer(insertionBounds)

            if (index != -1) {
                this.children[index]?.insert(obj)
                return
            }
        }

        // At this point, we know that either the object doesn't fit neatly into one of our containers, or we haven't split yet.
        this.objects.add(obj)

        // Let's see if we have too many objects available
        if (this.objects.size() > this.maxObjectsPerLeaf) {
            if (this.children[0] == null) {
                this.split()
            }

            val toRemove = HashSet<T>()

            this.objects.forEach { currentObject ->
                val index = this.getIndexOfContainer(currentObject.getBounds())
                if (index != PARENT_INDEX) {
                    this.children[index]?.insert(currentObject)
                    toRemove.add(currentObject)
                }
            }

            toRemove.forEach { currentObject ->
                this.objects.remove(currentObject)
            }
        }
    }

    public fun retrieve(bounds: Rectangle): Collection<T> {
        val returnObjects = HashSet<T>()

        // For each object in my collection, if it intersects the given bounds, add it to the return collection
        this.objects
                .filter { currentObject -> bounds.intersects(currentObject.getBounds()) }
                .forEach { currentObject -> returnObjects.add(currentObject) }

        for (child in this.children) {
            if (child != null && bounds.intersects(child.bounds)) {
                returnObjects.addAll(child.retrieve(bounds))
            }
        }

        return returnObjects
    }

    public fun remove(obj: T) {
        if (this.objects.contains(obj)) {
            this.objects.remove(obj)
        }

        for (child in this.children) {
            child?.remove(obj)
        }

        // If this takes our total count to below the maximum, we can collapse the child quadtrees
        if (this.size() <= this.maxObjectsPerLeaf) {
            this.recombine()
        }
    }

    public fun clear() {
        this.objects.clear()

        for (i in children.indices) {
            val node = this.children[i]
            if (node != null) {
                node.clear()
                this.children[i] = null
            }
        }
    }

    public fun size(): Int {
        var count = this.objects.size()

        if (this.children[0] != null) {
            for (child in this.children) {
                count += child?.size() ?: 0
            }
        }

        return count
    }


    // Private Methods
    private fun split() {
        val subWidth = this.bounds.width / 2
        val subHeight = this.bounds.height / 2
        val x = this.bounds.x
        val y = this.bounds.y

        this.children[TOP_LEFT_INDEX] = Quadtree<T>(Rectangle(x, y, subWidth, subHeight)) // Top Left
        this.children[TOP_RIGHT_INDEX] = Quadtree<T>(Rectangle(x + subWidth, y, subWidth, subHeight)) // Top Right
        this.children[BOTTOM_LEFT_INDEX] = Quadtree<T>(Rectangle(x, y + subHeight, subWidth, subHeight)) // Bottom Left
        this.children[BOTTOM_RIGHT_INDEX] = Quadtree<T>(Rectangle(x + subWidth, y + subHeight, subWidth, subHeight)) // Bottom Right
    }

    private fun recombine() {
        if (this.children[0] == null) {
            return
        }

        for (i in this.children.indices) {
            val child = this.children[i]
            this.objects.addAll(child?.retrieve(child?.bounds ?: Rectangle()) ?: setOf())
            this.children[i] = null
        }
    }

    private fun getIndexOfContainer(rect: Rectangle): Int {
        for (i in this.children.indices) {
            if (this.children[i] != null) {
                if (this.children[i]?.bounds?.contains(rect) ?: false) {
                    return i
                }
            }
        }

        return PARENT_INDEX
    }

    companion object {

        // Constants
        public val DEFAULT_MAX_OBJECTS_PER_LEAF: Int = 10

        private val PARENT_INDEX = -1
        private val TOP_LEFT_INDEX = 0
        private val TOP_RIGHT_INDEX = 1
        private val BOTTOM_LEFT_INDEX = 2
        private val BOTTOM_RIGHT_INDEX = 3
    }
}
