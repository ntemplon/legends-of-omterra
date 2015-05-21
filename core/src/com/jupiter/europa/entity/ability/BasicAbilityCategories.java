package com.jupiter.europa.entity.ability;

/**
 * Created by nathan on 5/20/15.
 */
public enum BasicAbilityCategories implements AbilityCategory {
    SPELLS("Spells"),
    COMBAT_MANEUVERS("Combat Maneuvers"),
    FEATS("Feats");

    private final String name;

    BasicAbilityCategories(String name) {
        this.name = name;
    }

    @Override
    public AbilityCategory getParent() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
