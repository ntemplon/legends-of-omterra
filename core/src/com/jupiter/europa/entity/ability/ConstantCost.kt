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