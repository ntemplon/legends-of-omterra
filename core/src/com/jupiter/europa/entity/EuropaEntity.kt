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

package com.jupiter.europa.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.component.OwnedComponent
import com.jupiter.europa.util.Initializable

/**

 * @author Nathan Templon
 */
public class EuropaEntity : Entity(), Serializable {


    // Public Methods
    public fun initializeComponents() {
        for (component in this.getComponents()) {
            if (component is OwnedComponent) {
                component.owner = this
            }
            if (component is Initializable) {
                component.initialize()
            }
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(OLD_ID_KEY, this.getId())

        json.writeArrayStart(COMPONENTS_KEY)
        for (component in this.getComponents()) {
            if (component is Serializable) {
                json.writeObjectStart()
                json.writeValue(COMPONENT_CLASS_KEY, component.javaClass.getName())
                json.writeValue(COMPONENT_DATA_KEY, component, component.javaClass)
                json.writeObjectEnd()
            }
        }
        json.writeArrayEnd()
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(OLD_ID_KEY)) {
            val oldID = jsonData.getLong(OLD_ID_KEY)
            EuropaGame.game.lastIdMapping.put(oldID, this)
        }

        if (jsonData.has(COMPONENTS_KEY)) {
            val componentsValue = jsonData.get(COMPONENTS_KEY)

            componentsValue.asSequence().forEach { value ->
                try {
                    if (value.has(COMPONENT_CLASS_KEY)) {
                        val type = Class.forName(value.getString(COMPONENT_CLASS_KEY));
                        if (javaClass<Component>().isAssignableFrom(type)) {
                            this.add(json.fromJson(type, value.get(COMPONENT_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS)) as Component);
                        }
                    }
                } catch (ex: ClassNotFoundException) {

                }
            }

            this.initializeComponents()
        }
    }

    companion object {

        // Constants
        public val COMPONENTS_KEY: String = "components"
        public val OLD_ID_KEY: String = "id"
        public val COMPONENT_CLASS_KEY: String = "component-class"
        public val COMPONENT_DATA_KEY: String = "component-data"
    }

}
