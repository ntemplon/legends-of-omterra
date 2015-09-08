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

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.scene2d.ui.EuropaButton
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent
import com.jupiter.europa.scene2d.ui.ObservableDialog
import com.jupiter.europa.screen.MainMenuScreen
import com.badlogic.gdx.scenes.scene2d.ui.List as GuiList
import com.badlogic.gdx.utils.Array as GdxArray

/**

 * @author Nathan Templon
 */
public class LoadGameDialog : ObservableDialog(LoadGameDialog.DIALOG_NAME, LoadGameDialog.getDefaultSkin()) {

    // Enumerations
    public enum class LoadGameExitStates {

        LOAD,
        CANCEL
    }


    // Fields
    private val skinInternal = getDefaultSkin()

    private var mainTable: Table? = null
    private var loadGameLabel: Label? = null
    private var listTable: Table? = null
    private var gameList: GuiList<String>? = null
    private var gameListPane: ScrollPane? = null
    private var buttonTableInternal: Table? = null
    private var okButton: EuropaButton? = null
    private var cancelButton: EuropaButton? = null
    private var deleteButton: EuropaButton? = null

    private var widthInternal: Float = 0.toFloat()
    private var heightInternal: Float = 0.toFloat()

    public var gameToLoad: String = ""
        private set
    public var exitState: LoadGameExitStates? = null
        private set

    private var confirmDeleteDialog: ConfirmDeleteSaveDialog? = null


    init {

        this.initComponents()
    }


    // Public Methods
    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        if (this.confirmDeleteDialog != null) {
            this.confirmDeleteDialog!!.setSize(width, this.heightInternal)
        }

