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

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.util.Category
import org.reflections.Reflections

/**
 * Created by nathan on 5/20/15.
 */
public interface AbilityCategory : Category<AbilityCategory> {

    override val parent: AbilityCategory?
    override val name: String
    val icon: TextureRegion

    companion object {
        public val categories: Set<Class<out AbilityCategory>> = Reflections("com.jupiter.europa").getSubTypesOf(javaClass<AbilityCategory>())
    }
}

public enum class BasicAbilityCategories(override val name: String, override val parent: AbilityCategory?, override val icon: TextureRegion) : AbilityCategory {
    ALL_ABILITIES("All Abilities", null, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("all-abilities")),
    SPELLS("Spells", ALL_ABILITIES, EuropaGame.game.assetManager!!.get(FileLocations.ICON_ATLAS, javaClass<TextureAtlas>()).findRegion("spells")),
    COMBAT_MANEUVERS("Combat Maneuvers", ALL_ABILITIES, TextureRegion()),
    FEATS("Feats", ALL_ABILITIES, TextureRegion());
}