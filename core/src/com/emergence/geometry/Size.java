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
package com.emergence.geometry;

/**
 *
 * @author Nathan Templon
 */
public class Size {
    
    // Fields
    public final int width;
    public final int height;
    
    
    // Initialization
    public Size() {
        this.width = 0;
        this.height = 0;
    }
    
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    
    // Public Methods
    @Override
    public String toString() {
        return "(" + this.width + ", " + this.height + ")";
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Size) {
            Size otherSize = (Size)other;
            return (this.width == otherSize.width) && (this.height == otherSize.height);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.width;
        hash = 67 * hash + this.height;
        return hash;
    }
    
}
