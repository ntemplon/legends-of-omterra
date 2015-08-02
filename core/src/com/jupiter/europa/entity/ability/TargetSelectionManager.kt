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

package com.jupiter.europa.entity.ability

import com.jupiter.europa.world.Level
import java.awt.Point
import java.util.ArrayList

/**
 * Created by nathan on 7/23/15.
 */
// First function is the filter, and the second function is the acceptance
public class TargetSelectionManager(public val action: Action, private val startSelection: ((Level, Point) -> Boolean, (Point) -> Unit) -> Unit) {

    private val points: MutableList<Point> = ArrayList()

    public fun beginSelection() {
        if (this.action.targets.size() == 0) {
            this.action.apply(points)
        } else {
            this.select(this.action.targets, 0)
        }
    }

    private fun select(targets: List<(Level, Point) -> Boolean>, index: Int) {
        if (index < targets.size()) {
            this.startSelection(targets[index], { point ->
                this.points.add(point)
                this.select(targets, index + 1)
            })
        } else {
            this.action.apply(points)
        }
    }

}