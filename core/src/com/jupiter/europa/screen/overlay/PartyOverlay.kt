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

package com.jupiter.europa.screen.overlay

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Disposable
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.EntityEventArgs
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.Party
import com.jupiter.europa.entity.component.MovementResourceComponent
import com.jupiter.europa.entity.component.ResourceComponent
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.scene2d.ui.ResourceBar
import com.jupiter.europa.screen.OverlayableScreen
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener

/**
 * Created by nathan on 5/18/15.
 */
public class PartyOverlay(private val party: Party) : Scene2DOverlay(false), Disposable {


    // Fields
    private val entityClicked = Event<EntityEventArgs>()
    private val table = Table()
    private val anchor = Table()


    init {
        this.initComponents()
    }


    // Public Methods
    public fun addEntityClickListener(listener: Listener<EntityEventArgs>): Boolean {
        return this.entityClicked.addListener(listener)
    }

    public fun removeEntityClickListener(listener: Listener<EntityEventArgs>): Boolean {
        return this.entityClicked.removeListener(listener)
    }

    override fun added(screen: OverlayableScreen) {
        super<Scene2DOverlay>.added(screen)
        this.anchor.setFillParent(true)
        this.anchor.add(this.table).expand().top().right().pad(10f)
        this.stage?.addActor(anchor)

        screen.addTintChangedListener(Listener { args -> this.tint = args.newValue })
    }

    override fun dispose() {
        for (act in this.table.getChildren()) {
            if (act is Disposable) {
                act.dispose()
            }
        }
    }


    // Private Methods
    private fun initComponents() {
        this.party.getActivePartyMembers().forEach { entity ->
            val actor = EntityActor(entity)
            actor.addListener(object : ClickListener() {
                override fun clicked(e: InputEvent, x: Float, y: Float) {
                    this@PartyOverlay.onEntityClick(entity)
                }
            })
            this.table.add(actor).center()
        }
    }

    private fun onEntityClick(entity: Entity) {
        this.entityClicked.dispatch(EntityEventArgs(entity))
    }


    // Inner Classes
    public class EntityActor(private val entity: Entity) : Table(), Disposable {


        // Fields
        private val entityPreview = Image()


        init {
            this.initComponents()
        }


        // Public Methods
        override fun dispose() {
            for (act in this.getChildren()) {
                if (act is Disposable) {
                    act.dispose()
                }
            }
        }


        // Private Methods
        private fun initComponents() {
            val sprite = getSprite(this.entity)
            this.entityPreview.setDrawable(SpriteDrawable(sprite))

            val width = sprite.getWidth() * PARTY_MEMBER_SCALE

            this.add(this.entityPreview).center().width(width).height(sprite.getHeight() * PARTY_MEMBER_SCALE)
            this.row()

            for (resource in ResourceComponent.Resources.values()) {
                this.add(ResourceBar(this.entity, resource)).center().expandX().fillX().height(BAR_HEIGHT.toFloat()).padTop(BAR_PADDING.toFloat())
                this.row()
            }
        }

        companion object {

            // Constants
            private val BAR_HEIGHT = 3
            private val BAR_PADDING = 1


            // Static Methods
            private fun getSprite(entity: Entity): Sprite {
                if (Families.classed.matches(entity) && Families.raced.matches(entity)) {
                    val characterClass = Mappers.characterClass.get(entity).characterClass
                    val race = Mappers.race.get(entity).race
                    val textureClassString = characterClass.textureSetName
                    val texture = (race?.textureString ?: "") + "-" + textureClassString + "-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME
                    return Sprite(EuropaGame.game.assetManager!!.get(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(), javaClass<TextureAtlas>()).findRegion(texture))
                }
                return Sprite()
            }
        }
    }

    companion object {

        // Constants
        public val PARTY_MEMBER_SCALE: Float = 3.0f
    }
}
