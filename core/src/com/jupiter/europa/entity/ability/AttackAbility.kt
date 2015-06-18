package com.jupiter.europa.entity.ability

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.io.FileLocations
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/22/15.
 */
public class AttackAbility// Initialization
(// Fields
        private val entity: Entity) : Ability {

    private var sprite: TextureRegion = TextureRegion()


    // Public Methods
    override val name: String = NAME
    override val cost: Cost = COST
    override val action: Action = ACTION

    override fun getCategory(): AbilityCategory {
        return BasicAbilityCategories.ALL_ABILITIES
    }

    override val icon: TextureRegion = ICON

    public class AttackAction private constructor(private val entity: Entity) : Action() {

        //TODO: Basically Everything

        // Public Methods
        override fun getTargetRequirements(): List<TargetInfo<Target>> {
            return listOf()
        }

        override fun apply(targets: List<Target>) {
        }
    }

    companion object {

        // Constants
        private val NAME = "Attack"
        private val ICON_NAME = "attack"
        private val COST = Cost.NONE
        private val ACTION = Action.NO_OP
        private val ICON by Delegates.lazy { EuropaGame.game.getAssetManager().get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion(ICON_NAME) }
    }
}
