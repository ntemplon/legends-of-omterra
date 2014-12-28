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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.EmergenceGame;
import com.emergence.entity.component.CollisionComponent;
import com.emergence.entity.component.PositionComponent;
import com.emergence.entity.component.RenderComponent;
import com.emergence.entity.component.SizeComponent;
import com.emergence.entity.component.MovementResourceComponent;
import com.emergence.entity.component.WalkComponent;
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
    
    
    // Fields
    private EmergenceEntity player1;
    
    private EmergenceEntity[] partyMembers;
    
    
    // Properties
    public final EmergenceEntity[] getPartyMembers() {
        return this.partyMembers;
    }
    
    
    // Initialization
    public Party() {
        this.player1 = new EmergenceEntity();
        
        // All the debug code
        TextureAtlas atlas = EmergenceGame.game.getAssetManager().get(new File(FileLocations.SPRITES_DIRECTORY,
                "CharacterSprites.atlas").getPath(), TextureAtlas.class);
        this.player1.add(new MovementResourceComponent(atlas, "champion"));
        this.player1.add(new PositionComponent(null, new Point(19, 25), 0));
        this.player1.add(new SizeComponent(new Size(1, 1)));
        this.player1.add(new CollisionComponent(Mappers.position.get(this.player1).getTilePosition(), Mappers.size.get(
                this.player1).getSize()));
        this.player1.add(new WalkComponent());
        this.player1.add(new RenderComponent(new Sprite(Mappers.moveTexture.get(this.player1).getFrontStandTexture())));
        
        this.partyMembers = new EmergenceEntity[] {this.player1};
    }

    
    // Serializable (Json) implementation
    @Override
    public void write(Json json) {
        json.writeArrayStart(PARTY_MEMBERS_KEY);
        for(EmergenceEntity entity : this.getPartyMembers()) {
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
