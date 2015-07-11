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

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.screen.MainMenuScreen
import com.jupiter.europa.screen.OverlayableScreen

/**

 * @author Nathan Templon
 */
public class PauseMenu : Scene2DOverlay(true) {


    // Fields
    public var exitKey: Int = Input.Keys.ESCAPE

    private var buttonTable: Table? = null
    private var resumeButton: TextButton? = null
    private var returnToMenuButton: TextButton? = null
    private var exitButton: TextButton? = null

    override fun getBackgroundTint(): Color {
        return BACKGROUND_TINT
    }


    // Public Methods
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == this.exitKey) {
            this.screen?.removeOverlay(this)
            return true
        }
        return super.keyDown(keycode)
    }

    override fun added(screen: OverlayableScreen) {
        super.added(screen)
        this.initializeComponents()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        this.buttonTable!!.setSize(width.toFloat(), height.toFloat())
        this.buttonTable!!.invalidate()
    }


    // Private Methods
    private fun initializeComponents() {
        this.buttonTable = Table()
        this.buttonTable!!.setFillParent(true)
        this.buttonTable!!.center()

        this.resumeButton = TextButton("Resume", getPauseMenuSkin())
        this.resumeButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event!!.getButton() == Input.Buttons.LEFT && !this@PauseMenu.resumeButton!!.isDisabled()) {
                    this@PauseMenu.resumeGame()
                }
            }
        })

        this.returnToMenuButton = TextButton("Return to Menu", getPauseMenuSkin())
        this.returnToMenuButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event!!.getButton() == Input.Buttons.LEFT && !this@PauseMenu.returnToMenuButton!!.isDisabled()) {
                    this@PauseMenu.returnToMenu()
                }
            }
        })

        this.exitButton = TextButton("Exit", getPauseMenuSkin())
        this.exitButton!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event!!.getButton() == Input.Buttons.LEFT && !this@PauseMenu.exitButton!!.isDisabled()) {
                    Gdx.app.exit()
                }
            }
        })

        // Table configuration
        this.buttonTable!!.add<TextButton>(this.resumeButton)
        this.buttonTable!!.row()
        this.buttonTable!!.add<TextButton>(this.returnToMenuButton)
        this.buttonTable!!.row()
        this.buttonTable!!.add<TextButton>(this.exitButton)
        this.buttonTable!!.row()

        this.stage?.addActor(this.buttonTable)
    }

    private fun resumeGame() {
        this.screen?.removeOverlay(this)
    }

    private fun returnToMenu() {
        this@PauseMenu.resumeGame()
        EuropaGame.game.setState(EuropaGame.GameStates.MAIN_MENU)
    }

    companion object {

        // Constants
        private val TRANSPARENT = Color(1f, 1f, 1f, 0f)
        private val BACKGROUND_TINT = Color(Color.LIGHT_GRAY)

        private var _pauseMenuSkin: Skin? = null


        // Static Methods
        public fun getPauseMenuSkin(): Skin {
            if (_pauseMenuSkin == null) {
                buildPauseMenuSkin()
            }
            return _pauseMenuSkin!!
        }

        private fun buildPauseMenuSkin() {
            val skin = Skin()

            skin.add("button-font", EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(MainMenuScreen.BUTTON_FONT).toString()))
            skin.add("title-font", EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(MainMenuScreen.TITLE_FONT).toString()))

            // Set the background texture
            val pixmap = Pixmap(1, 1.toInt(), Pixmap.Format.RGB888)
            pixmap.setColor(Color.WHITE)
            pixmap.fill()
            skin.add("background", Texture(pixmap))

            // Create a Label style for the title
            val titleStyle = Label.LabelStyle()
            titleStyle.background = skin.newDrawable("background", TRANSPARENT)
            titleStyle.font = skin.getFont("title-font")
            titleStyle.fontColor = Color.BLACK
            skin.add("default", titleStyle)

            //Create a button style
            val textButtonStyle = TextButton.TextButtonStyle()
            textButtonStyle.up = skin.newDrawable("background", TRANSPARENT)
            textButtonStyle.down = skin.newDrawable("background", TRANSPARENT)
            textButtonStyle.checked = skin.newDrawable("background", TRANSPARENT)
            textButtonStyle.over = skin.newDrawable("background", TRANSPARENT)
            textButtonStyle.disabled = skin.newDrawable("background", TRANSPARENT)
            textButtonStyle.font = skin.getFont("button-font")
            textButtonStyle.fontColor = Color.TEAL
            textButtonStyle.overFontColor = Color.YELLOW
            textButtonStyle.disabledFontColor = Color.GRAY
            textButtonStyle.pressedOffsetX = 2f
            textButtonStyle.pressedOffsetY = -3f
            skin.add("default", textButtonStyle)
            //        
            //        skin = EmergenceGame.game.getAssetManager().get(
            //                new File(FileLocations.SKINS_DIRECTORY, "main_menu.skin").getPath());
            _pauseMenuSkin = skin
        }
    }

}
