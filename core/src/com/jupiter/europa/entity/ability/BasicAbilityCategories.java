package com.jupiter.europa.entity.ability;

/**
 * Created by nathan on 5/20/15.
 */
public enum BasicAbilityCategories implements AbilityCategory {
    ALL_ABILITIES("All Abilities", null),
    SPELLS("Spells", ALL_ABILITIES),
    COMBAT_MANEUVERS("Combat Maneuvers", ALL_ABILITIES),
    FEATS("Feats", ALL_ABILITIES);

    private final String name;
    private final AbilityCategory parent;

    BasicAbilityCategories(String name, AbilityCategory parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public AbilityCategory getParent() {
        return this.parent;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
