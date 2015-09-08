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

import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import org.reflections.Reflections

/**
 * Created by nathan on 5/25/15.
 */
public object Spells {

    // Constants
    private val spellReflections = Reflections("com.jupiter.europa")
    private val spellTypes: Set<Class<out Ability>> = spellReflections.getTypesAnnotatedWith(javaClass<Spell>(), true)
            .filter { type -> javaClass<Ability>().isAssignableFrom(type) }
            .map { type -> type as? java.lang.Class<out Ability> }
            .filterNotNull()
            .toSet()
    private val SPELL_LIST: Map<Class<out CharacterClass>, Map<Int, Set<Class<out Ability>>>> = spellTypes
            .groupBy({ type -> type.getAnnotation(javaClass<Spell>()).characterClass })
            .mapValues { entry ->
                entry.getValue().groupBy { spellType ->
                    spellType.getAnnotation(javaClass<Spell>()).level
                }
                        .mapValues { entry -> entry.getValue().toSet() }
            }
    private val EMPTY: Set<Class<out Ability>> = setOf()


    // Static Methods
    public fun getSpells(characterClass: Class<out CharacterClass>, level: Int): Set<Class<out Ability>> {
        if (SPELL_LIST.containsKey(characterClass)) {
            val spells = SPELL_LIST.get(characterClass)
            if (spells != null && spells.containsKey(level)) {
                return spells.get(level) ?: EMPTY
            }
        }
        return EMPTY
    }
}
