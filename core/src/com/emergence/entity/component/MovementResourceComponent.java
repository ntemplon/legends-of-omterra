/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emergence.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.EmergenceGame;
import com.emergence.entity.MovementSystem.MovementDirections;

/**
 *
 * @author Hortator
 */
public class MovementResourceComponent extends Component implements Serializable {
    
    // Constants
    public static final String SEPARATION_STRING = "-";
    public static final String ATLAS_PATH_KEY = "atlas-path";
    public static final String SPRITE_SET_NAME_KEY = "sprite-set";
    
    public static final String FRONT_STAND_TEXTURE_NAME = "stand-front";
    public static final String BACK_STAND_TEXTURE_NAME = "stand-back";
    public static final String LEFT_STAND_TEXTURE_NAME = "stand-left";
    public static final String RIGHT_STAND_TEXTURE_NAME = "stand-right";
    public static final String FRONT_WALK_1_TEXTURE_NAME = "walk-front-1";
    public static final String FRONT_WALK_2_TEXTURE_NAME = "walk-front-2";
    public static final String BACK_WALK_1_TEXTURE_NAME = "walk-back-1";
    public static final String BACK_WALK_2_TEXTURE_NAME = "walk-back-2";
    public static final String LEFT_WALK_TEXTURE_NAME = "walk-left";
    public static final String RIGHT_WALK_TEXTURE_NAME = "walk-right";
    public static final String ATTACK_DOWN_TEXTURE_NAME = "attack-down";
    public static final String ATTACK_UP_TEXTURE_NAME = "attack-up";
    public static final String ATTACK_RIGHT_TEXTURE_NAME = "attack-right";
    public static final String ATTACK_LEFT_TEXTURE_NAME = "attack-left";
    public static final String CAST_TEXTURE_NAME = "cast";
    public static final String DEAD_TEXTURE_NAME = "dead";
    
    
    // Fields
    private String atlasPath;
    private String spriteSetName;
    
    private TextureRegion frontStandTexture;
    private TextureRegion backStandTexture;
    private TextureRegion leftStandTexture;
    private TextureRegion rightStandTexture;
    private TextureRegion frontWalkTexture1;
    private TextureRegion frontWalkTexture2;
    private TextureRegion backWalkTexture1;
    private TextureRegion backWalkTexture2;
    private TextureRegion leftWalkTexture;
    private TextureRegion rightWalkTexture;
    private TextureRegion attackDownTexture;
    private TextureRegion attackUpTexture;
    private TextureRegion attackRightTexture;
    private TextureRegion attackLeftTexture;
    private TextureRegion castTexture;
    private TextureRegion deadTexture;
    
    private TextureRegion nextLeftWalkTexture;
    private TextureRegion nextRightWalkTexture;
    private TextureRegion nextBackWalkTexture;
    private TextureRegion nextFrontWalkTexture;
    
    
    // Properties
    /**
     * @return the frontStandTexture
     */
    public final TextureRegion getFrontStandTexture() {
        return frontStandTexture;
    }

    /**
     * @return the backStandTexture
     */
    public final TextureRegion getBackStandTexture() {
        return backStandTexture;
    }

    /**
     * @return the leftStandTexture
     */
    public final TextureRegion getLeftStandTexture() {
        return leftStandTexture;
    }

    /**
     * @return the rightStandTexture
     */
    public final TextureRegion getRightStandTexture() {
        return rightStandTexture;
    }

    /**
     * @return the frontWalkTexture1
     */
    public final TextureRegion getFrontWalkTexture1() {
        return frontWalkTexture1;
    }

    /**
     * @return the frontWalkTexture2
     */
    public final TextureRegion getFrontWalkTexture2() {
        return frontWalkTexture2;
    }

    /**
     * @return the backWalkTexture1
     */
    public final TextureRegion getBackWalkTexture1() {
        return backWalkTexture1;
    }

    /**
     * @return the backWalkTexture2
     */
    public final TextureRegion getBackWalkTexture2() {
        return backWalkTexture2;
    }

    /**
     * @return the leftWalkTexture
     */
    public final TextureRegion getLeftWalkTexture() {
        return leftWalkTexture;
    }

    /**
     * @return the rightWalkTexture
     */
    public final TextureRegion getRightWalkTexture() {
        return rightWalkTexture;
    }

    /**
     * @return the attackDownTexture
     */
    public final TextureRegion getAttackDownTexture() {
        return attackDownTexture;
    }

    /**
     * @return the attackUpTexture
     */
    public final TextureRegion getAttackUpTexture() {
        return attackUpTexture;
    }

