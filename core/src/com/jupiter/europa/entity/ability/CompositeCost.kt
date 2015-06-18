package com.jupiter.europa.entity.ability

import com.badlogic.ashley.core.Entity
import com.jupiter.europa.entity.component.ResourceComponent
import java.util.Arrays
import kotlin.platform.platformStatic

/**
 * Created by nathan on 5/21/15.
 */
public class CompositeCost(vararg costs: Cost) : Cost(CompositeCost.getCompositeDescription(Arrays.asList(*costs))) {

    // Fields
    private val costs: List<Cost>


    init {
        this.costs = costs.asList()
    }


    // Public Methods
    override fun getSpecificCosts(entity: Entity): Map<ResourceComponent.Resources, Int> {
        return this.costs.map { it.getSpecificCosts(entity) }
                .flatMap { it.entrySet() }
                .groupBy { it.getKey() }
                .mapValues { entry ->
                    entry.getValue().fold(0, { first, second -> first + second.getValue() })
                }
    }


    // Companion Object
    companion object {
        platformStatic private fun getCompositeDescription(costs: List<Cost>): String {
            return costs
                    .map { cost -> cost.description }
                    .fold(StringBuilder(), { builder, value -> builder.append("\n").append(value) })
                    .toString()
        }
    }
}
