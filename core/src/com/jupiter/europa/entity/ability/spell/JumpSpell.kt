package com.jupiter.europa.entity.ability.spell

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.ability.AbilityCategory
import com.jupiter.europa.entity.ability.Action
import com.jupiter.europa.entity.ability.Cost
import com.jupiter.europa.entity.component.ResourceComponent
import com.jupiter.europa.entity.stats.characterclass.Magus
import com.jupiter.europa.io.FileLocations
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/25/15.
 */
Spell(characterClass = Magus::class, level = 1)
public class JumpSpell : Ability {


    // Public Methods
    override val name: String = "Jump"
    override val cost: Cost = COST
    override val action: Action = Action.NO_OP
    override fun getCategory(): AbilityCategory {
        return SpellCategories.LEVEL_ONE
    }

    override val icon: TextureRegion = innerIcon

    companion object {

        // TODO: Add Action

        // Static Members
        private val COST = Cost.constant(ResourceComponent.Resources.MANA, 1)
        private val innerIcon: TextureRegion by Delegates.lazy({ EuropaGame.game.getAssetManager().get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("jump-spell") })
    }
}
