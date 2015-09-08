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
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.effects.Effect
import com.jupiter.europa.entity.messaging.RequestEffectAddMessage
import java.util.ArrayList

/**
 * @author Nathan Templon
 */
public class EffectsComponent : Component(), Serializable, Owned {


    // Fields
    public val effects: MutableCollection<Effect> = ArrayList()
    private var efQueue: MutableList<Effect> = arrayListOf()


    // Properties
    public override var owner: Entity? = null
        set(value) {
            this.$owner = value
            if (value != null) {
                this.efQueue
                        .map { effect -> RequestEffectAddMessage(value, effect) }
                        .forEach { EuropaGame.game.messageSystem.publish(it) }
            }
            this.efQueue = arrayListOf()
        }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeArrayStart(EFFECTS_KEY)
        this.effects.forEach { effect ->
            json.writeObjectStart()
            json.writeValue(EFFECT_CLASS_KEY, effect.javaClass.getName())
            json.writeValue(EFFECT_DATA_KEY, effect, effect.javaClass)
            json.writeObjectEnd()
        }
        json.writeArrayEnd()
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(EFFECTS_KEY)) {
            val selectedData = jsonData.get(EFFECTS_KEY)
            if (selectedData.isArray()) {
                selectedData.filter { value -> value.has(EFFECT_CLASS_KEY) && (value.has(EFFECT_DATA_KEY) || value.hasChild(EFFECT_DATA_KEY)) }
                        .forEach { value ->
                            val typeName = value.getString(EFFECT_CLASS_KEY)
                            var text: String = ""
                            //                            try {
                                val type = Class.forName(typeName)
                            // TODO: NOW: Can't parse data from BasicAbilitiesEffect
                                if (javaClass<Effect>().isAssignableFrom(type)) {
                                    text = value.get(EFFECT_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS)
                                    val effect = json.fromJson(type, value.get(EFFECT_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS)) as? Effect
                                    if (effect != null) {
                                        this.efQueue.add(effect)
                                    }
                                }
                            //                            } catch (ex: Exception) {
                            //                                println(ex.javaClass.getName())
                            //                                println(text)
                            //                            }
                        }
            }
        }
    }

    companion object {

        // Constants
        private val EFFECTS_KEY = "effects"
        private val EFFECT_CLASS_KEY = "effect-class"
        private val EFFECT_DATA_KEY = "effect-data"
    }
}
