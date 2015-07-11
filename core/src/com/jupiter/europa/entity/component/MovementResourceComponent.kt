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
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.MovementSystem
import com.jupiter.europa.entity.MovementSystem.MovementDirections

/**

 * @author Hortator
 */
public class MovementResourceComponent : Component, Serializable {


    // Fields
    private var atlasPath: String? = null
    private var spriteSetName: String? = null

    public var frontStandTexture: TextureRegion = TextureRegion()
        private set
    public var backStandTexture: TextureRegion = TextureRegion()
        private set
    public var leftStandTexture: TextureRegion = TextureRegion()
        private set
    public var rightStandTexture: TextureRegion = TextureRegion()
        private set
    public var frontWalkTexture1: TextureRegion = TextureRegion()
        private set
    public var frontWalkTexture2: TextureRegion = TextureRegion()
        private set
    public var backWalkTexture1: TextureRegion = TextureRegion()
        private set
    public var backWalkTexture2: TextureRegion = TextureRegion()
        private set
    public var leftWalkTexture: TextureRegion = TextureRegion()
        private set
    public var rightWalkTexture: TextureRegion = TextureRegion()
        private set
    public var attackDownTexture: TextureRegion = TextureRegion()
        private set
    public var attackUpTexture: TextureRegion = TextureRegion()
        private set
    public var attackRightTexture: TextureRegion = TextureRegion()
        private set
    public var attackLeftTexture: TextureRegion = TextureRegion()
        private set
    public var castTexture: TextureRegion = TextureRegion()
        private set
    public var deadTexture: TextureRegion = TextureRegion()
        private set

    private var nextLeftWalkTexture: TextureRegion = TextureRegion()
    private var nextRightWalkTexture: TextureRegion = TextureRegion()
    private var nextBackWalkTexture: TextureRegion = TextureRegion()
    private var nextFrontWalkTexture: TextureRegion = TextureRegion()


    // Initialization
    public constructor() {
    }

    public constructor(atlasPath: String, spriteSetName: String) {
        this.atlasPath = atlasPath
        this.spriteSetName = spriteSetName

        this.fetchTextures()
    }


    // Public Methods
    public fun standingTextureFor(direction: MovementDirections): TextureRegion {
        when (direction) {
            MovementSystem.MovementDirections.LEFT -> return this.leftStandTexture
            MovementSystem.MovementDirections.RIGHT -> return this.rightStandTexture
            MovementSystem.MovementDirections.UP -> return this.backStandTexture
            MovementSystem.MovementDirections.DOWN -> return this.frontStandTexture
            else -> return this.frontStandTexture
        }
    }

    public fun walkingTextureFont(direction: MovementDirections): TextureRegion {
        when (direction) {
            MovementSystem.MovementDirections.LEFT -> return this.nextLeftWalkTexture
            MovementSystem.MovementDirections.RIGHT -> return this.nextRightWalkTexture
            MovementSystem.MovementDirections.UP -> return this.nextBackWalkTexture
            MovementSystem.MovementDirections.DOWN -> return this.nextFrontWalkTexture
            else -> return this.frontStandTexture
        }
    }

    public fun incrementWalkingTexture(direction: MovementDirections) {
        when (direction) {
            MovementSystem.MovementDirections.LEFT -> {
            }
            MovementSystem.MovementDirections.RIGHT -> {
            }
            MovementSystem.MovementDirections.UP -> {
                if (this.nextBackWalkTexture == this.backWalkTexture1) {
                    this.nextBackWalkTexture = this.backWalkTexture2
                } else {
                    this.nextBackWalkTexture = this.backWalkTexture1
                }
                if (this.nextFrontWalkTexture == this.frontWalkTexture1) {
                    this.nextFrontWalkTexture = this.frontWalkTexture2
                } else {
                    this.nextFrontWalkTexture = this.frontWalkTexture1
                }
            }
            MovementSystem.MovementDirections.DOWN -> if (this.nextFrontWalkTexture == this.frontWalkTexture1) {
                this.nextFrontWalkTexture = this.frontWalkTexture2
            } else {
                this.nextFrontWalkTexture = this.frontWalkTexture1
            }
        }
    }


