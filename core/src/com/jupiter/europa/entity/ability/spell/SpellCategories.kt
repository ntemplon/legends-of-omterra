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

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.entity.ability.AbilityCategory
import com.jupiter.europa.entity.ability.BasicAbilityCategories

/**
 * Created by nathan on 5/20/15.
 */
public enum class SpellCategories(override val name: String) : AbilityCategory {
    LEVEL_ONE("Level 1"),
    LEVEL_TWO("Level 2"),
    LEVEL_THREE("Level 3"),
    LEVEL_FOUR("Level 4"),
    LEVEL_FIVE("Level 5"),
    LEVEL_SIX("Level 6"),
    LEVEL_SEVEN("Level 7"),
    LEVEL_EIGHT("Level 8"),
    LEVEL_NINE("Level 9");

    override val parent: AbilityCategory = BasicAbilityCategories.SPELLS

    // TODO: Actual Icons
    override val icon: TextureRegion = TextureRegion()
}
