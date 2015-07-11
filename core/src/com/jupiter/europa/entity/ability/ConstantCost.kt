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

import com.badlogic.ashley.core.Entity
import com.jupiter.europa.entity.component.ResourceComponent

/**
 * Created by nathan on 6/13/15.
 */
public class ConstantCost(private val costs: Map<ResourceComponent.Resources, Int>) : Cost(ConstantCost.getCostDescription(costs)) {

    public constructor(res: ResourceComponent.Resources, cost: Int) : this(mapOf(res to cost))

    public override fun getSpecificCosts(entity: Entity) = this.costs

    // Companion Object
    companion object {
        private fun getCostDescription(costs: Map<ResourceComponent.Resources, Int>): String {
            return costs.entrySet()
                    .map { entry -> entry.getValue().toString() + " " + entry.getKey() }
                    .fold(StringBuilder(), { builder, value -> builder.append("\n").append(value) })
                    .toString()
        }
    }
}