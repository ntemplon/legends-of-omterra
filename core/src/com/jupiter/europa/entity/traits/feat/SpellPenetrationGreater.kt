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

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.effects.AttributeModifierEffect
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.traits.Qualifications

/**
 * Created by nathan on 5/18/15.
 */
public class SpellPenetrationGreater : AttributeModifierEffect(), Feat {

    // Feat Implementation
    override val qualifier = Qualifications.has(javaClass<SpellPenetration>()).and(Qualifications.lacks(javaClass<SpellPenetrationGreater>()))
    override val icon = Sprite()
    override val name = "Spell Penetration (Greater)"
    override val description = "You spells ignore an additional 10 of their targets' Spell Resistance (stacks with Spell Penetration)."

    override val modifiers = mapOf(Pair(AttributeSet.Attributes.SPELL_PENETRATION, 10))


    // Serializable (json) implementation
    override fun write(json: Json) {
    }

    override fun read(json: Json, jsonData: JsonValue) {
    }

}
