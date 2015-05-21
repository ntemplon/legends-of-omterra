package com.jupiter.europa.entity.ability.spell;

import com.jupiter.europa.entity.ability.AbilityCategory;
import com.jupiter.europa.entity.ability.BasicAbilityCategories;

/**
 * Created by nathan on 5/20/15.
 */
public enum SpellCategories implements AbilityCategory {
    LEVEL_ONE("Level 1"),
    LEVEL_TWO("Level 2"),
    LEVEL_THREE("Level 3"),
    LEVEL_FOUR("Level 4"),
    LEVEL_FIVE("Level 5"),
    LEVEL_SIX("Level 6"),
    LEVEL_SEVEN("Level 7"),
    LEVEL_EIGHT("Level 8"),
    LEVEL_NINE("Level 9");

    private final String name;

    SpellCategories(String name) {
        this.name = name;
    }

    @Override
    public AbilityCategory getParent() {
        return BasicAbilityCategories.SPELLS;
    }

    public String getName() {
        return name;
    }
}
