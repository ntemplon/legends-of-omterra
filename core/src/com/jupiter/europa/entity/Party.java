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

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.component.*;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.SkillSet;
import com.jupiter.europa.entity.stats.SkillSet.Skills;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.geometry.Size;
import com.jupiter.europa.io.FileLocations;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 * @author Nathan Templon
 */
public class Party implements Serializable {

    // Constants
    public static final String PARTY_MEMBERS_KEY = "party-members";
    public static final String ACTIVE_PARTY_MEMBERS_KEY = "active-party-members";
    
    
    // Static Methods
    public static EuropaEntity createPlayer(String name, Class<? extends CharacterClass> charClass, Race race, AttributeSet attributes) {
        // NOTE: Order of component creation is important!
        EuropaEntity entity = new EuropaEntity();

        entity.add(new NameComponent(name));
        entity.add(new EffectsComponent());
        entity.add(new RaceComponent(race));

        CharacterClassComponent classComponent = new CharacterClassComponent(charClass, entity);
        String textureSetName = race.getTextureString() + "-" + classComponent.getCharacterClass().getTextureSetName();

        entity.add(new MovementResourceComponent(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(), textureSetName));
        entity.add(new PositionComponent(null, new Point(19, 25), 0));
        entity.add(new SizeComponent(new Size(1, 1)));
        entity.add(new CollisionComponent(Mappers.position.get(entity).getTilePosition(), Mappers.size.get(
                entity).getSize()));
        entity.add(new WalkComponent());
        entity.add(new RenderComponent(new Sprite(Mappers.moveTexture.get(entity).getFrontStandTexture())));
        entity.add(new AttributesComponent(attributes));
        entity.add(new ResourceComponent());
        
        // Skills
        Set<Skills> classSkills = new HashSet<>();
        classSkills.addAll(classComponent.getCharacterClass().getClassSkills());
        classSkills.addAll(race.getClassSkills());
        List<Skills> sorted = new ArrayList<>(classSkills);
        Collections.sort(sorted);
        entity.add(new SkillsComponent(new SkillSet(), sorted));
        
        entity.add(classComponent);

        entity.initializeComponents();

        return entity;
    }


    // Fields
    private EuropaEntity player1;

    private final List<EuropaEntity> activePartyMembers = new ArrayList<>();
    private Map<String, EuropaEntity> partyMembers = new HashMap<>();


    // Properties
    public final EuropaEntity[] getActivePartyMembers() {
        return this.activePartyMembers.toArray(new EuropaEntity[this.activePartyMembers.size()]);
    }


    // Initialization
    public Party() {

    }
    
    
    // Public Methods
    public final void addPlayer(EuropaEntity entity) {
        this.partyMembers.put(Mappers.name.get(entity).getName(), entity);
    }

    public final void selectPlayer(EuropaEntity entity) {
        this.activePartyMembers.add(entity);
    }
    

    // Serializable (Json) implementation
    @Override
    public void write(Json json) {
        json.writeValue(PARTY_MEMBERS_KEY, this.partyMembers, HashMap.class);

        json.writeArrayStart(ACTIVE_PARTY_MEMBERS_KEY);
        for (EuropaEntity entity : this.getActivePartyMembers()) {
            json.writeValue(Mappers.name.get(entity).getName());
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(PARTY_MEMBERS_KEY)) {
            this.partyMembers = json.fromJson(HashMap.class, jsonData.get(PARTY_MEMBERS_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS));
        }

        JsonValue members = jsonData.get(ACTIVE_PARTY_MEMBERS_KEY);

        for (int i = 0; i < members.size; i++) {
            this.activePartyMembers.add(this.partyMembers.get(members.getString(i)));
        }
    }

}
