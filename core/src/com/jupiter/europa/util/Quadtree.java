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
package com.jupiter.europa.util;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is still mostly untested; that will have to wait until there are enough entities running around to do
 * something about it.
 *
 * @author Nathan Templon
 * @param <T>
 */
public class Quadtree<T extends RectangularBoundedObject> {

    // Constants
    public static final int DEFAULT_MAX_OBJECTS_PER_LEAF = 10;

    private static final int PARENT_INDEX = -1;
    private static final int TOP_LEFT_INDEX = 0;
    private static final int TOP_RIGHT_INDEX = 1;
    private static final int BOTTOM_LEFT_INDEX = 2;
    private static final int BOTTOM_RIGHT_INDEX = 3;


    // Fields
    private final int maxObjectsPerLeaf;

    private final Rectangle bounds;
    private final Collection<T> objects;
    private final Quadtree<T>[] children;


    // Properties
    public Rectangle getBounds() {
        return this.bounds;
    }


    // Initiallization
    public Quadtree(Rectangle bounds) {
        this.bounds = bounds;

        this.maxObjectsPerLeaf = DEFAULT_MAX_OBJECTS_PER_LEAF;
        this.objects = new HashSet<>();
        this.children = new Quadtree[4];
    }


    // Public Methods
    /**
     * Temporary implementation, should change to include more cases to prevent reordering children so often.
     *
     * @param obj
     */
    public void insert(T obj) {
        Rectangle insertionBounds = obj.getBounds();

        // Check if we are already split
        if (this.children[0] != null) {
            int index = this.getIndexOfContainer(insertionBounds);

            if (index != -1) {
                this.children[index].insert(obj);
                return;
            }
        }

        // At this point, we know that either the object doesn't fit neatly into one of our containers, or we haven't split yet.
        this.objects.add(obj);

        // Let's see if we have too many objects available
        if (this.objects.size() > this.maxObjectsPerLeaf) {
            if (this.children[0] == null) {
                this.split();
            }

            Collection<T> toRemove = new HashSet<>();

            this.objects.stream().forEach((currentObject) -> {
                int index = this.getIndexOfContainer(currentObject.getBounds());
                if (index != PARENT_INDEX) {
                    this.children[index].insert(currentObject);
                    toRemove.add(currentObject);
                }
            });

            toRemove.stream().forEach((currentObject) -> {
                this.objects.remove(currentObject);
            });
        }
    }

    public Collection<T> retrieve(Rectangle bounds) {
        Set<T> returnObjects = new HashSet<>();

        // For each object in my collection, if it intersects the given bounds, add it to the return collection
        this.objects.stream().filter((currentObject) -> (bounds.intersects(currentObject.getBounds()))).forEach(
                (currentObject) -> {
                    returnObjects.add(currentObject);
                });

        for (Quadtree<T> child : this.children) {
            if (child != null && bounds.intersects(child.getBounds())) {
                returnObjects.addAll(child.retrieve(bounds));
            }
        }

        return returnObjects;
    }
    
    public void remove(T obj) {
        if (this.objects.contains(obj)) {
            this.objects.remove(obj);
        }
        
        for(Quadtree<T> child : this.children) {
            child.remove(obj);
        }
        
        // If this takes our total count to below the maximum, we can collapse the child quadtrees
        if (this.size() <= this.maxObjectsPerLeaf) {
            this.recombine();
        }
    }

    public void clear() {
        this.objects.clear();

        for (int i = 0; i < children.length; i++) {
            Quadtree node = this.children[i];
            if (node != null) {
                node.clear();
                this.children[i] = null;
            }
        }
    }
    
    public int size() {
        int count = this.objects.size();
        
        if (this.children[0] != null) {
            for(Quadtree<T> child : this.children) {
                count += child.size();
            }
        }
        
        return count;
    }


    // Private Methods
    private void split() {
        int subWidth = this.bounds.width / 2;
        int subHeight = this.bounds.height / 2;
        int x = this.bounds.x;
        int y = this.bounds.y;

        this.children[TOP_LEFT_INDEX] = new Quadtree<>(new Rectangle(x, y, subWidth, subHeight)); // Top Left
        this.children[TOP_RIGHT_INDEX] = new Quadtree<>(new Rectangle(x + subWidth, y, subWidth, subHeight)); // Top Right
        this.children[BOTTOM_LEFT_INDEX] = new Quadtree<>(new Rectangle(x, y + subHeight, subWidth, subHeight)); // Bottom Left
        this.children[BOTTOM_RIGHT_INDEX] = new Quadtree<>(new Rectangle(x + subWidth, y + subHeight, subWidth,
                subHeight)); // Bottom Right
    }
    
    private void recombine() {
        if (this.children[0] == null) {
            return;
        }
        
        for (int i = 0; i < this.children.length; i++) {
            Quadtree<T> child = this.children[i];
            this.objects.addAll(child.retrieve(child.getBounds()));
            this.children[i] = null;
        }
    }

    private int getIndexOfContainer(Rectangle rect) {
        for (int i = 0; i < this.children.length; i++) {
            if (this.children[i] != null) {
                if (this.children[i].getBounds().contains(rect)) {
                    return i;
                }
            }
        }

        return PARENT_INDEX;
    }
}
