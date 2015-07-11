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
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.stats.race.PlayerRaces
import com.jupiter.europa.entity.stats.race.Race

/**

 * @author Nathan Templon
 */
public class RaceComponent(race: Race? = null) : Component(), Serializable {

    // Fields
    public var race: Race? = race
        private set


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(RACE_KEY, this.race.toString())
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(RACE_KEY)) {
            val race = try {
                PlayerRaces.valueOf(jsonData.getString(RACE_KEY))
            } catch (ex: Exception) {
                PlayerRaces.Human
            }
            this.race = race
        }
    }

    companion object {
        private val RACE_KEY = "race"
    }

}
