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

package com.jupiter.europa.save

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.Party
import com.jupiter.europa.world.Level
import com.jupiter.europa.world.World

/**
 * A class representing a save game in the Legends of Omterra game
 * @author Nathan Templon
 */
public class SaveGame(name: String? = null, world: World? = null, party: Party? = null) : Serializable {


    // Fields
    public var name: String? = null
        private set
    public var world: World? = null
        private set
    public var party: Party? = null
        private set
    public var level: Level? = null
        private set


    // Initialization
    init {
        this.name = name
        this.world = world
        this.party = party
        this.level = this.world?.getStartingLevel()
    }


    // Serializable (Json) implementation
    override fun write(json: Json) {
        json.writeValue(NAME_KEY, this.name, javaClass<String>())
        json.writeValue(WORLD_KEY, this.world?.name ?: "", javaClass<String>())
        json.writeValue(LEVEL_KEY, this.level?.name ?: "", javaClass<String>())
        json.writeValue(PARTY_KEY, this.party, javaClass<Party>())
    }

    override fun read(json: Json, jsonData: JsonValue) {
        this.name = jsonData.getString(NAME_KEY)
        this.world = EuropaGame.game.getWorld(jsonData.getString(WORLD_KEY))
        this.party = json.fromJson(javaClass<Party>(), jsonData.get(PARTY_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS))
        this.level = this.world?.getLevel(jsonData.getString(LEVEL_KEY))
    }

    companion object {
        // Constants
        public val SAVE_EXTENSION: String = "esg"

        public val NAME_KEY: String = "save-name"
        public val WORLD_KEY: String = "world-name"
        public val PARTY_KEY: String = "party"
        public val LEVEL_KEY: String = "current-level"
    }

}
