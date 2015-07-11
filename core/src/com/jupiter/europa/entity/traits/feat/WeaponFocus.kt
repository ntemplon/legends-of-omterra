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


package com.jupiter.europa.entity.traits.feat

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.effects.VariableAttributeModifierEffect
import com.jupiter.europa.entity.stats.AttributeSet.Attributes
import com.jupiter.europa.entity.traits.FeatNotPresentQualifier

/**

 * @author Nathan Templon
 */
public class WeaponFocus : Feat {

    // Properties
    override val qualifier = FeatNotPresentQualifier(javaClass<WeaponFocus>())
    override val effect = WeaponFocusEffect()
    override val icon = Sprite()
    override val name = "Weapon Focus"
    override val description = "Your attack bonus increases by 10."


    // Serializable (Json) Implementation
    override fun write(json: Json) {
    }

    override fun read(json: Json, jsonData: JsonValue) {
    }


    // Effect
    private class WeaponFocusEffect : VariableAttributeModifierEffect() {

        // Fields
        override val modifiers = hashMapOf(Pair(Attributes.ATTACK_BONUS, 10))


        // Properties
        override fun computeModifiers(entity: Entity): Map<Attributes, Int> {
            this.modifiers.clear()

            if (Families.classed.matches(entity)) {
                val charClass = Mappers.characterClass.get(entity).characterClass

                this.modifiers.put(Attributes.ATTACK_BONUS, 5 + charClass.level)
            }

            return this.modifiers
        }

    }

}
