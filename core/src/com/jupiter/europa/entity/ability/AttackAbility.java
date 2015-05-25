package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.io.FileLocations;

import java.util.List;

/**
 * Created by nathan on 5/22/15.
 */
public class AttackAbility implements Ability {

    // Constants
    private static final String NAME = "Attack";
    private static final String ICON_NAME = "attack";
    private static final Cost COST = Cost.NONE;


    // Fields
    private final Entity entity;

    private TextureRegion sprite;


    // Initialization
    public AttackAbility(Entity entity) {
        this.entity = entity;
    }


    // Public Methods
    @Override
    public String getName() {
        return NAME;
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
        return BasicAbilityCategories.ALL_ABILITIES;
    }

    @Override
    public TextureRegion getIcon() {
        if (this.sprite == null) {
            this.sprite = EuropaGame.game.getAssetManager().get(FileLocations.ICON_ATLAS, TextureAtlas.class).findRegion(ICON_NAME);
        }
        return this.sprite;
    }


    public static class AttackAction extends Action {

        //TODO: Basically Everything

        // Fields
        private final Entity entity;


        // Initialization
        private AttackAction(Entity entity) {
            this.entity = entity;
        }


        // Public Methods
        @Override
        public List<TargetInfo> getTargetRequirements() {
            return null;
        }

        @Override
        public void apply(List<Target> targets) {

        }
    }
}
