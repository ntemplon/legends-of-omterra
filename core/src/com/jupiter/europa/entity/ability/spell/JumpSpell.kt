/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.jupiter.europa.entity.ability.spell

import com.badlogic.ashley.core.Entity
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
public class JumpSpell(private val entity: Entity) : Ability {


    // Public Methods
    override val name: String = "Jump"
    override val cost: Cost = COST
    override val action: Action = Action(entity)
    override fun getCategory(): AbilityCategory {
        return SpellCategories.LEVEL_ONE
    }

    override val icon: TextureRegion = INNER_ICON

    companion object {

        // TODO: Add Action

        // Static Members
        private val COST = Cost.constant(ResourceComponent.Resources.MANA, 1)
        private val INNER_ICON: TextureRegion by Delegates.lazy({ EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("jump-spell") })
    }
}
