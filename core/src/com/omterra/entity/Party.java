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
package com.omterra.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.omterra.EmergenceGame;
import com.omterra.entity.component.PositionComponent;
import com.omterra.entity.component.RenderComponent;
import com.omterra.entity.component.SizeComponent;
import com.omterra.geometry.Size;
import com.omterra.io.FileLocations;
import java.awt.Point;
import java.io.File;

/**
 *
 * @author Nathan Templon
 */
public class Party {
    
    // Fields
    private Entity player1;
    private final Sprite sprite;
    
    private final Entity[] partyMembers;
    
    
    // Properties
    public final Entity[] getPartyMembers() {
        return this.partyMembers;
    }
    
    
    // Initialization
    public Party() {
        this.player1 = new Entity();
        
        // All the debug code
        TextureAtlas atlas = EmergenceGame.game.getAssetManager().get(new File(FileLocations.SPRITES_DIRECTORY,
                "CharacterSprites.atlas").getPath(), TextureAtlas.class);
        this.sprite = new Sprite(atlas.findRegion("champion-stand-front"));
        this.player1.add(new PositionComponent(null, new Point(19, 24), 0));
        this.player1.add(new SizeComponent(new Size(1, 1)));
        this.player1.add(new RenderComponent(this.sprite));
        
        this.partyMembers = new Entity[] {this.player1};
    }
    
}
