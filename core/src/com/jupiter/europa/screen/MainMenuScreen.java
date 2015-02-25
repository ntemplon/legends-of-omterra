/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
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
 */
package com.jupiter.europa.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jupiter.europa.EuropaGame;
import static com.jupiter.europa.audio.AudioService.TITLE_MUSIC;
import com.jupiter.europa.entity.EuropaEntity;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.geometry.Size;
import com.jupiter.europa.io.FileLocations;
import static com.jupiter.europa.io.FileLocations.SKINS_DIRECTORY;
import com.jupiter.europa.save.SaveGame;
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector;
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector.AttributeSelectorStyle;
import com.jupiter.europa.scene2d.ui.NumberSelector.NumberSelectorStyle;
import com.jupiter.europa.scene2d.ui.ObservableDialog.DialogEventArgs;
import com.jupiter.europa.scene2d.ui.ObservableDialog.DialogEvents;
import com.jupiter.europa.screen.dialog.CreateCharacterDialog;
import com.jupiter.europa.screen.dialog.CreateCharacterDialog.CreateCharacterExitStates;
import com.jupiter.europa.screen.dialog.CreditsDialog;
import com.jupiter.europa.screen.dialog.LoadGameDialog;
import com.jupiter.europa.screen.dialog.NewGameDialog;
import com.jupiter.europa.screen.dialog.NewGameDialog.NewGameExitStates;
import com.jupiter.europa.screen.dialog.OptionsDialog;
import com.jupiter.europa.world.World;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Nathan Templon
 */
public class MainMenuScreen implements Screen, InputProcessor {
    
    // Enumerations
    public enum DialogExitStates {
        NEXT,
        BACK
    }
    

    // Constants
    public static final Path MAIN_MENU_SKIN_DIRECTORY = SKINS_DIRECTORY.resolve("main_menu");

    public static final String TITLE_FANCY_FONT = "Diploma56-bold.fnt";
    public static final String TITLE_FONT = "MagicMedieval48-bold.fnt";
    public static final String BUTTON_FONT = "MagicMedieval40.fnt";
    public static final String LIST_FONT = "MagicMedieval32.fnt";
    public static final String TEXT_FIELD_FONT = "MagicMedieval32.fnt";
    public static final String INFO_LABEL_FONT = "MagicMedieval24.fnt";

    public static final String DEFAULT_KEY = "default";
    public static final String FANCY_KEY = "fancy";
    public static final String INFO_STYLE_KEY = "info";
    public static final String TAB_STYLE_KEY = "tab-style";
    public static final String POPUP_DIALOG_STYLE_KEY = "popup-dialog-style";

    public static final String BACKGROUND_FILE_NAME = FileLocations.UI_IMAGES_DIRECTORY.resolve("main_menu_background.png").toString();
    public static final String ATLAS_KEY = "main_menu.atlas";
    public static final String SOLID_TEXTURE_KEY = "solid-texture";
    public static final String DIALOG_BACKGROUND_KEY = "dialog-border";
    public static final String POPUP_BACKGROUND_KEY = "popup-border";
    public static final String TITLE_BACKGROUND_KEY = "title-border";
    public static final String BUTTON_BACKGROUND_KEY = "button-background";
    public static final String BUTTON_DOWN_KEY = "button-background-down";
    public static final String SLIDER_BACKGROUND_KEY = "slider-background-main_menu";
    public static final String LIST_BACKGROUND_KEY = "list-background";
    public static final String LIST_SELECTION_KEY = "list-selection";
    public static final String SLIDER_KNOB_KEY = "slider-knob-main_menu";
    public static final String TITLE_FONT_KEY = "title-font";
    public static final String TITLE_FANCY_FONT_KEY = "title-font-fancy";
    public static final String BUTTON_FONT_KEY = "button-font";
    public static final String TEXT_FIELD_FONT_KEY = "text-field-font";
    public static final String LIST_FONT_KEY = "list-font";
    public static final String INFO_LABEL_FONT_KEY = "info-label-font";
    public static final String SCROLL_BAR_VERTICAL_KEY = "scroll-bar-vertical";
    public static final String SCROLL_BAR_VERTICAL_KNOB_KEY = "scroll-bar-vertical-knob";
    public static final String DROP_DOWN_LIST_BACKGROUND = "drop-down-list-background";
    public static final String CREDITS_BACKGROUND_KEY = "credits-background";
    public static final String TAB_BUTTON_BACKGROUND_KEY = "tab-button-background";
    public static final String TAB_BUTTON_SELECTED_KEY = "tab-button-background-selected";
    public static final String NUMBER_SELECTOR_INCREASE_KEY = "number-increase";
    public static final String NUMBER_SELECTOR_DECREASE_KEY = "number-decrease";

