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


package com.jupiter.europa.entity.effects

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.stats.AttributeSet.Attributes

/**

 * @author Nathan Templon
 */
public abstract class AttributeModifierEffect : Effect {

    // Properties
    private var entity: Entity? = null
    public abstract val modifiers: Map<Attributes, Int>


    // Public Methods
    override fun onAdd(entity: Entity) {
        if (Families.attributed.matches(entity)) {
            this.entity = entity
            val attributes = Mappers.attributes.get(entity).currentAttributes
            for ((attr, mod) in this.modifiers) {
                attributes.setAttribute(attr, attributes.getAttribute(attr) + mod)
            }
        }
    }

    override fun onRemove() {
        if (this.entity != null && Families.attributed.matches(this.entity)) {
            val attributes = Mappers.attributes.get(this.entity).currentAttributes
            for ((attr, mod) in this.modifiers) {
                attributes.setAttribute(attr, attributes.getAttribute(attr) - mod)
            }
        }
    }

    override fun update(deltaT: Float) {
    }

    override fun onCombatTurnStart() {
    }

    override fun onCombatTurnEnd() {
    }

    override fun onOutOfCombatTurn() {
    }

    override fun write(json: Json) {
    }

    override fun read(json: Json, jsonData: JsonValue) {
    }

}