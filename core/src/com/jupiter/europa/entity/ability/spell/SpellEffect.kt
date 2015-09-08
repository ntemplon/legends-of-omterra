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

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.component.Owned
import com.jupiter.europa.entity.effects.AbilityGrantingEffect
import com.jupiter.europa.entity.traits.Qualifications
import com.jupiter.europa.entity.traits.Qualifier

/**
 * Created by nathan on 8/2/15.
 */
public class SpellEffect() : AbilityGrantingEffect(), Owned {

    public constructor(spellClass: Class<out Ability>) : this() {
        this.spellClass = spellClass
    }

    private var spellClass: Class<out Ability>? = null
        get() = this.$spellClass
        set(value) {
            if (value == null) {
                this.$spellClass = null
                this._qualifier = DEFAULT_QUALIFIER
            } else if (value.isAnnotationPresent(javaClass<Spell>())) {
                this.$spellClass = value

                val annotations = value.getAnnotationsByType(javaClass<Spell>())
                this._qualifier = Qualifications.any(*annotations.map { annotation ->
                    Qualifications.level(annotation.characterClass, annotation.level)
                }.toTypedArray())
            } else {
                throw IllegalArgumentException("The provided class must be a spell!")
            }
        }

    private var _qualifier: Qualifier = DEFAULT_QUALIFIER
    override val qualifier: Qualifier
        get() = this._qualifier

    override var owner: Entity? = null
        get() = this.$owner
        set(value) {
            this.$owner = value

            val spellClass = this.spellClass
            if (spellClass != null && value != null) {
                this.ability = SpellFactory.create(spellClass, value)
            } else {
                this.ability = null
            }
        }


    // Effect Implementation
    override fun onAdd(entity: Entity) {
        this.owner = entity
        super<AbilityGrantingEffect>.onAdd(entity)
    }

    override fun onRemove() {
        super<AbilityGrantingEffect>.onRemove()
        this.owner = null
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        val writeClass = this.spellClass
        if (writeClass != null) {
            json.writeValue(CLASS_NAME_KEY, writeClass.getName())
        }
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(CLASS_NAME_KEY)) {
            val readClass = Class.forName(jsonData.get(CLASS_NAME_KEY).asString())
            if (javaClass<Ability>().isAssignableFrom(readClass)) {
                this.spellClass = readClass as Class<out Ability>
            }
        }
    }

    companion object {
        public val CLASS_NAME_KEY: String = "spell-class"

        private val DEFAULT_QUALIFIER = Qualifications.ACCEPT
    }
}