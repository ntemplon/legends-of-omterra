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
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.stats.AttributeSet.Attributes
import java.util.Collections
import java.util.HashMap

/**

 * @author Nathan Templon
 */
public abstract class VariableAttributeModifierEffect : AttributeModifierEffect() {

    // TODO: Finish Implementation

    // Properties
    private val modifiersInternal = HashMap<Attributes, Int>()
    private var attached: Entity? = null

    override val modifiers: Map<Attributes, Int> = Collections.unmodifiableMap(this.modifiersInternal)


    // Public Methods
    override fun onAdd(entity: Entity) {
        if (Families.attributed.matches(entity)) {
            this.attached = entity
            this.recomputeAttributes()
        } else {
            this.attached = null
        }
    }

    override fun onRemove() {
        this.attached = null
    }

    override fun onCombatTurnStart() {
        this.recomputeAttributes()
    }

    override fun onCombatTurnEnd() {
        this.recomputeAttributes()
    }

    override fun onOutOfCombatTurn() {
        this.recomputeAttributes()
    }

    public abstract fun computeModifiers(entity: Entity): Map<Attributes, Int>


    // Private Methods
    private fun recomputeAttributes() {
        val att = this.attached // snapshot
        if (att != null) {
            val oldMods = HashMap(this.modifiers)

            this.modifiersInternal.clear()
            this.modifiersInternal.putAll(this.computeModifiers(att))

            val attributes = Mappers.attributes.get(att).currentAttributes
            this.modifiersInternal.forEach { entry ->
                attributes.setAttribute(entry.getKey(), entry.getValue() + this.modifiersInternal[attributes] - oldMods[entry.getKey()])
            }
        }
    }

}
