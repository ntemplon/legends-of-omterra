package com.jupiter.europa.entity.effects

import com.jupiter.europa.entity.ability.AttackAbility
import com.jupiter.europa.entity.ability.MoveAbility
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/24/15.
 */
public class BasicAbilitiesEffect : MultiEffect() {

    // Fields
    private val effectsInternal: List<Effect> by Delegates.lazy { this.generateEffects() }


    // Public Methods
    override fun getEffects(): List<Effect> {
        return this.effectsInternal
    }


    // Private Methods
    private fun generateEffects(): List<Effect> = listOf(
            AbilityGrantingEffect(MoveAbility(this.getEntity())),
            AbilityGrantingEffect(AttackAbility(this.getEntity()))
    )
}
