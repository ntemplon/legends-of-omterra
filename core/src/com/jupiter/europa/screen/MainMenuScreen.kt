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

package com.jupiter.europa.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.audio.AudioService
import com.jupiter.europa.entity.Party
import com.jupiter.europa.geometry.Size
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.save.SaveGame
import com.jupiter.europa.scene2d.ui.EuropaButton
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector
import com.jupiter.europa.scene2d.ui.NumberSelector.NumberSelectorStyle
import com.jupiter.europa.scene2d.ui.ObservableDialog.DialogEventArgs
import com.jupiter.europa.scene2d.ui.ObservableDialog.DialogEvents
import com.jupiter.europa.scene2d.ui.TraitPoolSelector.TraitPoolSelectorStyle
import com.jupiter.europa.screen.dialog.*
import com.jupiter.europa.screen.dialog.CreateCharacterDialog.CreateCharacterExitStates
import com.jupiter.europa.screen.dialog.NewGameDialog.NewGameExitStates
import com.jupiter.ganymede.event.Listener
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

/**

 * @author Nathan Templon
 */
public class MainMenuScreen : Screen, InputProcessor {

    // Enumerations
    public enum class DialogExitStates {
        NEXT,
        BACK
    }


    // Fields
    private var stage: Stage? = null
    private var background: Image? = null

    private var titleTable: Table? = null
    private var buttonTable: Table? = null
    private var titleWrapperTable: Table? = null
    private var newGameButton: EuropaButton? = null
    private var loadGameButton: EuropaButton? = null
    private var multiplayerButton: EuropaButton? = null
    private var optionsButton: EuropaButton? = null
    private var creditsButton: EuropaButton? = null
    private var quitButton: EuropaButton? = null

    private var createCharacterDialog: CreateCharacterDialog? = null
    private var newGameDialog: NewGameDialog? = null
    private var loadGameDialog: LoadGameDialog? = null
    private var creditsDialog: CreditsDialog? = null
    private var optionsDialog: OptionsDialog? = null

    private var size = Size(0, 0)


