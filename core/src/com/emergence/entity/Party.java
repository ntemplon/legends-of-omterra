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
import com.emergence.entity.component.AttributesComponent;
import com.emergence.entity.component.CollisionComponent;
import com.emergence.entity.component.PositionComponent;
import com.emergence.entity.component.RenderComponent;
import com.emergence.entity.component.SizeComponent;
import com.emergence.entity.component.MovementResourceComponent;
import com.emergence.entity.component.RaceComponent;
import com.emergence.entity.component.WalkComponent;
import com.emergence.entity.stats.CharacterClass;
import com.emergence.entity.stats.Race;
import com.emergence.entity.stats.Race.Races;
import com.emergence.geometry.Size;
import com.emergence.io.FileLocations;
import java.awt.Point;
import java.io.File;

/**
 *
 * @author Nathan Templon
 */
public class Party implements Serializable {

    // Constants
    public static final String PARTY_MEMBERS_KEY = "party-members";


    // Static Methods
    public static final EmergenceEntity createPlayer(CharacterClass charClass, Race race) {
        EmergenceEntity entity = new EmergenceEntity();
        entity.add(new MovementResourceComponent(new File(FileLocations.SPRITES_DIRECTORY,
                "CharacterSprites.atlas").getPath(), "human-champion"));
        entity.add(new PositionComponent(null, new Point(19, 25), 0));
        entity.add(new SizeComponent(new Size(1, 1)));
        entity.add(new CollisionComponent(Mappers.position.get(entity).getTilePosition(), Mappers.size.get(
                entity).getSize()));
        entity.add(new WalkComponent());
        entity.add(new RenderComponent(new Sprite(Mappers.moveTexture.get(entity).getFrontStandTexture())));
        entity.add(new AttributesComponent());
        entity.add(new RaceComponent(race));
        return entity;
    }


    // Fields
    private EmergenceEntity player1;

    private EmergenceEntity[] partyMembers;


    // Properties
    public final EmergenceEntity[] getPartyMembers() {
        return this.partyMembers;
    }


    // Initialization
    public Party() {
        this.player1 = createPlayer(null, Races.HUMAN);

        this.partyMembers = new EmergenceEntity[]{this.player1};
    }


    // Serializable (Json) implementation
    @Override
    public void write(Json json) {
        json.writeArrayStart(PARTY_MEMBERS_KEY);
        for (EmergenceEntity entity : this.getPartyMembers()) {
            json.writeValue(entity, entity.getClass());
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue members = jsonData.get(PARTY_MEMBERS_KEY);
        this.partyMembers = new EmergenceEntity[members.size];

        for (int i = 0; i < members.size; i++) {
            this.partyMembers[i] = json.fromJson(EmergenceEntity.class, members.get(i).toString());
        }
    }

}
