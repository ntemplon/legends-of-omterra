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

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.screen.LevelScreen
import com.jupiter.europa.screen.OverlayableScreen
import com.jupiter.europa.world.Level
import java.awt.Point

/**
 * Created by nathan on 7/12/15.
 */
public class TileSelectionOverlay(public var approval: (Level, Point) -> Boolean = { level, point -> true }) : Scene2DOverlay(blocking = false) {

    // Properties
    private val actor = SquareActor()

    private var levelScreen: LevelScreen? = null

    public var approvedColor: Color = Color(Color.WHITE)
    public var disapprovedColor: Color = Color(Color.RED)


    // Public Methods
    override fun added(screen: OverlayableScreen) {
        super<Scene2DOverlay>.added(screen)
        if (screen is LevelScreen) {
            this.levelScreen = screen
            this.actor.setWidth(screen.level.tileWidth * 2.0f)
            this.actor.setHeight(screen.level.tileHeight * 2.0f)
            this.actor.setScale(EuropaGame.SCALE)
            this.stage.addActor(this.actor)
        }
    }

    override fun removed() {
        super<Scene2DOverlay>.removed()
        this.levelScreen = null
    }

    override fun render() {
        val screen = this.levelScreen
        if (screen != null) {
            this.updateBox(screen)
            super<Scene2DOverlay>.render()
        }
    }

    override fun resize(width: Int, height: Int) {
        val cam = this.stage?.getCamera()
        if (cam != null) {
            cam.viewportWidth = width / EuropaGame.SCALE
            cam.viewportHeight = height / EuropaGame.SCALE
            cam.update()
        }
    }


    // Private Methods
    private fun updateBox(screen: LevelScreen) {
        val tile = screen.tileUnder(screen.mousePosition)
        if (this.approval(screen.level, tile)) {
            this.actor.renderColor = this.approvedColor
        } else {
            this.actor.renderColor = this.disapprovedColor
        }

        val pixelPoint = screen.tileToScreen(tile.x, tile.y)
        this.actor.setPosition(pixelPoint.x * 2.0f, pixelPoint.y * 2.0f)
    }


    // Inner Classes
    protected inner class SquareActor(color: Color = Color(Color.WHITE)) : Actor() {

        private val shape = ShapeRenderer()

        public var renderColor: Color = Color(Color.WHITE)

        init {
            this.setColor(color)
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            shape.begin(ShapeRenderer.ShapeType.Line)
            shape.setColor(this.renderColor)
            shape.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight())
            shape.end()
        }
    }

}