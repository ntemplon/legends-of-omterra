/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
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
 */
package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.stats.characterclass.Champion
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import com.jupiter.europa.util.Initializable

/**

 * @author Nathan Templon
 */
public class CharacterClassComponent : Component, Serializable, Initializable, OwnedComponent {


    // Properties
    public var characterClass: CharacterClass = Champion()
        private set

    public override var owner: Entity?
        get() {
            return this.characterClass.getOwner()
        }
        set(value) {
            this.characterClass.setOwner(value)
        }


    // Initialization
    public constructor() {
    }

    public constructor(charClass: Class<out CharacterClass>, owner: Entity) {
        try {
            this.characterClass = charClass.newInstance()
        } catch (ex: InstantiationException) {
            this.characterClass = Champion()
        } catch (ex: IllegalAccessException) {
            this.characterClass = Champion()
        }

        this.characterClass.setOwner(owner)
    }

    override fun initialize() {
        this.characterClass.initialize()
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(CHARACTER_CLASS_TYPE_KEY, this.characterClass.javaClass.getName())
        json.writeValue(CHARACTER_CLASS_INSTANCE_KEY, this.characterClass, this.characterClass.javaClass)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(CHARACTER_CLASS_TYPE_KEY)) {
            val className = jsonData.getString(CHARACTER_CLASS_TYPE_KEY)

            if (jsonData.has(CHARACTER_CLASS_INSTANCE_KEY)) {
                try {
                    val classType: Class<out CharacterClass> = try {
                        Class.forName(className) as Class<out CharacterClass>
                    } catch (ex: Exception) {
                        javaClass<Champion>()
                    }
                    if (javaClass<CharacterClass>().isAssignableFrom(classType)) {
                        this.characterClass = json.fromJson(classType, jsonData.get(CHARACTER_CLASS_INSTANCE_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS))
                    }
                } catch (ex: ClassNotFoundException) {
                }
            }
        }
    }

    companion object {

        // Constants
        private val CHARACTER_CLASS_TYPE_KEY = "class-type"
        private val CHARACTER_CLASS_INSTANCE_KEY = "class-instance"
    }

}
