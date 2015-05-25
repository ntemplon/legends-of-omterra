package com.jupiter.europa.entity.ability.spell;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jupiter.europa.entity.ability.Ability;
import com.jupiter.europa.entity.ability.AbilityCategory;
import com.jupiter.europa.entity.ability.Action;
import com.jupiter.europa.entity.ability.Cost;
import com.jupiter.europa.entity.stats.characterclass.Magus;

/**
 * Created by nathan on 5/25/15.
 */
@Spell(characterClass = Magus.class, level = 1)
public class JumpSpell implements Ability {

    // Public Methods
    @Override
    public String getName() {
        return "Jump";
    }

    @Override
    public Cost getCost() {
        return null;
    }

    @Override
    public Action getAction() {
        return null;
    }

    @Override
    public AbilityCategory getCategory() {
        return null;
    }

    @Override
    public TextureRegion getIcon() {
        return null;
    }
}
