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
import com.jupiter.europa.EuropaGame

/**

 * @author Nathan Templon
 */
public open class MultiEffect(vararg effects: Effect = listOf<Effect>().toTypedArray()) : Effect {


    // Properties
    public open var effects: List<Effect> = effects.toList()
        private set
    public var entity: Entity? = null
        private set

    // Public Methods
    override fun onAdd(entity: Entity) {
        this.entity = entity
        for (effect in this.effects) {
            effect.onAdd(entity)
        }
    }

    override fun onRemove() {
        this.entity = null
        this.effects.forEach { it.onRemove() }
    }

    override fun update(deltaT: Float) {
        for (effect in this.effects) {
            effect.update(deltaT)
        }
    }

    override fun onCombatTurnStart() {
        this.effects.forEach { it.onCombatTurnStart() }
    }

    override fun onCombatTurnEnd() {
        this.effects.forEach { it.onCombatTurnEnd() }
    }

    override fun onOutOfCombatTurn() {
        for (effect in this.effects) {
            effect.onOutOfCombatTurn()
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeArrayStart(EFFECTS_KEY)
        for (effect in this.effects!!) {
            json.writeObjectStart()
            json.writeValue(EFFECT_CLASS_KEY, effect.javaClass.getName())
            json.writeValue(EFFECT_DATA_KEY, effect, effect.javaClass)
            json.writeObjectEnd()
        }
        json.writeArrayEnd()
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(EFFECTS_KEY)) {
            this.effects = jsonData.get(EFFECTS_KEY)
                    .filter { value -> value.has(EFFECT_CLASS_KEY) && value.has(EFFECT_DATA_KEY) }
                    .map { value ->
                        val type: Class<*> = Class.forName(value.getString(EFFECT_CLASS_KEY))
                        if (javaClass<Effect>().isAssignableFrom(type)) {
                            json.fromJson(type, value.get(EFFECT_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS)) as? Effect
                        } else {
                            null
                        } // return if value
                    } // type Effect?
                    .filterNotNull()
        }
    }

    companion object {

        // Constants
        private val EFFECTS_KEY = "effects"
        private val EFFECT_CLASS_KEY = "effect-class"
        private val EFFECT_DATA_KEY = "effect-data"
    }

}
