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

package com.jupiter.europa.screen.dialog

import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.scene2d.ui.EuropaButton
import com.jupiter.europa.scene2d.ui.ObservableDialog
import com.jupiter.europa.screen.MainMenuScreen
import com.badlogic.gdx.scenes.scene2d.ui.List as GuiList
import com.badlogic.gdx.utils.Array as GdxArray

/**

 * @author Nathan Templon
 */
public class NewGameDialog : ObservableDialog(NewGameDialog.DIALOG_NAME, NewGameDialog.getDefaultSkin()) {

    // Enumerations
    public enum class NewGameExitStates {
        START_GAME,
        CANCEL
    }


    // Fields
    private val skinInternal = getDefaultSkin()

    private var mainTable: Table? = null
    private var titleLabelInternal: Label? = null
    private var gameNameField: TextField? = null
    private var woldLabel: Label? = null
    private var listWrapper: Table? = null
    private var newGameWorldList: GuiList<String>? = null
    private var worldPane: ScrollPane? = null
    private var buttonTableInternal: Table? = null
    private var nextButton: EuropaButton? = null
    private var backButton: EuropaButton? = null

    public var exitState: NewGameExitStates? = null
        private set

    public fun getNewGameName(): String {
        return this.gameNameField!!.getText()
    }

    public fun getNewGameWorldName(): String {
        return this.newGameWorldList!!.getSelected().toString()
    }


    init {

        this.initComponents()
    }


    // Private Methods
    private fun initComponents() {
        this.mainTable = Table()

        this.titleLabelInternal = Label("Save Name: ", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<Label.LabelStyle>()))
        this.gameNameField = TextField("default", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextField.TextFieldStyle>()))
        this.gameNameField!!.setMaxLength(16)
        this.woldLabel = Label("World:", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<Label.LabelStyle>()))
        this.newGameWorldList = GuiList<String>(skinInternal.get<GuiList.ListStyle>(MainMenuScreen.DEFAULT_KEY, javaClass<GuiList.ListStyle>()))
        this.newGameWorldList!!.setItems(GdxArray(EuropaGame.game.getWorldNames()))
        this.worldPane = ScrollPane(this.newGameWorldList, skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<ScrollPane.ScrollPaneStyle>()))

        this.listWrapper = Table()
        this.listWrapper!!.add<ScrollPane>(this.worldPane).pad(MainMenuScreen.LIST_WRAPPER_PADDING.toFloat()).expand().fill()
        this.listWrapper!!.background(skinInternal.get(MainMenuScreen.LIST_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

        this.nextButton = EuropaButton("Accept", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.nextButton!!.addClickListener { e -> this.startNewGame() }

        this.backButton = EuropaButton("Back", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.backButton!!.addClickListener { this.cancelNewGame() }

        this.buttonTableInternal = Table()
        this.buttonTableInternal!!.add<EuropaButton>(this.backButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right().expandX()
        this.buttonTableInternal!!.add<EuropaButton>(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()

        this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())

        this.mainTable!!.add<Label>(this.titleLabelInternal).center().left()
        this.mainTable!!.add<TextField>(this.gameNameField).center().left().padTop(15f).expandX().fillX()
        this.mainTable!!.row()
        this.mainTable!!.add<Label>(this.woldLabel).colspan(2).left()
        this.mainTable!!.row()
        this.mainTable!!.add<Table>(this.listWrapper).colspan(2).expandY().fill()
        this.mainTable!!.row()
        this.mainTable!!.add<Table>(this.buttonTableInternal).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).colspan(2).right().expandX().fillX()
        this.mainTable!!.row()

        this.mainTable!!.background(this.skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

        this.getContentTable().add<Table>(this.mainTable).center().expandY().fillY().width(MainMenuScreen.DIALOG_WIDTH.toFloat())
    }

    private fun startNewGame() {
        this.exitState = NewGameExitStates.START_GAME
        this.hide()
    }

    private fun cancelNewGame() {
        this.exitState = NewGameExitStates.CANCEL
        this.hide()
    }

    companion object {


        // Constants
        private val DIALOG_NAME = ""


        // Static Methods
        private fun getDefaultSkin(): Skin {
            return MainMenuScreen.createMainMenuSkin()
        }
    }

}
