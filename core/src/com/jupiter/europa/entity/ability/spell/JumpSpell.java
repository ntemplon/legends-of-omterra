package com.jupiter.europa.entity.ability.spell;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.ability.Ability;
import com.jupiter.europa.entity.ability.AbilityCategory;
import com.jupiter.europa.entity.ability.Action;
import com.jupiter.europa.entity.ability.Cost;
import com.jupiter.europa.entity.component.ResourceComponent;
import com.jupiter.europa.entity.stats.characterclass.Magus;
import com.jupiter.europa.io.FileLocations;

/**
 * Created by nathan on 5/25/15.
 */
@Spell(characterClass = Magus.class, level = 1)
public class JumpSpell implements Ability {

    // TODO: Add Action

    // Static Members
    private static final Cost COST = Cost.constant(ResourceComponent.Resources.MANA, 1);
    private static TextureRegion icon;


    // Public Methods
    @Override
    public String getName() {
        return "Jump";
    }

    @Override
    public Cost getCost() {
        return COST;
    }

    @Override
    public Action getAction() {
        return null;
    }

    @Override
    public AbilityCategory getCategory() {
        return SpellCategories.LEVEL_ONE;
    }

    @Override
    public TextureRegion getIcon() {
        if (icon == null) {
            synchronized (icon) {
                if (icon == null) {
                    icon = EuropaGame.game.getAssetManager().get(FileLocations.ICON_ATLAS, TextureAtlas.class).findRegion("jump-spell");
                }
            }
        }
        return icon;
    }
}
