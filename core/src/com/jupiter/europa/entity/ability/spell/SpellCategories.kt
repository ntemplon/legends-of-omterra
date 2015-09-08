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

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.ability.AbilityCategory
import com.jupiter.europa.entity.ability.BasicAbilityCategories
import com.jupiter.europa.io.FileLocations

/**
 * Created by nathan on 5/20/15.
 */
public enum class SpellCategories(override val name: String, public val level: Int, override val icon: TextureRegion) : AbilityCategory {
    LEVEL_ONE("Level 1 Spells", 1, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_TWO("Level 2 Spells", 2, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_THREE("Level 3 Spells", 3, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_FOUR("Level 4 Spells", 4, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_FIVE("Level 5 Spells", 5, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_SIX("Level 6 Spells", 6, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_SEVEN("Level 7 Spells", 7, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_EIGHT("Level 8 Spells", 8, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells")),
    LEVEL_NINE("Level 9 Spells", 9, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("first-level-spells"));

    override val parent: AbilityCategory = BasicAbilityCategories.SPELLS
}
