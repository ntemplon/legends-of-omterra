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
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import java.util.EnumMap

/**
 * Created by nathan on 6/16/15.
 */
public class ResourceComponent() : Component(), Json.Serializable, OwnedComponent {

    // Enumerations
    public enum class Resources(public val drawColor: Color) {

        public val displayName: String = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase()

        HEALTH(Color(Color.RED)),
        MANA(Color(Color.BLUE)),
        STAMINA(Color(Color.GREEN)),
        AETHER(Color(0.5f, 0.5f, 0.5f, 0.0f));
    }


    // Properties
    private val maxAmounts = EnumMap<Resources, Int>(javaClass<Resources>())
    private val currentAmounts = EnumMap<Resources, Int>(javaClass<Resources>())

    public override var owner: Entity? = null
        public set(value) {
            this.$owner = value
            this.updateMaxResources()
        }

    public fun getCurrent(res: Resources): Int = this.currentAmounts.get(res)

    public fun setCurrent(res: Resources, amount: Int) {
        this.currentAmounts.put(res, amount)
    }

    public fun getMax(res: Resources): Int = this.maxAmounts.get(res)


    // Public Methods
    public fun updateMaxResources() {
        // TODO: Resource computing code
    }


    public override fun read(json: Json, jsonValue: JsonValue) {
        if (jsonValue.hasChild(MAX_KEY)) {
            for (child in jsonValue.get(MAX_KEY)) {
                try {
                    val name = child.name()
                    val res = Resources.valueOf(name)
                    this.maxAmounts.put(res, child.asInt())
                } catch(ex: Exception) {

                }
            }
        }

        if (jsonValue.hasChild(CURRENT_KEY)) {
            for (child in jsonValue.get(CURRENT_KEY)) {
                try {
                    val name = child.name()
                    val res = Resources.valueOf(name)
                    this.currentAmounts.put(res, child.asInt())
                } catch(ex: Exception) {

                }
            }
        }
    }

    public override fun write(json: Json) {
        json.writeObjectStart(MAX_KEY)
        Resources.values().forEach { res -> json.writeValue(res.toString(), this.maxAmounts.get(res)) }
        json.writeObjectEnd()

        json.writeObjectStart(CURRENT_KEY)
        Resources.values().forEach { res -> json.writeValue(res.toString(), this.currentAmounts.get(res)) }
        json.writeObjectEnd()
    }


    // Initialization
    init {
        for (resource in Resources.values()) {
            this.maxAmounts.put(resource, 0)
            this.currentAmounts.put(resource, 0)
        }
    }


    companion object {
        public val MAX_KEY: String = "max"
        public val CURRENT_KEY: String = "current"
    }

}