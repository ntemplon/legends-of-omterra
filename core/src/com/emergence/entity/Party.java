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
package com.emergence.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.entity.trait.feat.Feat;
import com.emergence.entity.trait.feat.FeatPool;
import com.emergence.entity.component.AttributesComponent;
import com.emergence.entity.component.CharacterClassComponent;
import com.emergence.entity.component.CollisionComponent;
import com.emergence.entity.component.EffectsComponent;
import com.emergence.entity.component.PositionComponent;
import com.emergence.entity.component.RenderComponent;
import com.emergence.entity.component.SizeComponent;
import com.emergence.entity.component.MovementResourceComponent;
import com.emergence.entity.component.NameComponent;
import com.emergence.entity.component.RaceComponent;
import com.emergence.entity.component.WalkComponent;
import com.emergence.entity.stats.Attributes;
import com.emergence.entity.stats.characterclass.Champion;
import com.emergence.entity.stats.characterclass.CharacterClass;
import com.emergence.entity.stats.race.Race;
import com.emergence.entity.stats.race.Race.Races;
import com.emergence.geometry.Size;
import com.emergence.io.FileLocations;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public class Party implements Serializable {

    // Constants
    public static final String PARTY_MEMBERS_KEY = "party-members";
    public static final String ACTIVE_PARTY_MEMBERS_KEY = "active-party-members";


    // Static Methods
    public final EmergenceEntity createPlayer(String name, Class<? extends CharacterClass> charClass, Race race, Attributes attributes) {
        // NOTE: Order of component creation is important!
        EmergenceEntity entity = new EmergenceEntity();

        entity.add(new NameComponent(name));
        entity.add(new EffectsComponent());
        entity.add(new RaceComponent(race));

        CharacterClassComponent classComponent = new CharacterClassComponent(charClass, entity);
        String textureSetName = race.getTextureString() + "-" + classComponent.getCharacterClass().getTextureSetName();

        entity.add(new MovementResourceComponent(new File(FileLocations.SPRITES_DIRECTORY,
                "CharacterSprites.atlas").getPath(), textureSetName));
        entity.add(new PositionComponent(null, new Point(19, 25), 0));
        entity.add(new SizeComponent(new Size(1, 1)));
        entity.add(new CollisionComponent(Mappers.position.get(entity).getTilePosition(), Mappers.size.get(
                entity).getSize()));
        entity.add(new WalkComponent());
        entity.add(new RenderComponent(new Sprite(Mappers.moveTexture.get(entity).getFrontStandTexture())));
        entity.add(new AttributesComponent(attributes));
        entity.add(classComponent);

        entity.initializeComponents();

        this.partyMembers.put(name, entity);

        return entity;
    }


    // Fields
    private EmergenceEntity player1;

    private List<EmergenceEntity> activePartyMembers = new ArrayList<>();
    private Map<String, EmergenceEntity> partyMembers = new HashMap<>();


    // Properties
    public final EmergenceEntity[] getActivePartyMembers() {
        return this.activePartyMembers.toArray(new EmergenceEntity[this.activePartyMembers.size()]);
    }


    // Initialization
    public Party() {
        this(false);
    }

    public Party(boolean createNew) {
        if (createNew) {
            this.player1 = this.createPlayer("Tharivol", Champion.class, Races.HUMAN, new Attributes());
            this.activePartyMembers.add(this.player1);

            // DEBUG - Assign Default Feats
            CharacterClass charClass = Mappers.characterClass.get(this.player1).getCharacterClass();
            FeatPool feats = charClass.getFeatPool();

            feats.setAutoQualify(true);
            feats.getSources().stream().filter((Feat source) -> {
                return feats.getNumberOfSelections() < feats.getCapacity();
            }).forEach(
                    (Feat source) -> {
                        feats.select(source);
                    });
        }
    }


    // Serializable (Json) implementation
    @Override
    public void write(Json json) {
        json.writeValue(PARTY_MEMBERS_KEY, this.partyMembers, HashMap.class);

        json.writeArrayStart(ACTIVE_PARTY_MEMBERS_KEY);
        for (EmergenceEntity entity : this.getActivePartyMembers()) {
            json.writeValue(Mappers.name.get(entity).getName());
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(PARTY_MEMBERS_KEY)) {
            this.partyMembers = json.fromJson(HashMap.class, jsonData.get(PARTY_MEMBERS_KEY).toString());
        }

        JsonValue members = jsonData.get(ACTIVE_PARTY_MEMBERS_KEY);

        for (int i = 0; i < members.size; i++) {
            this.activePartyMembers.add(this.partyMembers.get(members.getString(i)));
        }
    }

}