    // Serializable (Json)
    override fun write(json: Json) {
        json.writeValue(ATLAS_PATH_KEY, this.atlasPath)
        json.writeValue(SPRITE_SET_NAME_KEY, this.spriteSetName)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        this.atlasPath = jsonData.getString(ATLAS_PATH_KEY)
        this.spriteSetName = jsonData.getString(SPRITE_SET_NAME_KEY)

        this.fetchTextures()
    }


    // Private Methods
    private fun fetchTextures() {
        val atlas = EuropaGame.game.assetManager!!.get<TextureAtlas>(this.atlasPath ?: "")
        this.frontStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + FRONT_STAND_TEXTURE_NAME)
        this.backStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + BACK_STAND_TEXTURE_NAME)
        this.leftStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + LEFT_STAND_TEXTURE_NAME)
        this.rightStandTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + RIGHT_STAND_TEXTURE_NAME)

        this.frontWalkTexture1 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + FRONT_WALK_1_TEXTURE_NAME)
        this.frontWalkTexture2 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + FRONT_WALK_2_TEXTURE_NAME)
        this.backWalkTexture1 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + BACK_WALK_1_TEXTURE_NAME)
        this.backWalkTexture2 = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + BACK_WALK_2_TEXTURE_NAME)
        this.leftWalkTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + LEFT_WALK_TEXTURE_NAME)
        this.rightWalkTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + RIGHT_WALK_TEXTURE_NAME)

        this.attackUpTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_UP_TEXTURE_NAME)
        this.attackDownTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_DOWN_TEXTURE_NAME)
        this.attackLeftTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_LEFT_TEXTURE_NAME)
        this.attackRightTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + ATTACK_RIGHT_TEXTURE_NAME)

        this.castTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + CAST_TEXTURE_NAME)
        this.deadTexture = atlas.findRegion(this.spriteSetName + SEPARATION_STRING + DEAD_TEXTURE_NAME)

        this.nextBackWalkTexture = this.backWalkTexture1
        this.nextFrontWalkTexture = this.frontWalkTexture1
        this.nextLeftWalkTexture = this.leftWalkTexture
        this.nextRightWalkTexture = this.rightWalkTexture
    }

    companion object {

        // Constants
        public val SEPARATION_STRING: String = "-"
        public val ATLAS_PATH_KEY: String = "atlas-path"
        public val SPRITE_SET_NAME_KEY: String = "sprite-set"

        public val FRONT_STAND_TEXTURE_NAME: String = "stand-front"
        public val BACK_STAND_TEXTURE_NAME: String = "stand-back"
        public val LEFT_STAND_TEXTURE_NAME: String = "stand-left"
        public val RIGHT_STAND_TEXTURE_NAME: String = "stand-right"
        public val FRONT_WALK_1_TEXTURE_NAME: String = "walk-front-1"
        public val FRONT_WALK_2_TEXTURE_NAME: String = "walk-front-2"
        public val BACK_WALK_1_TEXTURE_NAME: String = "walk-back-1"
        public val BACK_WALK_2_TEXTURE_NAME: String = "walk-back-2"
        public val LEFT_WALK_TEXTURE_NAME: String = "walk-left"
        public val RIGHT_WALK_TEXTURE_NAME: String = "walk-right"
        public val ATTACK_DOWN_TEXTURE_NAME: String = "attack-down"
        public val ATTACK_UP_TEXTURE_NAME: String = "attack-up"
        public val ATTACK_RIGHT_TEXTURE_NAME: String = "attack-right"
        public val ATTACK_LEFT_TEXTURE_NAME: String = "attack-left"
        public val CAST_TEXTURE_NAME: String = "cast"
        public val DEAD_TEXTURE_NAME: String = "dead"
    }

}