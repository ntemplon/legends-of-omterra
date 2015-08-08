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

package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.ability.AbilityCategory
import com.jupiter.europa.entity.ability.BasicAbilityCategories
import com.jupiter.europa.util.CategorizedTree
import java.util.TreeSet

/**
 * Created by nathan on 5/21/15.
 */
public class AbilityComponent : Component(), Json.Serializable {

    // Fields
    private val abilities = CategorizedTree<Ability, AbilityCategory>(BasicAbilityCategories.ALL_ABILITIES)


    // Public Methods
    public fun add(ability: Ability) {
        this.abilities.add(ability)
    }

    public fun remove(ability: Ability) {
        this.abilities.remove(ability)
    }

    public fun getAbilities(category: AbilityCategory): Set<Ability> {
        return TreeSet(this.abilities.getItems(category))
    }

    public fun getSubcategories(category: AbilityCategory): Set<AbilityCategory> {
        return TreeSet(this.abilities.getChildren(category))
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
    }

    override fun read(json: Json, jsonValue: JsonValue) {
    }

}
