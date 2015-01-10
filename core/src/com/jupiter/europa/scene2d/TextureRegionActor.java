/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 * @author Nathan Templon
 */
public class TextureRegionActor extends Actor {

    // Fields
    private TextureRegion region;


    // Initialization
    public TextureRegionActor() {
        this.region = new TextureRegion();
    }
    
    public TextureRegionActor(TextureRegion region) {
        this.region = region;
    }


    // Public Methods
    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        System.out.println("Drawing");
        batch.draw(this.region, this.getX(), this.getY());
    }

}
