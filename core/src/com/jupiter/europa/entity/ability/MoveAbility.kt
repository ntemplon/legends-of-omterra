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
import java.util.Arrays
import java.util.Collections
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/21/15.
 */
public class MoveAbility(private val entity: Entity) : Ability {
    override val action: MoveAction

    init {
        this.action = MoveAction(this.entity)
    }


    // Public Methods
    override val name: String = NAME
    override val cost: Cost = COST
    override fun getCategory(): AbilityCategory {
        return BasicAbilityCategories.ALL_ABILITIES
    }

    override val icon: TextureRegion = ICON


    // Inner Classes
    public class MoveAction// Initialization
    internal constructor(private val entity: Entity) : Action() {

        // TODO: Make it take pathing into account (i.e., path exists instead of distance maintainable)
        // Fields
        private val reqList: List<TargetInfo<Target>>


        init {

            val targets = arrayOf<TargetInfo<Target>>()
            this.reqList = Collections.unmodifiableList(Arrays.asList(*targets))
        }

        override fun getTargetRequirements(): List<TargetInfo<Target>> {
            return this.reqList
        }

        public override fun apply(targets: List<Target>) {
            // TODO: Move to square
        }
    }

    companion object {

        private val NAME = "Move"
        private val ICON_NAME = "move"
        private val COST = Cost.NONE
        private val ICON by Delegates.lazy { EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion(ICON_NAME) }
    }
}