    public static Skin mainMenuSkin;

    public static final int COMPONENT_SPACING = 4;
    public static final int TITLE_BUTTON_WIDTH = 250;
    public static final int DIALOG_BUTTON_WIDTH = 190;
    public static final int TABLE_PADDING = 14;
    public static final int LIST_WRAPPER_PADDING = 20;

    public static final int DIALOG_WIDTH = 640;

    public static final Color BACKGROUND_COLOR = new Color(Color.WHITE);
    public static final Color SELECTION_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.2f);
    public static final Color TRANSPARENT = new Color(1, 1, 1, 0);


    // Static Methods
    public static Skin getMainMenuSkin() {
        if (mainMenuSkin == null) {
            buildMainMenuSkin();
        }
        return mainMenuSkin;
    }

    private static void buildMainMenuSkin() {
        Skin skin = new Skin();

        // Fonts
        skin.add(BUTTON_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(BUTTON_FONT).toString()));
        skin.add(TITLE_FANCY_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(TITLE_FANCY_FONT).toString()));
        skin.add(TITLE_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(TITLE_FONT).toString()));
        skin.add(LIST_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(LIST_FONT).toString()));
        skin.add(TEXT_FIELD_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(TEXT_FIELD_FONT).toString()));
        skin.add(INFO_LABEL_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(INFO_LABEL_FONT).toString()));

        // Set the background texture
        Pixmap pixmap = new Pixmap(1, (int) 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add(SOLID_TEXTURE_KEY, new Texture(pixmap));
        Drawable transparentDrawable = skin.newDrawable(SOLID_TEXTURE_KEY, TRANSPARENT);

        // Get values from the atlas
        skin.addRegions(EuropaGame.game.getAssetManager().get(MAIN_MENU_SKIN_DIRECTORY.resolve(ATLAS_KEY).toString()));
        
        // Colors
        Color textButtonFontColor = new Color(0.85f, 0.85f, 0.85f, 1.0f);

        // Set images
        Drawable textButtonBackground = new TextureRegionDrawable(skin.get(BUTTON_BACKGROUND_KEY, TextureRegion.class));
        textButtonBackground.setLeftWidth(32);
        textButtonBackground.setRightWidth(32);
        textButtonBackground.setTopHeight(5);
        textButtonBackground.setBottomHeight(5);
        skin.add(BUTTON_BACKGROUND_KEY, textButtonBackground);
        
        Drawable textButtonBackgroundDown = new TextureRegionDrawable(skin.get(BUTTON_DOWN_KEY, TextureRegion.class));
        textButtonBackgroundDown.setLeftWidth(32);
        textButtonBackgroundDown.setRightWidth(32);
        textButtonBackgroundDown.setTopHeight(5);
        textButtonBackgroundDown.setBottomHeight(5);
        skin.add(BUTTON_DOWN_KEY, textButtonBackgroundDown);
        
        Drawable listSelection = new TextureRegionDrawable(skin.get(LIST_SELECTION_KEY, TextureRegion.class));
        listSelection.setLeftWidth(7);
        listSelection.setRightWidth(7);
        listSelection.setTopHeight(0);
        listSelection.setBottomHeight(0);
        skin.add(LIST_SELECTION_KEY, listSelection);
        
        Drawable tabButtonBackground = new TextureRegionDrawable(skin.get(TAB_BUTTON_BACKGROUND_KEY, TextureRegion.class));
        tabButtonBackground.setLeftWidth(5);
        tabButtonBackground.setRightWidth(5);
        tabButtonBackground.setTopHeight(0);
        tabButtonBackground.setBottomHeight(0);
        skin.add(TAB_BUTTON_BACKGROUND_KEY, tabButtonBackground);
        
        Drawable tabButtonBackgroundSelected = new TextureRegionDrawable(skin.get(TAB_BUTTON_SELECTED_KEY, TextureRegion.class));
        tabButtonBackgroundSelected.setLeftWidth(5);
        tabButtonBackgroundSelected.setRightWidth(5);
        tabButtonBackgroundSelected.setTopHeight(0);
        tabButtonBackgroundSelected.setBottomHeight(0);
        skin.add(TAB_BUTTON_SELECTED_KEY, tabButtonBackgroundSelected);
        
        Drawable titleBackground = new TextureRegionDrawable(skin.get(TITLE_BACKGROUND_KEY, TextureRegion.class));
        titleBackground.setLeftWidth(10);
        titleBackground.setRightWidth(10);
        titleBackground.setTopHeight(0);
        titleBackground.setBottomHeight(0);
        skin.add(TITLE_BACKGROUND_KEY, titleBackground);
        
        Drawable numberIncreaseDrawable = new TextureRegionDrawable(skin.get(NUMBER_SELECTOR_INCREASE_KEY, TextureRegion.class));
        numberIncreaseDrawable.setLeftWidth(0);
        numberIncreaseDrawable.setRightWidth(0);
        numberIncreaseDrawable.setTopHeight(0);
        numberIncreaseDrawable.setBottomHeight(0);
        skin.add(NUMBER_SELECTOR_INCREASE_KEY, numberIncreaseDrawable);
        
        Drawable numberDecreaseDrawable = new TextureRegionDrawable(skin.get(NUMBER_SELECTOR_DECREASE_KEY, TextureRegion.class));
        numberDecreaseDrawable.setLeftWidth(0);
        numberDecreaseDrawable.setRightWidth(0);
        numberDecreaseDrawable.setTopHeight(0);
        numberDecreaseDrawable.setBottomHeight(0);
        skin.add(NUMBER_SELECTOR_DECREASE_KEY, numberDecreaseDrawable);
        
        skin.add(DIALOG_BACKGROUND_KEY, skin.newDrawable(new TextureRegionDrawable(skin.get(DIALOG_BACKGROUND_KEY, TextureRegion.class)), new Color(1.0f, 1.0f,
                1.0f, 1.0f)));
        skin.add(POPUP_BACKGROUND_KEY, skin.newDrawable(new TextureRegionDrawable(skin.get(POPUP_BACKGROUND_KEY, TextureRegion.class)), new Color(1.0f, 1.0f,
                1.0f, 1.0f)));
        skin.add(LIST_BACKGROUND_KEY, skin.newDrawable(new TextureRegionDrawable(skin.get(LIST_BACKGROUND_KEY, TextureRegion.class)),
                new Color(1.0f, 1.0f, 1.0f, 1.0f)));
        skin.add(LIST_SELECTION_KEY, skin.newDrawable(new TextureRegionDrawable(skin.get(LIST_SELECTION_KEY, TextureRegion.class)), new Color(1.0f, 1.0f, 1.0f,
                1.0f)));
        skin.add(CREDITS_BACKGROUND_KEY, skin.newDrawable(new TextureRegionDrawable(skin.get(CREDITS_BACKGROUND_KEY, TextureRegion.class)),
                new Color(1.0f, 1.0f, 1.0f, 1.0f)));
        
        Drawable dropdownListBackground = skin.newDrawable(new TextureRegionDrawable(skin.get(DROP_DOWN_LIST_BACKGROUND, TextureRegion.class)), new Color(1, 1, 1, 1));
        dropdownListBackground.setLeftWidth(28);
        dropdownListBackground.setRightWidth(28);
        dropdownListBackground.setTopHeight(0);
        dropdownListBackground.setBottomHeight(0);
        skin.add(DROP_DOWN_LIST_BACKGROUND, dropdownListBackground);

        // Create a Label style for the title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.background = transparentDrawable;
        titleStyle.font = skin.getFont(TITLE_FONT_KEY);
        titleStyle.fontColor = new Color(Color.BLACK);
        skin.add(DEFAULT_KEY, titleStyle);
        
        // Fancy Character Label Style
        Label.LabelStyle fancyTitleStyle = new Label.LabelStyle();
        fancyTitleStyle.background = transparentDrawable;
        fancyTitleStyle.font = skin.getFont(TITLE_FANCY_FONT_KEY);
        fancyTitleStyle.fontColor = new Color(Color.BLACK);
        skin.add(FANCY_KEY, fancyTitleStyle);

        // Create a Label style for dialogs
        LabelStyle infoStyle = new LabelStyle();
        infoStyle.background = transparentDrawable;
        infoStyle.font = skin.getFont(INFO_LABEL_FONT_KEY);
        infoStyle.fontColor = new Color(Color.BLACK);
        skin.add(INFO_STYLE_KEY, infoStyle);

        // Default Button Style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = textButtonBackground;
        textButtonStyle.down = textButtonBackgroundDown;
        textButtonStyle.checked = textButtonBackground;
        textButtonStyle.over = textButtonBackgroundDown;
        textButtonStyle.disabled = textButtonBackground;
        textButtonStyle.font = skin.getFont(BUTTON_FONT_KEY);
        textButtonStyle.fontColor = textButtonFontColor;
        textButtonStyle.disabledFontColor = new Color(0.3f, 0.3f, 0.3f, 1.0f);
//        textButtonStyle.pressedOffsetX = 2f;
//        textButtonStyle.pressedOffsetY = -3f;
        skin.add(DEFAULT_KEY, textButtonStyle);
        
        // Tab Button Style
        TextButtonStyle tabButtonStyle = new TextButtonStyle();
        tabButtonStyle.up = tabButtonBackground;
        tabButtonStyle.down = tabButtonBackground;
        tabButtonStyle.checked = tabButtonBackgroundSelected;
        tabButtonStyle.over = tabButtonBackground;
        tabButtonStyle.disabled = tabButtonBackground;
        tabButtonStyle.font = skin.getFont(BUTTON_FONT_KEY);
        tabButtonStyle.fontColor = textButtonFontColor;
        tabButtonStyle.overFontColor = textButtonFontColor;
        tabButtonStyle.disabledFontColor = new Color(Color.GRAY);
        skin.add(TAB_STYLE_KEY, tabButtonStyle);

        // Create a TextField style
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0f, 0f, 0f, 0.1f));
        textFieldStyle.selection = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0f, 0f, 1f, 0.3f));
        textFieldStyle.cursor = skin.newDrawable(SOLID_TEXTURE_KEY, Color.BLACK);
        textFieldStyle.font = skin.getFont(TEXT_FIELD_FONT_KEY);
        textFieldStyle.fontColor = Color.BLACK;
        skin.add(DEFAULT_KEY, textFieldStyle);

        // Create a List style
        ListStyle listStyle = new ListStyle();
        listStyle.font = skin.getFont(LIST_FONT_KEY);
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.selection = listSelection;
        listStyle.background = transparentDrawable;
        skin.add(DEFAULT_KEY, listStyle);

        // Create a Scroll Pane Style
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.background = transparentDrawable;
//        scrollPaneStyle.vScroll = skin.newDrawable(MainMenuScreen.SCROLL_BAR_VERTICAL_KEY);
//        scrollPaneStyle.vScrollKnob = skin.newDrawable(MainMenuScreen.SCROLL_BAR_VERTICAL_KNOB_KEY);
        skin.add(DEFAULT_KEY, scrollPaneStyle);

        // Create a Dialog Style
        WindowStyle dialogStyle = new WindowStyle();
        dialogStyle.background = new SpriteDrawable(new Sprite(EuropaGame.game.getAssetManager().get(BACKGROUND_FILE_NAME, Texture.class)));
        dialogStyle.titleFont = skin.getFont(TITLE_FONT_KEY);
        dialogStyle.titleFontColor = new Color(Color.BLACK);
        skin.add(DEFAULT_KEY, dialogStyle);
        
        // Popup Dialog Style
        WindowStyle popupStyle = new WindowStyle();
        popupStyle.titleFont = skin.getFont(TITLE_FONT_KEY);
        popupStyle.titleFontColor = new Color(Color.BLACK);
        skin.add(POPUP_DIALOG_STYLE_KEY, popupStyle);

        // Create a Slider Skin
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(skin.get(SLIDER_BACKGROUND_KEY, TextureRegion.class));
        sliderStyle.knob = new TextureRegionDrawable(skin.get(SLIDER_KNOB_KEY, TextureRegion.class));
        skin.add(DEFAULT_KEY, sliderStyle);

        // Create a Drop Down Menu Skin
        SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
        selectBoxStyle.background = textButtonBackground;
        selectBoxStyle.backgroundOpen = textButtonBackgroundDown;
        selectBoxStyle.backgroundOver = textButtonBackgroundDown;
        selectBoxStyle.scrollStyle = scrollPaneStyle;
        selectBoxStyle.font = skin.getFont(TEXT_FIELD_FONT_KEY);
        selectBoxStyle.fontColor = textButtonFontColor;
        ListStyle selectBoxListStyle = new ListStyle();
        selectBoxListStyle.font = skin.getFont(LIST_FONT_KEY);
        selectBoxListStyle.fontColorSelected = textButtonFontColor;
        selectBoxListStyle.fontColorUnselected = textButtonFontColor;
        selectBoxListStyle.selection = skin.newDrawable(SOLID_TEXTURE_KEY, SELECTION_COLOR);
        selectBoxListStyle.background = dropdownListBackground;
        selectBoxStyle.listStyle = selectBoxListStyle;
        skin.add(DEFAULT_KEY, selectBoxStyle);
        
        // NumberSelectorStyle
        NumberSelectorStyle numberStyle = new NumberSelectorStyle();
        numberStyle.decrease = numberDecreaseDrawable;
        numberStyle.increase = numberIncreaseDrawable;
        numberStyle.minimumNumberSize = 50;
        numberStyle.numberLabelStyle = infoStyle;
        numberStyle.spacing = COMPONENT_SPACING;
        skin.add(DEFAULT_KEY, numberStyle);
        
        // AttributeSelectorStyle
        AttributeSelectorStyle attrStyle = new MultipleNumberSelector.AttributeSelectorStyle();
        attrStyle.labelStyle = infoStyle;
        attrStyle.numberSelectorStyle = numberStyle;
        attrStyle.spacing = COMPONENT_SPACING;
        skin.add(DEFAULT_KEY, attrStyle);

        mainMenuSkin = skin;
    }


    // Fields
    private Stage stage;
    private Image background;

    private Table titleTable;
    private Table buttonTable;
    private Table titleWrapperTable;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton multiplayerButton;
    private TextButton optionsButton;
    private TextButton creditsButton;
    private TextButton quitButton;

    private CreateCharacterDialog createCharacterDialog;
    private NewGameDialog newGameDialog;
    private LoadGameDialog loadGameDialog;
    private CreditsDialog creditsDialog;
    private OptionsDialog optionsDialog;

    private Size size = new Size(0, 0);


    // Initialization
    public MainMenuScreen() {

    }


    // Screen Implementation
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.size = new Size(width, height);

        // True puts 0, 0 at the bottom left corner, false or omission puts 0, 0 at the center
        this.stage.getViewport().update(width, height, true);

        // Resize dialogs
        if (this.createCharacterDialog != null) {
            this.createCharacterDialog.setSize(width, height);
        }
        if (this.newGameDialog != null) {
            this.newGameDialog.setSize(width, height);
        }
        if (this.loadGameDialog != null) {
            this.loadGameDialog.setSize(width, height);
        }
        if (this.creditsDialog != null) {
            this.creditsDialog.setSize(width, height);
        }
        if (this.optionsDialog != null) {
            this.optionsDialog.setSize(width, height);
        }
    }

    @Override
    public void show() {
        EuropaGame.game.inspectSaves();
        this.init();

        // Play Music
        EuropaGame.game.getAudioService().playMusic(TITLE_MUSIC);

        // Create Screens
        this.optionsDialog = new OptionsDialog();
        this.creditsDialog = new CreditsDialog();
    }

    @Override
    public void hide() {
        EuropaGame.game.getAudioService().stop();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        if (this.stage != null) {
            this.stage.dispose();
        }
    }


    // InputProcessor Implementation
    @Override
    public boolean keyDown(int i) {
        return this.stage.keyDown(i);
    }

    @Override
    public boolean keyUp(int i) {
        return this.stage.keyUp(i);
    }

    @Override
    public boolean keyTyped(char c) {
        return this.stage.keyTyped(c);
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return this.stage.touchDown(i, i1, i2, i3);
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return this.stage.touchUp(i, i1, i2, i3);
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return this.stage.touchDragged(i, i1, i2);
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return this.stage.mouseMoved(i, i1);
    }

    @Override
    public boolean scrolled(int i) {
        return this.stage.scrolled(i);
    }


    // Private Methods
    private void init() {
        this.stage = new Stage(new ScreenViewport());
        Skin skin = getMainMenuSkin();

        // Background
        this.background = new Image(EuropaGame.game.getAssetManager().get(BACKGROUND_FILE_NAME, Texture.class));
        this.background.setFillParent(true);
        this.stage.addActor(this.background);

        // Create Buttons
        this.buttonTable = new Table();
        this.buttonTable.setFillParent(false);
        this.buttonTable.center();

        this.newGameButton = new TextButton("New Game", skin.get(DEFAULT_KEY, TextButtonStyle.class)); // Use the initialized skin
        this.newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.newGameButton.isDisabled()) {
                    MainMenuScreen.this.onNewGameClick();
                }
                MainMenuScreen.this.newGameButton.setChecked(false);
            }
        });

        this.loadGameButton = new TextButton("Load Game", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.loadGameButton.isDisabled()) {
                    MainMenuScreen.this.onLoadGameClick();
                }
                MainMenuScreen.this.loadGameButton.setChecked(false);
            }
        });
        this.loadGameButton.setDisabled(EuropaGame.game.getSaveNames().length == 0);

        this.multiplayerButton = new TextButton("Multiplayer", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.multiplayerButton.isDisabled()) {
                    MainMenuScreen.this.onMultiplayerClick();
                }
                MainMenuScreen.this.multiplayerButton.setChecked(false);
            }
        });
        this.multiplayerButton.setDisabled(true);

        this.optionsButton = new TextButton("Options", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.optionsButton.isDisabled()) {
                    MainMenuScreen.this.onOptionsClick();
                }
                MainMenuScreen.this.optionsButton.setChecked(false);
            }
        });

        this.creditsButton = new TextButton("Credits", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.creditsButton.isDisabled()) {
                    MainMenuScreen.this.onCreditsClick();
                }
                MainMenuScreen.this.creditsButton.setChecked(false);
            }
        });

        this.quitButton = new TextButton("Exit", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.quitButton.isDisabled()) {
                    MainMenuScreen.this.onQuitClick();
                }
                MainMenuScreen.this.quitButton.setChecked(false);
            }
        });

        // Configure Button Table
        this.buttonTable.add(this.newGameButton).width(TITLE_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.loadGameButton).width(TITLE_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.multiplayerButton).width(TITLE_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.optionsButton).width(TITLE_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.creditsButton).width(TITLE_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.quitButton).width(TITLE_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING);
        this.buttonTable.row();

        // Title
        this.titleTable = new Table();
        this.titleTable.setFillParent(true);
        this.titleTable.center();

        this.titleWrapperTable = new Table();
        String[] titleWords = EuropaGame.TITLE.split("\\s+");
        this.titleWrapperTable.add(new Label(titleWords[0].substring(0, 1), skin.get(FANCY_KEY, LabelStyle.class))).right().expandX();
        this.titleWrapperTable.add(new Label(titleWords[0].substring(1), skin.get(DEFAULT_KEY, LabelStyle.class)));
        for (int i = 1; i < titleWords.length - 1; i++) {
            String text = titleWords[i];
            if (i == 1) {
                text = " " + text;
            }
            if (i == titleWords.length - 1) {
                text += " ";
            }
            this.titleWrapperTable.add(new Label(text, skin.get(DEFAULT_KEY, LabelStyle.class)));
        }
        this.titleWrapperTable.add(new Label(titleWords[titleWords.length - 1].substring(0, 1), skin.get(FANCY_KEY, LabelStyle.class)));
        this.titleWrapperTable.add(new Label(titleWords[titleWords.length - 1].substring(1), skin.get(DEFAULT_KEY, LabelStyle.class))).left().expandX();
        
        this.titleWrapperTable.background(skin.get(TITLE_BACKGROUND_KEY, TextureRegionDrawable.class));

        this.titleTable.add(this.titleWrapperTable).pad(COMPONENT_SPACING).top();
        this.titleTable.row();
        this.titleTable.add(this.buttonTable).center().expandY();
        this.titleTable.row();

        this.stage.addActor(this.titleTable);
    }

    private void onNewGameClick() {
        this.createCharacterDialog = new CreateCharacterDialog();
        this.createCharacterDialog.addDialogListener(this::onCharacterCreationCompleted, DialogEvents.HIDDEN);
        this.showDialog(this.createCharacterDialog);
    }

    private void onLoadGameClick() {
        this.loadGameDialog = new LoadGameDialog();
        this.loadGameDialog.addDialogListener(this::onLoadGameDialogHidden, DialogEvents.HIDDEN);
        this.showDialog(this.loadGameDialog);
    }

    private void onMultiplayerClick() {
        System.out.println("Multiplayer is not yet implemented!");
    }

    private void onOptionsClick() {
        this.showDialog(this.optionsDialog);
    }

    private void onCreditsClick() {
        this.showDialog(this.creditsDialog);
    }

    private void onQuitClick() {
        Gdx.app.exit();
    }

    private void onCharacterCreationCompleted(DialogEventArgs args) {
        if (this.createCharacterDialog.getExitState().equals(CreateCharacterExitStates.OK)) {
            this.newGameDialog = new NewGameDialog();
            this.newGameDialog.addDialogListener(this::onNewGameDialogHidden, DialogEvents.HIDDEN);
            this.showDialog(this.newGameDialog);
        }
    }

    private void onNewGameDialogHidden(DialogEventArgs args) {
        if (this.newGameDialog.getExitState().equals(NewGameExitStates.START_GAME)) {
            this.startNewGame();
        }
        else {
            this.showDialog(this.createCharacterDialog);
        }
    }

    private void onLoadGameDialogHidden(DialogEventArgs args) {
        if (this.loadGameDialog.getExitState() == LoadGameDialog.LoadGameExitStates.LOAD) {
            this.loadGame();
        }
    }

    private void startNewGame() {
        String gameName = this.newGameDialog.getNewGameName();
        if (gameName == null || gameName.isEmpty()) {
            gameName = "default";
        }

        World world = EuropaGame.game.getWorld(this.newGameDialog.getNewGameWorldName());

        Party party = new Party();

        // Get Entities here
        EuropaEntity entity = this.createCharacterDialog.getCreatedEntity();
        party.addPlayer(entity);
        party.selectPlayer(entity);

        EuropaGame.game.startGame(gameName, world, party);
    }

    private void loadGame() {
        Path saveFile = FileLocations.SAVE_DIRECTORY.resolve(this.loadGameDialog.getGameToLoad() + "." + SaveGame.SAVE_EXTENSION);
        if (!Files.exists(saveFile)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(saveFile)) {
            Json json = new Json();
            JsonValue value = new JsonReader().parse(reader);
            SaveGame save = json.fromJson(SaveGame.class, value.toString());
            EuropaGame.game.startGame(save);
        }
        catch (IOException ex) {

        }
    }

    private void showDialog(Dialog dialog) {
        dialog.show(this.stage).setSize(this.size.width, this.size.height);
    }

}
