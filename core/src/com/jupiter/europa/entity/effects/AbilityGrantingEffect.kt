package com.jupiter.europa.entity.effects

import com.badlogic.ashley.core.Entity
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.ability.Ability

/**
 * Created by nathan on 5/21/15.
 */
public class AbilityGrantingEffect(public val ability: Ability) : BaseEffect() {

    // Public Methods
    override fun onAdd(entity: Entity) {
        super.onAdd(entity)
        if (Families.abilitied.matches(entity)) {
            Mappers.abilities.get(entity).add(this.ability)
        }
    }

    override fun onRemove() {
        super.onRemove()
        if (Families.abilitied.matches(this.entity)) {
            Mappers.abilities.get(this.entity).remove(this.ability)
        }
    }
}
