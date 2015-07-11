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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.scene2d.ui.EuropaButton
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent
import com.jupiter.europa.scene2d.ui.ObservableDialog
import com.jupiter.europa.scene2d.ui.TabbedPane
import com.jupiter.europa.screen.MainMenuScreen

import com.jupiter.ganymede.event.Listener

/**

 * @author Nathan Templon
 */
public class OptionsDialog : ObservableDialog(OptionsDialog.DIALOG_NAME, OptionsDialog.getDefaultSkin().get(javaClass<Window.WindowStyle>())) {

    // Enumerations
    public enum class OptionsDialogExitStates {

        ACCEPT,
        CANCEL
    }


    // Fields
    private val skinInternal: Skin

    private var mainTable: Table? = null
    private var optionsLabel: Label? = null
    private var optionsPane: TabbedPane? = null
    private var optionsAcceptButton: EuropaButton? = null
    private var optionsCancelButton: EuropaButton? = null
    private var optionsButtonTable: Table? = null

    private var audioTable: AudioOptionsTable? = null
    private var graphicsTable: GraphicOptionsTable? = null

    public val exitState: OptionsDialogExitStates? = null


    init {

        this.skinInternal = getDefaultSkin()

        this.initComponent()

        this.addDialogListener(Listener { this.loadSettings() }, ObservableDialog.DialogEvents.SHOWN)
    }


    // Private Methods
    private fun initComponent() {
        this.mainTable = Table()
        this.optionsLabel = Label("Options", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<Label.LabelStyle>()))
        this.optionsPane = TabbedPane(skinInternal.get(MainMenuScreen.TAB_STYLE_KEY, javaClass<TextButton.TextButtonStyle>()))

        this.optionsAcceptButton = EuropaButton("Accept", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.optionsAcceptButton!!.addClickListener(Listener { args -> this.onAcceptClick(args) })

        this.optionsCancelButton = EuropaButton("Cancel", skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<TextButton.TextButtonStyle>()))
        this.optionsCancelButton!!.addClickListener(Listener { this.onCancelClick(it) })

        this.optionsButtonTable = Table()
        this.optionsButtonTable!!.add<EuropaButton>(this.optionsCancelButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right().expandX()
        this.optionsButtonTable!!.add<EuropaButton>(this.optionsAcceptButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()

        // Create and Add Tabs
        this.audioTable = AudioOptionsTable(this.skinInternal)
        this.graphicsTable = GraphicOptionsTable(this.skinInternal)
        this.optionsPane!!.addTab("Audio", this.audioTable!!)
        this.optionsPane!!.addTab("Graphics", this.graphicsTable!!)

        this.mainTable!!.add<Label>(this.optionsLabel).left()
        this.mainTable!!.row()
        this.mainTable!!.add<TabbedPane>(this.optionsPane).expand().fill().top()
        this.mainTable!!.row()
        this.mainTable!!.add<Table>(this.optionsButtonTable).expandX().fillX().right()

        this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())
        this.mainTable!!.background(this.skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

        this.getContentTable().add<Table>(this.mainTable).center().expandY().fillY().width(MainMenuScreen.DIALOG_WIDTH.toFloat())
    }

    private fun onAcceptClick(event: ClickEvent) {
        this.applySettings()
        this.hide()
    }

    private fun onCancelClick(event: ClickEvent) {
        this.hide()
    }

    private fun loadSettings() {
        this.audioTable!!.setMusicVolume(EuropaGame.game.settings!!.musicVolume.get())
    }

    private fun applySettings() {
        EuropaGame.game.settings!!.musicVolume.set(this.audioTable!!.getMusicVolume())
        EuropaGame.game.saveSettings()
    }


    // Nested Classes
    private class AudioOptionsTable(private val skinInternal: Skin) : Table() {
        private val volumeLabel: Label
        private val musicSlider: Slider


        // Properties
        public fun setMusicVolume(volume: Float) {
            this.musicSlider.setValue(volume)
        }

        public fun getMusicVolume(): Float {
            return this.musicSlider.getValue()
        }


        init {

            this.volumeLabel = Label("Volumne:", skinInternal.get(MainMenuScreen.INFO_STYLE_KEY, javaClass<LabelStyle>()))

            this.musicSlider = Slider(0f, 1f, 0.05f, false, skinInternal.get(MainMenuScreen.DEFAULT_KEY, javaClass<Slider.SliderStyle>()))

            this.add(this.volumeLabel).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).center().left()
            this.add(this.musicSlider).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).center().minHeight(20f).expandX().fillX()
            this.row()

            this.pad(MainMenuScreen.TABLE_PADDING.toFloat())
        }

    }


    private class GraphicOptionsTable(private val skinInternal: Skin) : Table() {


        init {

            this.pad(MainMenuScreen.TABLE_PADDING.toFloat())
        }

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
