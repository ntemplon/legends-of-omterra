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
package com.jupiter.europa.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.component.OwnedComponent;
import com.jupiter.europa.util.Initializable;

/**
 *
 * @author Nathan Templon
 */
public class EuropaEntity extends Entity implements Serializable {

    // Constants
    public static final String COMPONENTS_KEY = "components";
    public static final String OLD_ID_KEY = "id";
    public static final String COMPONENT_CLASS_KEY = "component-class";
    public static final String COMPONENT_DATA_KEY = "component-data";


    // Public Methods
    public void initializeComponents() {
        for(Component component : this.getComponents()) {
            if (component instanceof OwnedComponent) {
                ((OwnedComponent)component).setOwner(this);
            }
            if (component instanceof Initializable) {
                ((Initializable)component).initialize();
            }
        }
    }
    

    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(OLD_ID_KEY, this.getId());

        json.writeArrayStart(COMPONENTS_KEY);
        for (Component component : this.getComponents()) {
            if (component instanceof Serializable) {
                json.writeObjectStart();
                json.writeValue(COMPONENT_CLASS_KEY, component.getClass().getName());
                json.writeValue(COMPONENT_DATA_KEY, component, component.getClass());
                json.writeObjectEnd();
            }
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(OLD_ID_KEY)) {
            long oldID = jsonData.getLong(OLD_ID_KEY);
            EuropaGame.game.lastIdMapping.put(oldID, this);
        }

        if (jsonData.has(COMPONENTS_KEY)) {
            JsonValue componentsValue = jsonData.get(COMPONENTS_KEY);

            componentsValue.iterator().forEach((JsonValue value) -> {
                try {
                    if (value.has(COMPONENT_CLASS_KEY)) {
                        Class<?> type = Class.forName(value.getString(COMPONENT_CLASS_KEY));
                        if (Component.class.isAssignableFrom(type)) {
                            this.add((Component) json.fromJson(type, value.get(COMPONENT_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS)));
                        }
                    }
                }
                catch (ClassNotFoundException ex) {

                }
            });

            this.initializeComponents();
        }
    }

}
