package com.jupiter.europa.entity.ability

import com.badlogic.ashley.core.Entity
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.component.ResourceComponent

/**
 * Created by nathan on 5/21/15.
 */
public class ScalingCost(private val resource: ResourceComponent.Resources, private val levelsPerEpoch: Int, private val costPerEpoch: Int) :
        Cost(ScalingCost.getDescription(resource, levelsPerEpoch, costPerEpoch)) {

    // Public Methods
    override fun getSpecificCosts(entity: Entity): Map<ResourceComponent.Resources, Int> {
        // Should be 0 for all other resources besides the one used
        return ResourceComponent.Resources.values()
                .map { res ->
                    res to if (res == this.resource) {
                        if (Families.classed.matches(entity)) {
                            val characterClass = Mappers.characterClass.get(entity).characterClass
                            val cost = Math.max(0, costPerEpoch * (characterClass.getLevel() / this.levelsPerEpoch))
                            cost
                        } else {
                            // If the entity has no class, and therefore no level for the resource to scale on
                            0
                        }
                    } else {
                        // If not the resource associated with this cost
                        0
                    }
                }.toMap()
    }


    companion object {
        private fun getDescription(resource: ResourceComponent.Resources, levelsPerEpoch: Int, costPerEpoch: Int): String {
            val sb = StringBuilder()
            sb.append(costPerEpoch)
            sb.append(' ').append(resource.displayName)
            sb.append(" per ")
            if (levelsPerEpoch == 1) {
                sb.append("Level")
            } else {
                sb.append(levelsPerEpoch).append(" Levels")
            }
            return sb.toString()
        }
    }
}
