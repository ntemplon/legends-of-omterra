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
package com.emergence.save;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.EmergenceGame;
import com.emergence.entity.Party;
import com.emergence.world.World;

/**
 * A class representing a save game in the Legends of Omterra game
 * @author Nathan Templon
 */
public class SaveGame implements Serializable {
    
    // Constants
    public static final String NAME_KEY = "save-name";
    public static final String WORLD_KEY = "world-name";
    public static final String PARTY_KEY = "party";
    
    
    // Fields
    private String name;
    private World world;
    private Party party;
    
    
    // Properties
    public String getName() {
        return this.name;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public Party getParty() {
        return this.party;
    }
    
    
    // Initialization
    public SaveGame() {
        
    }
    
    public SaveGame(String name, World world) {
        this.name = name;
        this.world = world;
        this.party = new Party();
    }
    
    
    // Serializable (Json) implementation

    @Override
    public void write(Json json) {
        json.writeValue(NAME_KEY, this.name);
        json.writeValue(WORLD_KEY, this.world.getName());
        json.writeValue(PARTY_KEY, this.party, Party.class);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.name = jsonData.getString(NAME_KEY);
        this.world = EmergenceGame.game.getWorld(jsonData.getString(WORLD_KEY));
        this.party = json.fromJson(Party.class, jsonData.get(PARTY_KEY).toString());
    }
    
}
