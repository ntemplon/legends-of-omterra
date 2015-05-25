package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.io.FileLocations;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by nathan on 5/21/15.
 */
public class MoveAbility implements Ability {

    private static final String NAME = "Move";
    private static final String ICON_NAME = "move";
    private static final Cost cost = Cost.NONE;


    // Fields
    private final Entity entity;
    private final MoveAction action;

    private TextureRegion icon;


    // Initialization
    public MoveAbility(Entity entity) {
        this.entity = entity;
        this.action = new MoveAction(this.entity);
    }


    // Public Methods
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Cost getCost() {
        return cost;
    }

    @Override
    public Action getAction() {
        return this.action;
    }

    @Override
    public AbilityCategory getCategory() {
        return BasicAbilityCategories.ALL_ABILITIES;
    }

    @Override
    public TextureRegion getIcon() {
        if (this.icon == null) {
            this.icon = EuropaGame.game.getAssetManager().get(FileLocations.ICON_ATLAS, TextureAtlas.class).findRegion(ICON_NAME);
        }
        return this.icon;
    }


    // Inner Classes
    public static class MoveAction extends Action {

        // TODO: Make it take pathing into account (i.e., path exists instead of distance maintainable)
        // Fields
        private final List<TargetInfo> reqList;
        private final Entity entity;


        // Initialization
        private MoveAction(Entity entity) {
            this.entity = entity;

            TargetInfo[] targets = {
                    new TargetInfo(TargetInfo.TargetType.TILE, target -> {
                        if (target instanceof Target.TileTarget && Families.positionables.matches(entity) && Families.attributed.matches(entity)) {
                            Target.TileTarget tileTarget = (Target.TileTarget) target;
                            Point tile = tileTarget.tile;
                            Point location = Mappers.position.get(entity).getTilePosition();
                            int speed = Mappers.attributes.get(entity).getCurrentAttributes().getAttribute(AttributeSet.Attributes.MOVEMENT_SPEED);
                            return Math.abs(tile.x - location.x) + Math.abs(tile.y - location.y) <= speed;
                        } else {
                            return false;
                        }
                    })
            };
            this.reqList = Collections.unmodifiableList(Arrays.asList(targets));
        }

        @Override
        public List<TargetInfo> getTargetRequirements() {
            return this.reqList;
        }

        @Override
        public void apply(List<Target> targets) {
            // TODO: Move to square
        }
    }
}
