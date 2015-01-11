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
package com.jupiter.europa.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.stats.characterclass.Champion;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.util.Initializable;

/**
 *
 * @author Nathan Templon
 */
public class CharacterClassComponent extends Component implements Serializable, Initializable {

    // Constants
    private static final String CHARACTER_CLASS_TYPE_KEY = "class-type";
    private static final String CHARACTER_CLASS_INSTANCE_KEY = "class-instance";


    // Fields
    private CharacterClass characterClass;


    // Properties
    public CharacterClass getCharacterClass() {
        return this.characterClass;
    }


    // Initialization
    public CharacterClassComponent() {

    }

    public CharacterClassComponent(Class<? extends CharacterClass> charClass, Entity owner) {
        try {
            this.characterClass = charClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex) {
            this.characterClass = new Champion();
        }
        this.characterClass.create();
        this.characterClass.setOwner(owner);
    }
    
    @Override
    public void initialize() {
        if (this.characterClass != null) {
            this.characterClass.initialize();
        }
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(CHARACTER_CLASS_TYPE_KEY, this.getCharacterClass().getClass().getName());
        json.writeValue(CHARACTER_CLASS_INSTANCE_KEY, this.getCharacterClass(), this.getCharacterClass().getClass());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(CHARACTER_CLASS_TYPE_KEY)) {
            String className = jsonData.getString(CHARACTER_CLASS_TYPE_KEY);

            if (jsonData.has(CHARACTER_CLASS_INSTANCE_KEY)) {
                try {
                    Class<?> classType = Class.forName(className);
                    if (CharacterClass.class.isAssignableFrom(classType)) {
                        this.characterClass = (CharacterClass) json.fromJson(classType, jsonData.get(
                                CHARACTER_CLASS_INSTANCE_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS));
                    }
                }
                catch (ClassNotFoundException ex) {

                }
            }
        }

        // If a value was not assigned for whatver reason
        if (this.characterClass == null) {
            this.characterClass = new Champion();
        }
    }

}
