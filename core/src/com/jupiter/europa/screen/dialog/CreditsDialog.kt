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

/**

 * @author Nathan Templon
 */
public class CreditsDialog : ObservableDialog(CreditsDialog.DIALOG_NAME, CreditsDialog.getDefaultSkin().get(javaClass<Window.WindowStyle>())) {


    // Fields
    private val skinInternal = getDefaultSkin()

    private var mainTable: Table? = null
    private var wrapperTable: Table? = null
    private var scrollPane: ScrollPane? = null
    private var label: Label? = null
    private var returnButton: EuropaButton? = null


    init {

        this.initComponents()
    }


    // Private Methods
    private fun initComponents() {
        // Credits Dialog
        this.label = Label(EuropaGame.game.credits, skinInternal.get(MainMenuScreen.INFO_STYLE_KEY, javaClass<Label.LabelStyle>()))
        this.scrollPane = ScrollPane(this.label, skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<ScrollPane.ScrollPaneStyle>()))
        this.returnButton = EuropaButton("Return to Menu", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.returnButton?.addClickListener { args -> this.hide() }

        this.wrapperTable = Table()
        this.wrapperTable!!.add<ScrollPane>(this.scrollPane).pad(MainMenuScreen.LIST_WRAPPER_PADDING.toFloat()).expand().fill()
        this.wrapperTable!!.background(this.skinInternal.get(MainMenuScreen.CREDITS_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

        this.mainTable = Table()

        this.mainTable!!.add<Table>(this.wrapperTable).row()
        this.mainTable!!.add<EuropaButton>(this.returnButton).center().space(MainMenuScreen.COMPONENT_SPACING.toFloat())

        this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())

        this.getContentTable().add<Table>(this.mainTable).expandY().fillY().minWidth(MainMenuScreen.DIALOG_WIDTH.toFloat()).center()
    }

    companion object {

        // Constants
        public val DIALOG_NAME: String = ""


        // Static Methods
        private fun getDefaultSkin(): Skin {
            return MainMenuScreen.createMainMenuSkin()
        }
    }

}