    /**
     * @return the attackRightTexture
     */
    public final TextureRegion getAttackRightTexture() {
        return attackRightTexture;
    }

    /**
     * @return the attackLeftTexture
     */
    public final TextureRegion getAttackLeftTexture() {
        return attackLeftTexture;
    }

    /**
     * @return the castTexture
     */
    public final TextureRegion getCastTexture() {
        return castTexture;
    }

    /**
     * @return the deadTexture
     */
    public final TextureRegion getDeadTexture() {
        return deadTexture;
    }
    
    
    // Initialization
    public MovementResourceComponent() {
        
    }
    
    public MovementResourceComponent(String atlasPath, String spriteSetName) {
        this.atlasPath = atlasPath;
        this.spriteSetName = spriteSetName;
        
        this.fetchTextures();
    }
    
    
    // Public Methods
    public final TextureRegion standingTextureFor(MovementDirections direction) {
        switch (direction) {
            case LEFT:
                return this.getLeftStandTexture();
            case RIGHT:
                return this.getRightStandTexture();
            case UP:
                return this.getBackStandTexture();
            case DOWN:
                return this.getFrontStandTexture();
            default:
                return this.getFrontStandTexture();
        }
    }
    
    public final TextureRegion walkingTextureFont(MovementDirections direction) {
        switch (direction) {
            case LEFT:
                return this.nextLeftWalkTexture;
            case RIGHT:
                return this.nextRightWalkTexture;
            case UP:
                return this.nextBackWalkTexture;
            case DOWN:
                return this.nextFrontWalkTexture;
            default:
                return this.getFrontStandTexture();
        }
    }
    
    public final void incrementWalkingTexture(MovementDirections direction) {
        switch (direction) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case UP:
                if (this.nextBackWalkTexture.equals(this.getBackWalkTexture1())) {
                    this.nextBackWalkTexture = this.getBackWalkTexture2();
                }
                else {
                    this.nextBackWalkTexture = this.getBackWalkTexture1();
                }
            case DOWN:
                if (this.nextFrontWalkTexture.equals(this.getFrontWalkTexture1())) {
                    this.nextFrontWalkTexture = this.getFrontWalkTexture2();
                }
                else {
                    this.nextFrontWalkTexture = this.getFrontWalkTexture1();
                }
        }
    }

    
    // Serializable (Json)
    @Override
    public void write(Json json) {
        json.writeValue(ATLAS_PATH_KEY, this.atlasPath);
        json.writeValue(SPRITE_SET_NAME_KEY, this.spriteSetName);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.atlasPath = jsonData.getString(ATLAS_PATH_KEY);
        this.spriteSetName = jsonData.getString(SPRITE_SET_NAME_KEY);
        
        this.fetchTextures();
    }
    
    
    // Private Methods
    private void fetchTextures() {
        TextureAtlas atlas = EmergenceGame.game.getAssetManager().get(this.atlasPath);
        this.frontStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + FRONT_STAND_TEXTURE_NAME);
        this.backStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + BACK_STAND_TEXTURE_NAME);
        this.leftStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + LEFT_STAND_TEXTURE_NAME);
        this.rightStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + RIGHT_STAND_TEXTURE_NAME);
        
        this.frontWalkTexture1 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + FRONT_WALK_1_TEXTURE_NAME);
        this.frontWalkTexture2 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + FRONT_WALK_2_TEXTURE_NAME);
        this.backWalkTexture1 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + BACK_WALK_1_TEXTURE_NAME);
        this.backWalkTexture2 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + BACK_WALK_2_TEXTURE_NAME);
        this.leftWalkTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + LEFT_WALK_TEXTURE_NAME);
        this.rightWalkTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + RIGHT_WALK_TEXTURE_NAME);
        
        this.attackUpTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_UP_TEXTURE_NAME);
        this.attackDownTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_DOWN_TEXTURE_NAME);
        this.attackLeftTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_LEFT_TEXTURE_NAME);
        this.attackRightTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_RIGHT_TEXTURE_NAME);
        
        this.castTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + CAST_TEXTURE_NAME);
        this.deadTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + DEAD_TEXTURE_NAME);
        
        this.nextBackWalkTexture = this.getBackWalkTexture1();
        this.nextFrontWalkTexture = this.getFrontWalkTexture1();
        this.nextLeftWalkTexture = this.getLeftWalkTexture();
        this.nextRightWalkTexture = this.getRightWalkTexture();
    }
    
}