        this.widthInternal = width
        this.heightInternal = height
    }


    // Private Methods
    private fun initComponents() {
        this.mainTable = Table()

        this.listTable = Table()
        this.loadGameLabel = Label("Save Games", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<Label.LabelStyle>()))
        this.gameList = GuiList(skinInternal.get<GuiList.ListStyle>(MainMenuScreen.DEFAULT_KEY, javaClass<GuiList.ListStyle>()))
        this.gameList?.setItems(GdxArray(EuropaGame.game.getSaveNames()))
        this.gameListPane = ScrollPane(this.gameList, skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<ScrollPane.ScrollPaneStyle>()))

        this.listTable!!.add<ScrollPane>(this.gameListPane).pad(MainMenuScreen.LIST_WRAPPER_PADDING.toFloat()).expand().fill()
        this.listTable!!.background(skinInternal.get(MainMenuScreen.LIST_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

        this.okButton = EuropaButton("Accept", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.okButton!!.addClickListener { args -> this.onLoadClick(args) }

        this.cancelButton = EuropaButton("Cancel", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.cancelButton!!.addClickListener { args -> this.onCancelClick(args) }

        this.deleteButton = EuropaButton("Delete", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.deleteButton!!.addClickListener { args -> this.onDeleteClick(args) }

        this.buttonTableInternal = Table()
        this.buttonTableInternal!!.add<EuropaButton>(this.cancelButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right().expandX()
        this.buttonTableInternal!!.add<EuropaButton>(this.deleteButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()
        this.buttonTableInternal!!.add<EuropaButton>(this.okButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()

        this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())

        this.mainTable!!.add<Label>(this.loadGameLabel).center().left()
        this.mainTable!!.row()
        this.mainTable!!.add<Table>(this.listTable).expandY().fill()
        this.mainTable!!.row()
        this.mainTable!!.add<Table>(this.buttonTableInternal).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).right().expandX().fillX()
        this.mainTable!!.row()

        this.mainTable!!.background(this.skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

        this.getContentTable().add<Table>(this.mainTable).center().expandY().fillY().width(MainMenuScreen.DIALOG_WIDTH.toFloat())
    }

    private fun onLoadClick(event: ClickEvent) {
        this.gameToLoad = this.gameList!!.getSelected().toString()
        this.exitState = LoadGameExitStates.LOAD
        this.hide()
    }

    private fun onCancelClick(event: ClickEvent) {
        this.gameToLoad = ""
        this.exitState = LoadGameExitStates.CANCEL
        this.hide()
    }

    private fun onDeleteClick(event: ClickEvent) {
        this.confirmDeleteDialog = ConfirmDeleteSaveDialog(this.gameList!!.getSelected().toString(), this.skinInternal)
        this.confirmDeleteDialog!!.addDialogListener({ args -> this.onConfirmDeleteSaveDialogClose(args) }, ObservableDialog.DialogEvents.HIDDEN)
        this.confirmDeleteDialog!!.show(this.getStage())
        this.confirmDeleteDialog!!.setSize(this.widthInternal, this.heightInternal)
    }

    private fun onConfirmDeleteSaveDialogClose(args: ObservableDialog.DialogEventArgs) {
        if (this.confirmDeleteDialog!!.exitState === ConfirmDeleteSaveDialog.ConfirmDeleteExitStates.DELETE) {
            EuropaGame.game.deleteSave(this.gameList!!.getSelected().toString())
            this.gameList!!.setItems(GdxArray(EuropaGame.game.getSaveNames()))
            this.okButton!!.setDisabled(EuropaGame.game.getSaveNames().size() == 0)
        }
    }


    // Nested Classes
    private class ConfirmDeleteSaveDialog(public val saveToDelete: String, private val skinInternal: Skin) : ObservableDialog(LoadGameDialog.ConfirmDeleteSaveDialog.DIALOG_NAME, skinInternal.get(MainMenuScreen.POPUP_DIALOG_STYLE_KEY, javaClass<Window.WindowStyle>())) {

        // Enumerations
        public enum class ConfirmDeleteExitStates {

            DELETE,
            CANCEL
        }

        private var titleLabelInternal: Label? = null
        private var mainTable: Table? = null
        private var yesButton: TextButton? = null
        private var noButton: TextButton? = null

        public var exitState: ConfirmDeleteExitStates? = null
            private set


        init {

            this.initComponents()
        }


        // Public Methods
        override fun setSize(width: Float, height: Float) {
            super.setSize(width, height)
        }


        // Private Methods
        private fun initComponents() {
            this.titleLabelInternal = Label("Are you sure you want to delete the save game \"" + this.saveToDelete + "\"?", skinInternal.get(MainMenuScreen.INFO_STYLE_KEY, javaClass<Label.LabelStyle>()))
            this.titleLabelInternal!!.setWrap(true)
            this.titleLabelInternal!!.setAlignment(Align.center)

            this.yesButton = TextButton("Yes", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
            this.yesButton!!.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if (event!!.getButton() == Input.Buttons.LEFT && !this@ConfirmDeleteSaveDialog.yesButton!!.isDisabled()) {
                        this@ConfirmDeleteSaveDialog.onYesButtonClick()
                    }
                    this@ConfirmDeleteSaveDialog.yesButton!!.setChecked(false)
                }
            })

            this.noButton = TextButton("No", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
            this.noButton!!.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if (event!!.getButton() == Input.Buttons.LEFT && !this@ConfirmDeleteSaveDialog.noButton!!.isDisabled()) {
                        this@ConfirmDeleteSaveDialog.onNoButtonClick()
                    }
                    this@ConfirmDeleteSaveDialog.noButton!!.setChecked(false)
                }
            })

            this.mainTable = Table()
            this.mainTable!!.add<Label>(this.titleLabelInternal).colspan(2).width(MainMenuScreen.DIALOG_WIDTH.toFloat()).expandX().fillX()
            this.mainTable!!.row()
            this.mainTable!!.add<TextButton>(this.noButton).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).right()
            this.mainTable!!.add<TextButton>(this.yesButton).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).left()

            this.mainTable!!.padLeft(MainMenuScreen.TABLE_PADDING.toFloat())
            this.mainTable!!.padRight(MainMenuScreen.TABLE_PADDING.toFloat())

            this.mainTable!!.background(this.skinInternal.get(MainMenuScreen.POPUP_BACKGROUND_KEY, javaClass<SpriteDrawable>()))
            this.getContentTable().add<Table>(this.mainTable).row()
        }

        private fun onYesButtonClick() {
            this.exitState = ConfirmDeleteExitStates.DELETE
            this.hide()
        }

        private fun onNoButtonClick() {
            this.exitState = ConfirmDeleteExitStates.CANCEL
            this.hide()
        }

        companion object {


            // Constants
            private val DIALOG_NAME = ""
        }

    }// Properties

    companion object {


        // Constants
        private val DIALOG_NAME = ""


        // Static Methods
        private fun getDefaultSkin(): Skin {
            return MainMenuScreen.createMainMenuSkin()
        }
    }

}
