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
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.ability.Ability

/**
 * Created by nathan on 5/21/15.
 */
public open class AbilityGrantingEffect(ability: Ability? = null) : BaseEffect() {

    // Properties
    private var _icon = DEFAULT_ICON
    private var _name = DEFAULT_NAME
    private var _description = DEFAULT_DESCRIPTION

    override val icon: Sprite
        get() = this._icon
    override val name: String
        get() = this._name
    override val description: String
        get() = this._description

    public var ability: Ability? = ability
        public get() = this.$ability
        internal set(value) {
            this.$ability = value
            if (value == null) {
                this._icon = DEFAULT_ICON
                this._name = DEFAULT_NAME
                this._description = DEFAULT_DESCRIPTION
            } else {
                this._icon = Sprite(value.icon)
                this._name = value.name
                this._description = value.description
            }
        }

    // Public Methods
    override fun onAdd(entity: Entity) {
        super.onAdd(entity)

        val curAbility = this.ability
        if (curAbility != null && Families.abilitied.matches(entity)) {
            Mappers.abilities.get(entity).add(curAbility)
        }
    }

    override fun onRemove() {
        super.onRemove()

        val curAbility = this.ability
        if (curAbility != null && Families.abilitied.matches(this.entity)) {
            Mappers.abilities.get(this.entity).remove(curAbility)
        }
    }

    // Serializable Implementation
    override fun read(json: Json, jsonValue: JsonValue) {
        if (jsonValue.has(ABILITY_CLASS_KEY)) {
            val typeName: String = jsonValue.getString(ABILITY_CLASS_KEY)
            try {
                val type: Class<*> = Class.forName(typeName)
                this.ability = type.newInstance() as? Ability
            } catch (ex: Exception) {

            }
        }
    }

    override fun write(json: Json) {
        val curAbility = this.ability
        if (curAbility != null) {
            json.writeValue(ABILITY_CLASS_KEY, curAbility.javaClass.getName())
        }
    }

    // Companion Object
    companion object {
        private val DEFAULT_NAME = "name"
        private val DEFAULT_DESCRIPTION = "description"
        private val DEFAULT_ICON = Sprite()

        public val ABILITY_CLASS_KEY: String = "ability-class"
    }
}
