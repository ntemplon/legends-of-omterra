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
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.component.ResourceComponent

import java.util.Collections
import java.util.EnumMap

/**
 * Created by nathan on 5/20/15.
 */
public abstract class Cost(public val description: String) {


    // Abstract Methods
    public abstract fun getSpecificCosts(entity: Entity): Map<ResourceComponent.Resources, Int>

    public open fun canPay(entity: Entity): Boolean {
        if (!Families.resourced.matches(entity)) {
            return false
        }

        val costs = this.getSpecificCosts(entity)
        val component = Mappers.resources.get(entity)
        return costs.entrySet()
                .map { entry -> component.getCurrent(entry.getKey()) >= entry.getValue() } // If the specific cost can be payed
                .fold(true, { first, second -> first && second }) // If all costs can be payed
    }

    public open fun exact(entity: Entity) {
        if (!Families.resourced.matches(entity)) {
            return
        }

        val costs = this.getSpecificCosts(entity)
        val component = Mappers.resources.get(entity)
        costs.entrySet()
                .forEach { entry ->
                    component.setCurrent(entry.getKey(), Math.max(0, entry.getValue() - (costs[entry.getKey()] ?: 0)))
                }
    }

    public fun and(other: Cost): Cost {
        return CompositeCost(this, other)
    }

    companion object {

        // Constants
        public val NONE: Cost = object : Cost("No Cost") {

            private val costs = EnumMap<ResourceComponent.Resources, Int>(javaClass<ResourceComponent.Resources>())

            init {
                for (resource in ResourceComponent.Resources.values()) {
                    costs.put(resource, 0)
                }
            }

            private val costsView = Collections.unmodifiableMap(this.costs)

            override fun getSpecificCosts(entity: Entity): Map<ResourceComponent.Resources, Int> {
                return this.costsView
            }

            override fun canPay(entity: Entity): Boolean {
                return true
            }

            override fun exact(entity: Entity) {
            }
        }


        // Static Methods
        public fun constant(resource: ResourceComponent.Resources, cost: Int): Cost {
            return ConstantCost(resource, cost)
        }

        public fun scaling(resource: ResourceComponent.Resources, levelsPerEpoch: Int, costPerEpoch: Int): Cost {
            return ScalingCost(resource, levelsPerEpoch, costPerEpoch)
        }

        public fun all(vararg costs: Cost): Cost {
            return CompositeCost(*costs)
        }
    }
}// Public Methods
