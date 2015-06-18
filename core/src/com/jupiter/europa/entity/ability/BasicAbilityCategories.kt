package com.jupiter.europa.entity.ability

/**
 * Created by nathan on 5/20/15.
 */
public enum class BasicAbilityCategories(private val name: String, private val parent: AbilityCategory?) : AbilityCategory {
    ALL_ABILITIES("All Abilities", null),
    SPELLS("Spells", ALL_ABILITIES),
    COMBAT_MANEUVERS("Combat Maneuvers", ALL_ABILITIES),
    FEATS("Feats", ALL_ABILITIES);

    override fun getParent(): AbilityCategory? {
        return this.parent
    }

    override fun getName(): String {
        return this.name
    }

}