    // Screen Implementation
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        this.stage!!.act(delta)
        this.stage!!.draw()
    }

    override fun resize(width: Int, height: Int) {
        this.size = Size(width, height)

        // True puts 0, 0 at the bottom left corner, false or omission puts 0, 0 at the center
        this.stage!!.getViewport().update(width, height, true)

        // Resize dialogs
        if (this.createCharacterDialog != null) {
            this.createCharacterDialog!!.setSize(width.toFloat(), height.toFloat())
        }
        if (this.newGameDialog != null) {
            this.newGameDialog!!.setSize(width.toFloat(), height.toFloat())
        }
        if (this.loadGameDialog != null) {
            this.loadGameDialog!!.setSize(width.toFloat(), height.toFloat())
        }
        if (this.creditsDialog != null) {
            this.creditsDialog!!.setSize(width.toFloat(), height.toFloat())
        }
        if (this.optionsDialog != null) {
            this.optionsDialog!!.setSize(width.toFloat(), height.toFloat())
        }
    }

    override fun show() {
        EuropaGame.game.inspectSaves()
        this.init()

        // Play Music
        EuropaGame.game.audioService?.playMusic(AudioService.TITLE_MUSIC)

        // Create Screens
        this.optionsDialog = OptionsDialog()
        this.creditsDialog = CreditsDialog()
    }

    override fun hide() {
        EuropaGame.game.audioService?.stop()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        if (this.stage != null) {
            this.stage!!.dispose()
        }
    }


    // InputProcessor Implementation
    override fun keyDown(i: Int): Boolean {
        return this.stage!!.keyDown(i)
    }

    override fun keyUp(i: Int): Boolean {
        return this.stage!!.keyUp(i)
    }

    override fun keyTyped(c: Char): Boolean {
        return this.stage!!.keyTyped(c)
    }

    override fun touchDown(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return this.stage!!.touchDown(i, i1, i2, i3)
    }

    override fun touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return this.stage!!.touchUp(i, i1, i2, i3)
    }

    override fun touchDragged(i: Int, i1: Int, i2: Int): Boolean {
        return this.stage!!.touchDragged(i, i1, i2)
    }

    override fun mouseMoved(i: Int, i1: Int): Boolean {
        return this.stage!!.mouseMoved(i, i1)
    }

    override fun scrolled(i: Int): Boolean {
        return this.stage!!.scrolled(i)
    }


    // Private Methods
    private fun init() {
        this.stage = Stage(ScreenViewport())
        val skin = createMainMenuSkin()

        // Background
        this.background = Image(EuropaGame.game.assetManager?.get(BACKGROUND_FILE_NAME, javaClass<Texture>()))
        this.background!!.setFillParent(true)
        this.stage!!.addActor(this.background)

        // Create Buttons
        this.buttonTable = Table()
        this.buttonTable!!.setFillParent(false)
        this.buttonTable!!.center()

        this.newGameButton = EuropaButton("New Game", skin.get(DEFAULT_KEY, javaClass<TextButtonStyle>())) // Use the initialized skin
        this.newGameButton!!.addClickListener(Listener { args -> this.onNewGameClick(args) })

        this.loadGameButton = EuropaButton("Load Game", skin.get(DEFAULT_KEY, javaClass<TextButtonStyle>()))
        this.loadGameButton!!.addClickListener(Listener { args -> this.onLoadGameClick(args) })
        this.loadGameButton!!.setDisabled(EuropaGame.game.getSaveNames().size() == 0)

        this.multiplayerButton = EuropaButton("Multiplayer", skin.get(DEFAULT_KEY, javaClass<TextButtonStyle>()))
        this.multiplayerButton!!.addClickListener(Listener { args -> this.onMultiplayerClick(args) })
        this.multiplayerButton!!.setDisabled(true)

        this.optionsButton = EuropaButton("Options", skin.get(DEFAULT_KEY, javaClass<TextButtonStyle>()))
        this.optionsButton!!.addClickListener(Listener { args -> this.onOptionsClick(args) })

        this.creditsButton = EuropaButton("Credits", skin.get(DEFAULT_KEY, javaClass<TextButtonStyle>()))
        this.creditsButton!!.addClickListener(Listener { args -> this.onCreditsClick(args) })

        this.quitButton = EuropaButton("Exit", skin.get(DEFAULT_KEY, javaClass<TextButtonStyle>()))
        this.quitButton!!.addClickListener(Listener { args -> this.onQuitClick(args) })

        // Configure Button Table
        this.buttonTable!!.add<EuropaButton>(this.newGameButton).width(TITLE_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.buttonTable!!.row()
        this.buttonTable!!.add<EuropaButton>(this.loadGameButton).width(TITLE_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.buttonTable!!.row()
        this.buttonTable!!.add<EuropaButton>(this.multiplayerButton).width(TITLE_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.buttonTable!!.row()
        this.buttonTable!!.add<EuropaButton>(this.optionsButton).width(TITLE_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.buttonTable!!.row()
        this.buttonTable!!.add<EuropaButton>(this.creditsButton).width(TITLE_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.buttonTable!!.row()
        this.buttonTable!!.add<EuropaButton>(this.quitButton).width(TITLE_BUTTON_WIDTH.toFloat()).space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.buttonTable!!.row()

        // Title
        this.titleTable = Table()
        this.titleTable!!.setFillParent(true)
        this.titleTable!!.center()

        this.titleWrapperTable = Table()
        val titleWords = EuropaGame.TITLE.split("\\s+")
        this.titleWrapperTable!!.add(Label(titleWords[0].substring(0, 1), skin.get(FANCY_KEY, javaClass<LabelStyle>()))).right().expandX()
        this.titleWrapperTable!!.add(Label(titleWords[0].substring(1), skin.get(DEFAULT_KEY, javaClass<LabelStyle>())))
        for (i in 1..titleWords.size() - 1 - 1) {
            var text = titleWords[i]
            if (i == 1) {
                text = " " + text
            }
            if (i == titleWords.size() - 1) {
                text += " "
            }
            this.titleWrapperTable!!.add(Label(text, skin.get(DEFAULT_KEY, javaClass<LabelStyle>())))
        }
        this.titleWrapperTable!!.add(Label(titleWords[titleWords.size() - 1].substring(0, 1), skin.get(FANCY_KEY, javaClass<LabelStyle>())))
        this.titleWrapperTable!!.add(Label(titleWords[titleWords.size() - 1].substring(1), skin.get(DEFAULT_KEY, javaClass<LabelStyle>()))).left().expandX()

        this.titleWrapperTable!!.background(skin.get(TITLE_BACKGROUND_KEY, javaClass<TextureRegionDrawable>()))

        this.titleTable!!.add<Table>(this.titleWrapperTable).pad(COMPONENT_SPACING.toFloat()).top()
        this.titleTable!!.row()
        this.titleTable!!.add<Table>(this.buttonTable).center().expandY()
        this.titleTable!!.row()

        this.stage!!.addActor(this.titleTable)
    }

    private fun onNewGameClick(event: ClickEvent) {
        this.createCharacterDialog = CreateCharacterDialog()
        this.createCharacterDialog!!.addDialogListener(Listener { args -> this.onCharacterCreationCompleted(args) }, DialogEvents.HIDDEN)
        this.showDialog(this.createCharacterDialog!!)
    }

    private fun onLoadGameClick(event: ClickEvent) {
        this.loadGameDialog = LoadGameDialog()
        this.loadGameDialog!!.addDialogListener(Listener { args -> this.onLoadGameDialogHidden(args) }, DialogEvents.HIDDEN)
        this.showDialog(this.loadGameDialog!!)
    }

    private fun onMultiplayerClick(event: ClickEvent) {
        println("Multiplayer is not yet implemented!")
    }

    private fun onOptionsClick(event: ClickEvent) {
        this.showDialog(this.optionsDialog!!)
    }

    private fun onCreditsClick(event: ClickEvent) {
        this.showDialog(this.creditsDialog!!)
    }

    private fun onQuitClick(event: ClickEvent) {
        Gdx.app.exit()
    }

    private fun onCharacterCreationCompleted(args: DialogEventArgs) {
        if (this.createCharacterDialog!!.exitState == CreateCharacterExitStates.OK) {
            this.newGameDialog = NewGameDialog()
            this.newGameDialog!!.addDialogListener(Listener { args -> this.onNewGameDialogHidden(args) }, DialogEvents.HIDDEN)
            this.showDialog(this.newGameDialog!!)
        }
    }

    private fun onNewGameDialogHidden(args: DialogEventArgs) {
        if (this.newGameDialog!!.exitState == NewGameExitStates.START_GAME) {
            this.startNewGame()
        } else {
            this.showDialog(this.createCharacterDialog!!)
        }
    }

    private fun onLoadGameDialogHidden(args: DialogEventArgs) {
        if (this.loadGameDialog!!.exitState === LoadGameDialog.LoadGameExitStates.LOAD) {
            this.loadGame()
        }
    }

    private fun startNewGame() {
        var gameName: String? = this.newGameDialog!!.getNewGameName()
        if (gameName == null || gameName.isEmpty()) {
            gameName = "default"
        }

        val world = EuropaGame.game.getWorld(this.newGameDialog!!.getNewGameWorldName())

        val party = Party()

        // Get Entities here
        val entity = this.createCharacterDialog!!.createdEntity

        if (entity != null) {
            party.addPlayer(entity)
            party.selectPlayer(entity)

            EuropaGame.game.startGame(gameName, world, party)
        }
    }

    private fun loadGame() {
        val saveFile = FileLocations.SAVE_DIRECTORY.resolve(this.loadGameDialog!!.gameToLoad + "." + SaveGame.SAVE_EXTENSION)
        if (!Files.exists(saveFile)) {
            return
        }

        try {
            Files.newBufferedReader(saveFile).use { reader ->
                val json = Json()
                val value = JsonReader().parse(reader)
                val save = json.fromJson(javaClass<SaveGame>(), value.toString())
                EuropaGame.game.startGame(save)
            }
        } catch (ex: IOException) {
        }

    }

    private fun showDialog(dialog: Dialog) {
        dialog.show(this.stage).setSize(this.size.width.toFloat(), this.size.height.toFloat())
    }

    companion object {


        // Constants
        public val MAIN_MENU_SKIN_DIRECTORY: Path = FileLocations.SKINS_DIRECTORY.resolve("main_menu")

        public val TITLE_FANCY_FONT: String = "Diploma56-bold.fnt"
        public val TITLE_FONT: String = "MagicMedieval48-bold.fnt"
        public val BUTTON_FONT: String = "MagicMedieval40.fnt"
        public val LIST_FONT: String = "MagicMedieval32.fnt"
        public val TEXT_FIELD_FONT: String = "MagicMedieval32.fnt"
        public val INFO_LABEL_FONT: String = "MagicMedieval24.fnt"

        public val DEFAULT_KEY: String = "default"
        public val FANCY_KEY: String = "fancy"
        public val INFO_STYLE_KEY: String = "info"
        public val TAB_STYLE_KEY: String = "tab-style"
        public val POPUP_DIALOG_STYLE_KEY: String = "popup-dialog-style"

        public val BACKGROUND_FILE_NAME: String = FileLocations.UI_IMAGES_DIRECTORY.resolve("main_menu_background.png").toString()
        public val ATLAS_KEY: String = "main_menu.atlas"
        public val SOLID_TEXTURE_KEY: String = "solid-texture"
        public val DIALOG_BACKGROUND_KEY: String = "dialog-border"
        public val POPUP_BACKGROUND_KEY: String = "popup-border"
        public val TITLE_BACKGROUND_KEY: String = "title-border"
        public val BUTTON_BACKGROUND_KEY: String = "button-background"
        public val BUTTON_DOWN_KEY: String = "button-background-down"
        public val SLIDER_BACKGROUND_KEY: String = "slider-background-main_menu"
        public val LIST_BACKGROUND_KEY: String = "list-background"
        public val LIST_SELECTION_KEY: String = "list-selection"
        public val SLIDER_KNOB_KEY: String = "slider-knob-main_menu"
        public val TITLE_FONT_KEY: String = "title-font"
        public val TITLE_FANCY_FONT_KEY: String = "title-font-fancy"
        public val BUTTON_FONT_KEY: String = "button-font"
        public val TEXT_FIELD_FONT_KEY: String = "text-field-font"
        public val LIST_FONT_KEY: String = "list-font"
        public val INFO_LABEL_FONT_KEY: String = "info-label-font"
        public val SCROLL_BAR_VERTICAL_KEY: String = "scroll-bar-vertical"
        public val SCROLL_BAR_VERTICAL_KNOB_KEY: String = "scroll-bar-vertical-knob"
        public val DROP_DOWN_LIST_BACKGROUND: String = "drop-down-list-background"
        public val CREDITS_BACKGROUND_KEY: String = "credits-background"
        public val TAB_BUTTON_BACKGROUND_KEY: String = "tab-button-background"
        public val TAB_BUTTON_SELECTED_KEY: String = "tab-button-background-selected"
        public val NUMBER_SELECTOR_INCREASE_KEY: String = "number-increase"
        public val NUMBER_SELECTOR_DECREASE_KEY: String = "number-decrease"

        public var mainMenuSkin: Skin? = null

        public val COMPONENT_SPACING: Int = 4
        public val TITLE_BUTTON_WIDTH: Int = 250
        public val DIALOG_BUTTON_WIDTH: Int = 190
        public val TABLE_PADDING: Int = 14
        public val LIST_WRAPPER_PADDING: Int = 20

        public val DIALOG_WIDTH: Int = 850

        public val BACKGROUND_COLOR: Color = Color(Color.WHITE)
        public val SELECTION_COLOR: Color = Color(0.0f, 0.0f, 0.0f, 0.2f)
        public val TRANSPARENT: Color = Color(1f, 1f, 1f, 0f)


        // Static Methods
        public fun createMainMenuSkin(): Skin {
            if (mainMenuSkin == null) {
                buildMainMenuSkin()
            }
            return mainMenuSkin!!
        }

        private fun buildMainMenuSkin() {
            val skin = Skin()

            // Fonts
            skin.add(BUTTON_FONT_KEY, EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(BUTTON_FONT).toString()))
            skin.add(TITLE_FANCY_FONT_KEY, EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(TITLE_FANCY_FONT).toString()))
            skin.add(TITLE_FONT_KEY, EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(TITLE_FONT).toString()))
            skin.add(LIST_FONT_KEY, EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(LIST_FONT).toString()))
            skin.add(TEXT_FIELD_FONT_KEY, EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(TEXT_FIELD_FONT).toString()))
            skin.add(INFO_LABEL_FONT_KEY, EuropaGame.game.assetManager!!.get<Any>(FileLocations.FONTS_DIRECTORY.resolve(INFO_LABEL_FONT).toString()))

            // Set the background texture
            val pixmap = Pixmap(1, 1, Pixmap.Format.RGB888)
            pixmap.setColor(Color.WHITE)
            pixmap.fill()
            skin.add(SOLID_TEXTURE_KEY, Texture(pixmap))
            val transparentDrawable = skin.newDrawable(SOLID_TEXTURE_KEY, TRANSPARENT)

            // Get values from the atlas
            skin.addRegions(EuropaGame.game.assetManager!!.get(MAIN_MENU_SKIN_DIRECTORY.resolve(ATLAS_KEY).toString(), javaClass<TextureAtlas>()))

            // Colors
            val textButtonFontColor = Color(0.85f, 0.85f, 0.85f, 1.0f)

            // Set images
            val textButtonBackground = TextureRegionDrawable(skin.get(BUTTON_BACKGROUND_KEY, javaClass<TextureRegion>()))
            textButtonBackground.setLeftWidth(32f)
            textButtonBackground.setRightWidth(32f)
            textButtonBackground.setTopHeight(5f)
            textButtonBackground.setBottomHeight(5f)
            skin.add(BUTTON_BACKGROUND_KEY, textButtonBackground)

            val textButtonBackgroundDown = TextureRegionDrawable(skin.get(BUTTON_DOWN_KEY, javaClass<TextureRegion>()))
            textButtonBackgroundDown.setLeftWidth(32f)
            textButtonBackgroundDown.setRightWidth(32f)
            textButtonBackgroundDown.setTopHeight(5f)
            textButtonBackgroundDown.setBottomHeight(5f)
            skin.add(BUTTON_DOWN_KEY, textButtonBackgroundDown)

            val listSelection = TextureRegionDrawable(skin.get(LIST_SELECTION_KEY, javaClass<TextureRegion>()))
            listSelection.setLeftWidth(7f)
            listSelection.setRightWidth(7f)
            listSelection.setTopHeight(0f)
            listSelection.setBottomHeight(0f)
            skin.add(LIST_SELECTION_KEY, listSelection)

            val tabButtonBackground = TextureRegionDrawable(skin.get(TAB_BUTTON_BACKGROUND_KEY, javaClass<TextureRegion>()))
            tabButtonBackground.setLeftWidth(5f)
            tabButtonBackground.setRightWidth(5f)
            tabButtonBackground.setTopHeight(0f)
            tabButtonBackground.setBottomHeight(0f)
            skin.add(TAB_BUTTON_BACKGROUND_KEY, tabButtonBackground)

            val tabButtonBackgroundSelected = TextureRegionDrawable(skin.get(TAB_BUTTON_SELECTED_KEY, javaClass<TextureRegion>()))
            tabButtonBackgroundSelected.setLeftWidth(5f)
            tabButtonBackgroundSelected.setRightWidth(5f)
            tabButtonBackgroundSelected.setTopHeight(0f)
            tabButtonBackgroundSelected.setBottomHeight(0f)
            skin.add(TAB_BUTTON_SELECTED_KEY, tabButtonBackgroundSelected)

            val titleBackground = TextureRegionDrawable(skin.get(TITLE_BACKGROUND_KEY, javaClass<TextureRegion>()))
            titleBackground.setLeftWidth(10f)
            titleBackground.setRightWidth(10f)
            titleBackground.setTopHeight(0f)
            titleBackground.setBottomHeight(0f)
            skin.add(TITLE_BACKGROUND_KEY, titleBackground)

            val numberIncreaseDrawable = TextureRegionDrawable(skin.get(NUMBER_SELECTOR_INCREASE_KEY, javaClass<TextureRegion>()))
            numberIncreaseDrawable.setLeftWidth(0f)
            numberIncreaseDrawable.setRightWidth(0f)
            numberIncreaseDrawable.setTopHeight(0f)
            numberIncreaseDrawable.setBottomHeight(0f)
            skin.add(NUMBER_SELECTOR_INCREASE_KEY, numberIncreaseDrawable)

            val numberDecreaseDrawable = TextureRegionDrawable(skin.get(NUMBER_SELECTOR_DECREASE_KEY, javaClass<TextureRegion>()))
            numberDecreaseDrawable.setLeftWidth(0f)
            numberDecreaseDrawable.setRightWidth(0f)
            numberDecreaseDrawable.setTopHeight(0f)
            numberDecreaseDrawable.setBottomHeight(0f)
            skin.add(NUMBER_SELECTOR_DECREASE_KEY, numberDecreaseDrawable)

            skin.add(DIALOG_BACKGROUND_KEY, skin.newDrawable(TextureRegionDrawable(skin.get(DIALOG_BACKGROUND_KEY, javaClass<TextureRegion>())), Color(1.0f, 1.0f, 1.0f, 1.0f)))
            skin.add(POPUP_BACKGROUND_KEY, skin.newDrawable(TextureRegionDrawable(skin.get(POPUP_BACKGROUND_KEY, javaClass<TextureRegion>())), Color(1.0f, 1.0f, 1.0f, 1.0f)))
            skin.add(LIST_BACKGROUND_KEY, skin.newDrawable(TextureRegionDrawable(skin.get(LIST_BACKGROUND_KEY, javaClass<TextureRegion>())), Color(1.0f, 1.0f, 1.0f, 1.0f)))
            skin.add(LIST_SELECTION_KEY, skin.newDrawable(TextureRegionDrawable(skin.get(LIST_SELECTION_KEY, javaClass<TextureRegion>())), Color(1.0f, 1.0f, 1.0f, 1.0f)))
            skin.add(CREDITS_BACKGROUND_KEY, skin.newDrawable(TextureRegionDrawable(skin.get(CREDITS_BACKGROUND_KEY, javaClass<TextureRegion>())), Color(1.0f, 1.0f, 1.0f, 1.0f)))

            val dropdownListBackground = skin.newDrawable(TextureRegionDrawable(skin.get(DROP_DOWN_LIST_BACKGROUND, javaClass<TextureRegion>())), Color(1f, 1f, 1f, 1f))
            dropdownListBackground.setLeftWidth(28f)
            dropdownListBackground.setRightWidth(28f)
            dropdownListBackground.setTopHeight(0f)
            dropdownListBackground.setBottomHeight(0f)
            skin.add(DROP_DOWN_LIST_BACKGROUND, dropdownListBackground)

            // Create a Label style for the title
            val titleStyle = Label.LabelStyle()
            titleStyle.background = transparentDrawable
            titleStyle.font = skin.getFont(TITLE_FONT_KEY)
            titleStyle.fontColor = Color(Color.BLACK)
            skin.add(DEFAULT_KEY, titleStyle)

            // Fancy Character Label Style
            val fancyTitleStyle = Label.LabelStyle()
            fancyTitleStyle.background = transparentDrawable
            fancyTitleStyle.font = skin.getFont(TITLE_FANCY_FONT_KEY)
            fancyTitleStyle.fontColor = Color(Color.BLACK)
            skin.add(FANCY_KEY, fancyTitleStyle)

            // Create a Label style for dialogs
            val infoStyle = LabelStyle()
            infoStyle.background = transparentDrawable
            infoStyle.font = skin.getFont(INFO_LABEL_FONT_KEY)
            infoStyle.fontColor = Color(Color.BLACK)
            skin.add(INFO_STYLE_KEY, infoStyle)

            // Default Button Style
            val textButtonStyle = TextButton.TextButtonStyle()
            textButtonStyle.up = textButtonBackground
            textButtonStyle.down = textButtonBackgroundDown
            textButtonStyle.checked = textButtonBackground
            textButtonStyle.over = textButtonBackgroundDown
            textButtonStyle.disabled = textButtonBackground
            textButtonStyle.font = skin.getFont(BUTTON_FONT_KEY)
            textButtonStyle.fontColor = textButtonFontColor
            textButtonStyle.disabledFontColor = Color(0.3f, 0.3f, 0.3f, 1.0f)
            //        textButtonStyle.pressedOffsetX = 2f;
            //        textButtonStyle.pressedOffsetY = -3f;
            skin.add(DEFAULT_KEY, textButtonStyle)

            // Tab Button Style
            val tabButtonStyle = TextButtonStyle()
            tabButtonStyle.up = tabButtonBackground
            tabButtonStyle.down = tabButtonBackground
            tabButtonStyle.checked = tabButtonBackgroundSelected
            tabButtonStyle.over = tabButtonBackground
            tabButtonStyle.disabled = tabButtonBackground
            tabButtonStyle.font = skin.getFont(BUTTON_FONT_KEY)
            tabButtonStyle.fontColor = textButtonFontColor
            tabButtonStyle.overFontColor = textButtonFontColor
            tabButtonStyle.disabledFontColor = Color(Color.GRAY)
            skin.add(TAB_STYLE_KEY, tabButtonStyle)

            // Create a TextField style
            val textFieldStyle = TextFieldStyle()
            textFieldStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, Color(0f, 0f, 0f, 0.1f))
            textFieldStyle.selection = skin.newDrawable(SOLID_TEXTURE_KEY, Color(0f, 0f, 1f, 0.3f))
            textFieldStyle.cursor = skin.newDrawable(SOLID_TEXTURE_KEY, Color.BLACK)
            textFieldStyle.font = skin.getFont(TEXT_FIELD_FONT_KEY)
            textFieldStyle.fontColor = Color.BLACK
            skin.add(DEFAULT_KEY, textFieldStyle)

            // Create a List style
            val listStyle = ListStyle()
            listStyle.font = skin.getFont(LIST_FONT_KEY)
            listStyle.fontColorSelected = Color.BLACK
            listStyle.fontColorUnselected = Color.BLACK
            listStyle.selection = listSelection
            listStyle.background = transparentDrawable
            skin.add(DEFAULT_KEY, listStyle)

            // Create a Scroll Pane Style
            val scrollPaneStyle = ScrollPaneStyle()
            scrollPaneStyle.background = transparentDrawable
            //        scrollPaneStyle.vScroll = skin.newDrawable(MainMenuScreen.SCROLL_BAR_VERTICAL_KEY);
            //        scrollPaneStyle.vScrollKnob = skin.newDrawable(MainMenuScreen.SCROLL_BAR_VERTICAL_KNOB_KEY);
            skin.add(DEFAULT_KEY, scrollPaneStyle)

            // Create a Dialog Style
            val dialogStyle = WindowStyle()
            dialogStyle.background = SpriteDrawable(Sprite(EuropaGame.game.assetManager!!.get(BACKGROUND_FILE_NAME, javaClass<Texture>())))
            dialogStyle.titleFont = skin.getFont(TITLE_FONT_KEY)
            dialogStyle.titleFontColor = Color(Color.BLACK)
            skin.add(DEFAULT_KEY, dialogStyle)

            // Popup Dialog Style
            val popupStyle = WindowStyle()
            popupStyle.titleFont = skin.getFont(TITLE_FONT_KEY)
            popupStyle.titleFontColor = Color(Color.BLACK)
            skin.add(POPUP_DIALOG_STYLE_KEY, popupStyle)

            // Create a Slider Skin
            val sliderStyle = SliderStyle()
            sliderStyle.background = TextureRegionDrawable(skin.get(SLIDER_BACKGROUND_KEY, javaClass<TextureRegion>()))
            sliderStyle.knob = TextureRegionDrawable(skin.get(SLIDER_KNOB_KEY, javaClass<TextureRegion>()))
            skin.add(DEFAULT_KEY, sliderStyle)

            // Create a Drop Down Menu Skin
            val selectBoxStyle = SelectBoxStyle()
            selectBoxStyle.background = textButtonBackground
            selectBoxStyle.backgroundOpen = textButtonBackgroundDown
            selectBoxStyle.backgroundOver = textButtonBackgroundDown
            selectBoxStyle.scrollStyle = scrollPaneStyle
            selectBoxStyle.font = skin.getFont(TEXT_FIELD_FONT_KEY)
            selectBoxStyle.fontColor = textButtonFontColor
            val selectBoxListStyle = ListStyle()
            selectBoxListStyle.font = skin.getFont(LIST_FONT_KEY)
            selectBoxListStyle.fontColorSelected = textButtonFontColor
            selectBoxListStyle.fontColorUnselected = textButtonFontColor
            selectBoxListStyle.selection = skin.newDrawable(SOLID_TEXTURE_KEY, SELECTION_COLOR)
            selectBoxListStyle.background = dropdownListBackground
            selectBoxStyle.listStyle = selectBoxListStyle
            skin.add(DEFAULT_KEY, selectBoxStyle)

            // NumberSelectorStyle
            val numberStyle = NumberSelectorStyle()
            numberStyle.decrease = numberDecreaseDrawable
            numberStyle.increase = numberIncreaseDrawable
            numberStyle.minimumNumberSize = 50
            numberStyle.numberLabelStyle = infoStyle
            numberStyle.spacing = COMPONENT_SPACING
            skin.add(DEFAULT_KEY, numberStyle)

            // AttributeSelectorStyle
            val attrStyle = MultipleNumberSelector.MultipleNumberSelectorStyle()
            attrStyle.labelStyle = infoStyle
            attrStyle.numberSelectorStyle = numberStyle
            attrStyle.spacing = COMPONENT_SPACING
            skin.add(DEFAULT_KEY, attrStyle)

            // TraitPoolSelectorStyle
            val tpsStyle = TraitPoolSelectorStyle()
            tpsStyle.add = numberIncreaseDrawable
            tpsStyle.remove = numberDecreaseDrawable
            tpsStyle.spacing = MainMenuScreen.COMPONENT_SPACING
            tpsStyle.labelStyle = infoStyle
            tpsStyle.scrollPaneStyle = scrollPaneStyle
            tpsStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, Color(0.4f, 0.4f, 0.4f, 0.2f))
            tpsStyle.selectedBackground = skin.newDrawable(SOLID_TEXTURE_KEY, SELECTION_COLOR)
            skin.add(DEFAULT_KEY, tpsStyle)

            mainMenuSkin = skin
        }
    }

}
