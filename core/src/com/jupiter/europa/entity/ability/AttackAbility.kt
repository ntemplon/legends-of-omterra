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
public class AttackAbility(private val entity: Entity) : Ability {

    private var sprite: TextureRegion = TextureRegion()


    // Public Methods
    override val name: String = NAME
    override val description: String = "Perform a basic attack with a melee or ranged weapon."
    override val cost: Cost = COST
    override val action: Action = AttackAction(entity)

    override val category: AbilityCategory = BasicAbilityCategories.ALL_ABILITIES

    override val icon: TextureRegion = ICON

    public class AttackAction internal constructor(entity: Entity) : Action(entity) {
    }

    companion object {

        // Constants
        private val NAME = "Attack"
        private val ICON_NAME = "attack"
        private val COST = Cost.NONE

        private val ICON by Delegates.lazy { EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion(ICON_NAME) }
    }
}
