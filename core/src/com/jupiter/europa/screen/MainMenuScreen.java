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
import com.badlogic.gdx.Input.Buttons;
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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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
import com.jupiter.europa.scene2d.ui.ObservableDialog.DialogEventArgs;
import com.jupiter.europa.scene2d.ui.ObservableDialog.DialogEvents;
import com.jupiter.europa.scene2d.ui.TabbedPane;
import com.jupiter.europa.screen.dialog.CreateCharacterDialog;
import com.jupiter.europa.screen.dialog.CreateCharacterDialog.CreateCharacterExitStates;
import com.jupiter.europa.screen.dialog.NewGameDialog;
import com.jupiter.europa.screen.dialog.NewGameDialog.NewGameExitStates;
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

    // Constants
    public static final Path MAIN_MENU_SKIN_DIRECTORY = SKINS_DIRECTORY.resolve("main_menu");

    public static final String TITLE_FONT = "MagicMedieval48-bold.fnt";
    public static final String BUTTON_FONT = "MagicMedieval40.fnt";
    public static final String LIST_FONT = "MagicMedieval32.fnt";
    public static final String TEXT_FIELD_FONT = "MagicMedieval32.fnt";
    public static final String INFO_LABEL_FONT = "MagicMedieval24.fnt";

    public static final String DEFAULT_KEY = "default";
    public static final String INFO_STYLE_KEY = "info";
    public static final String TAB_STYLE_KEY = "tab-style";

    public static final String BACKGROUND_FILE_NAME = FileLocations.UI_IMAGES_DIRECTORY.resolve("main_menu_background.png").toString();
    public static final String ATLAS_KEY = "main_menu.atlas";
    public static final String SOLID_TEXTURE_KEY = "solid-texture";
    public static final String BUTTON_TABLE_BACKGROUND_KEY = "button-table-background";
    public static final String SLIDER_BACKGROUND_KEY = "slider-background-main_menu";
    public static final String SLIDER_KNOB_KEY = "slider-knob-main_menu";
    public static final String TITLE_FONT_KEY = "title-font";
    public static final String BUTTON_FONT_KEY = "button-font";
    public static final String TEXT_FIELD_FONT_KEY = "text-field-font";
    public static final String LIST_FONT_KEY = "list-font";
    public static final String INFO_LABEL_FONT_KEY = "info-label-font";

    public static Skin mainMenuSkin;

    public static final int TITLE_PADDING = 25;
    public static final int BUTTON_WIDTH = 225;
    public static final int BUTTON_SPACING = 20;
    public static final int TABLE_HORIZONTAL_PADDING = 20;
    
    public static final int DIALOG_MIN_WIDTH = 580;

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

        skin.add(BUTTON_FONT_KEY, EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(BUTTON_FONT).toString()));
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

        // Add textures to skin
        skin.add(BUTTON_TABLE_BACKGROUND_KEY, skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0.6f, 0.6f, 0.6f, 0.75f)));

        // Get values from the atlas
        skin.addRegions(EuropaGame.game.getAssetManager().get(MAIN_MENU_SKIN_DIRECTORY.resolve(ATLAS_KEY).toString()));

        // Create a Label style for the title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.background = transparentDrawable;
        titleStyle.font = skin.getFont(TITLE_FONT_KEY);
        titleStyle.fontColor = new Color(Color.BLACK);
        skin.add(DEFAULT_KEY, titleStyle);

        // Create a Label style for dialogs
        LabelStyle infoStyle = new LabelStyle();
        infoStyle.background = transparentDrawable;
        infoStyle.font = skin.getFont(INFO_LABEL_FONT_KEY);
        infoStyle.fontColor = new Color(Color.BLACK);
        skin.add(INFO_STYLE_KEY, infoStyle);

        // Default Button Style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = transparentDrawable;
        textButtonStyle.down = transparentDrawable;
        textButtonStyle.checked = transparentDrawable;
        textButtonStyle.over = transparentDrawable;
        textButtonStyle.disabled = transparentDrawable;
        textButtonStyle.font = skin.getFont(BUTTON_FONT_KEY);
        textButtonStyle.fontColor = new Color(Color.BLACK);
        textButtonStyle.overFontColor = new Color(Color.BLUE);
        textButtonStyle.disabledFontColor = new Color(0.3f, 0.3f, 0.3f, 1.0f);
        textButtonStyle.pressedOffsetX = 2f;
        textButtonStyle.pressedOffsetY = -3f;
        skin.add(DEFAULT_KEY, textButtonStyle);
//        listStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0f, 0f, 0f, 0.1f));
        // Tab Button Style
        TextButtonStyle tabButtonStyle = new TextButtonStyle();
        tabButtonStyle.up = transparentDrawable;
        tabButtonStyle.down = transparentDrawable;
        tabButtonStyle.checked = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0, 0, 0, 0.2f));
        tabButtonStyle.over = transparentDrawable;
        tabButtonStyle.disabled = transparentDrawable;
        tabButtonStyle.font = skin.getFont(BUTTON_FONT_KEY);
        tabButtonStyle.fontColor = new Color(Color.BLACK);
        tabButtonStyle.overFontColor = new Color(Color.BLUE);
        tabButtonStyle.disabledFontColor = new Color(Color.GRAY);
        tabButtonStyle.pressedOffsetX = 2f;
        tabButtonStyle.pressedOffsetY = -3f;
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
        listStyle.selection = skin.newDrawable(SOLID_TEXTURE_KEY, SELECTION_COLOR);
        listStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(Color.WHITE));
        skin.add(DEFAULT_KEY, listStyle);

        // Create a Scroll Pane Style
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.background = transparentDrawable;
        skin.add(DEFAULT_KEY, scrollPaneStyle);

        // Create a Dialog Style
        WindowStyle dialogStyle = new WindowStyle();
        dialogStyle.background = new SpriteDrawable(new Sprite(EuropaGame.game.getAssetManager().get(BACKGROUND_FILE_NAME, Texture.class)));
        dialogStyle.stageBackground = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(Color.WHITE));
        dialogStyle.titleFont = skin.getFont(TITLE_FONT_KEY);
        dialogStyle.titleFontColor = new Color(Color.BLACK);
        skin.add(DEFAULT_KEY, dialogStyle);

        // Create a Slider Skin
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(skin.get(SLIDER_BACKGROUND_KEY, TextureRegion.class));
        sliderStyle.knob = new TextureRegionDrawable(skin.get(SLIDER_KNOB_KEY, TextureRegion.class));
        skin.add(DEFAULT_KEY, sliderStyle);

        // Create a Drop Down Menu Skin
        SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
        selectBoxStyle.background = transparentDrawable;
        selectBoxStyle.backgroundOpen = transparentDrawable;
        selectBoxStyle.backgroundOver = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0, 0, 0, 0.1f));
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = scrollPaneStyle;
        selectBoxStyle.font = skin.getFont(TEXT_FIELD_FONT_KEY);
        selectBoxStyle.fontColor = new Color(Color.BLACK);
        skin.add(DEFAULT_KEY, selectBoxStyle);

        mainMenuSkin = skin;
    }


    // Fields
    private Stage stage;
    private Image background;

    private Table titleTable;
    private Table buttonTable;
    private Label titleLabel;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton multiplayerButton;
    private TextButton optionsButton;
    private TextButton creditsButton;
    private TextButton quitButton;

    private Dialog loadGameDialog;
    private Table loadGameTable;
    private Label loadGameLabel;
    private List loadGameList;
    private ScrollPane loadGameListPane;
    private Table loadGameButtonTable;
    private TextButton loadGameOkButton;
    private TextButton loadGameCancelButton;
    private TextButton loadGameDeleteButton;

    private Dialog confirmDeleteDialog;
    private Label confirmDeleteLabel;
    private Table confirmDeleteButtonTable;
    private TextButton confirmDeleteYesButton;
    private TextButton confirmDeleteNoButton;

    private Dialog optionsDialog;
    private Table optionsTable;
    private Label optionsLabel;
    private TabbedPane optionsPane;
    private Table audioTable;
    private Slider musicSlider;
    private Table graphicsTable;
    private TextButton optionsAcceptButton;
    private TextButton optionsCancelButton;
    private Table optionsButtonTable;

    private Dialog creditsDialog;
    private ScrollPane creditsScrollPane;
    private Label creditsLabel;
    private TextButton creditsOkButton;

    private CreateCharacterDialog createCharacterDialog;
    private NewGameDialog newGameDialog;
    
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
        this.loadGameDialog.setSize(width, height);
        this.confirmDeleteDialog.setSize(width, height);
        this.optionsDialog.setSize(width, height);
        this.creditsDialog.setSize(width, height);
        
        if (this.createCharacterDialog != null) {
            this.createCharacterDialog.setSize(width, height);
        }
        if (this.newGameDialog != null) {
            this.newGameDialog.setSize(width, height);
        }
    }

    @Override
    public void show() {
        EuropaGame.game.inspectSaves();
        this.init();

        this.loadSettings();

        // Play Music
        EuropaGame.game.getAudioService().playMusic(TITLE_MUSIC);
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
            }
        });

        this.loadGameButton = new TextButton("Load Game", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.loadGameButton.isDisabled()) {
                    MainMenuScreen.this.onLoadGameClick();
                }
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
            }
        });

        this.creditsButton = new TextButton("Credits", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.creditsButton.isDisabled()) {
                    MainMenuScreen.this.onCreditsClick();
                }
            }
        });

        this.quitButton = new TextButton("Exit", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.quitButton.isDisabled()) {
                    MainMenuScreen.this.onQuitClick();
                }
            }
        });

        // Configure Button Table
        this.buttonTable.add(this.newGameButton).width(BUTTON_WIDTH);
        this.buttonTable.row();
        this.buttonTable.add(this.loadGameButton).width(BUTTON_WIDTH);
        this.buttonTable.row();
        this.buttonTable.add(this.multiplayerButton).width(BUTTON_WIDTH);
        this.buttonTable.row();
        this.buttonTable.add(this.optionsButton).width(BUTTON_WIDTH);
        this.buttonTable.row();
        this.buttonTable.add(this.creditsButton).width(BUTTON_WIDTH);
        this.buttonTable.row();
        this.buttonTable.add(this.quitButton).width(BUTTON_WIDTH);
        this.buttonTable.row();
        this.buttonTable.background(skin.get(BUTTON_TABLE_BACKGROUND_KEY, SpriteDrawable.class));

        // Title
        this.titleTable = new Table();
        this.titleTable.setFillParent(true);
        this.titleTable.center();

        this.titleLabel = new Label(EuropaGame.TITLE, skin.get(DEFAULT_KEY, LabelStyle.class));

        this.titleTable.add(this.titleLabel).top().pad(TITLE_PADDING);
        this.titleTable.row();
        this.titleTable.add(this.buttonTable).center().expandY();
        this.titleTable.row();

        this.stage.addActor(this.titleTable);

        // Load Game Table
        this.loadGameTable = new Table();
        this.loadGameTable.setFillParent(true);
        this.loadGameTable.center();

        this.loadGameLabel = new Label("Save Games", skin.get(DEFAULT_KEY, LabelStyle.class));
        this.loadGameList = new List(skin.get(DEFAULT_KEY, ListStyle.class));
        this.loadGameList.setItems((Object[]) EuropaGame.game.getSaveNames());
        this.loadGameListPane = new ScrollPane(this.loadGameList, skin.get(DEFAULT_KEY, ScrollPaneStyle.class));

        this.loadGameOkButton = new TextButton("Accept", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.loadGameOkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.loadGameOkButton.isDisabled()) {
                    MainMenuScreen.this.loadGame();
                }
            }
        });

        this.loadGameCancelButton = new TextButton("Cancel", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.loadGameCancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.loadGameCancelButton.isDisabled()) {
                    MainMenuScreen.this.cancelLoadGame();
                }
            }
        });

        this.loadGameDeleteButton = new TextButton("Delete", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.loadGameDeleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.loadGameDeleteButton.isDisabled()) {
                    MainMenuScreen.this.onDeleteSaveGameClick();
                }
            }
        });

        this.loadGameButtonTable = new Table();
        this.loadGameButtonTable.add(this.loadGameOkButton).right();
        this.loadGameButtonTable.add(this.loadGameCancelButton).space(20).right();
        this.loadGameButtonTable.add(this.loadGameDeleteButton).space(20).right();

        this.loadGameTable.add(this.loadGameLabel).top().left().height(50).width(600).padTop(25);
        this.loadGameTable.row();
        this.loadGameTable.add(this.loadGameListPane).expandY().minWidth(600).fill();
        this.loadGameTable.row();
        this.loadGameTable.add(this.loadGameButtonTable).right().padBottom(25);
        this.loadGameTable.row();

        this.loadGameDialog = new Dialog("", skin.get(DEFAULT_KEY, WindowStyle.class));
        this.loadGameDialog.getContentTable().add(this.loadGameTable).expand().fill();

        // Confirm Delete Dialog
        this.confirmDeleteDialog = new Dialog("", skin.get(DEFAULT_KEY, WindowStyle.class));
        this.confirmDeleteLabel = new Label("Are you sure you want to delete this save game?", skin.get(INFO_STYLE_KEY,
                LabelStyle.class));
        this.confirmDeleteYesButton = new TextButton("Yes", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.confirmDeleteYesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.confirmDeleteYesButton.isDisabled()) {
                    MainMenuScreen.this.deleteSaveGame();
                }
            }
        });
        this.confirmDeleteNoButton = new TextButton("No", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.confirmDeleteNoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.confirmDeleteNoButton.isDisabled()) {
                    MainMenuScreen.this.confirmDeleteDialog.hide();
                }
            }
        });
        this.confirmDeleteButtonTable = new Table();
        this.confirmDeleteButtonTable.center();
        this.confirmDeleteButtonTable.add(this.confirmDeleteYesButton).center();
        this.confirmDeleteButtonTable.add(this.confirmDeleteNoButton).space(20).center();

        this.confirmDeleteDialog.getContentTable().add(this.confirmDeleteLabel).row();
        this.confirmDeleteDialog.getContentTable().add(this.confirmDeleteButtonTable);

        // Options Dialog
        this.optionsDialog = new Dialog("", skin.get(DEFAULT_KEY, WindowStyle.class));
        this.optionsTable = new Table();
        this.optionsTable.center();
        this.optionsLabel = new Label("Options", skin.get(DEFAULT_KEY, LabelStyle.class));
        this.optionsPane = new TabbedPane(skin.get(TAB_STYLE_KEY, TextButtonStyle.class));
        this.optionsAcceptButton = new TextButton("Accept", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.optionsAcceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.optionsAcceptButton.isDisabled()) {
                    MainMenuScreen.this.applySettings();
                    MainMenuScreen.this.optionsDialog.hide();
                }
            }
        });
        this.optionsCancelButton = new TextButton("Cancel", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.optionsCancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.optionsCancelButton.isDisabled()) {
                    MainMenuScreen.this.optionsDialog.hide();
                }
            }
        });

        this.optionsButtonTable = new Table();
        this.optionsButtonTable.add(this.optionsAcceptButton).right();
        this.optionsButtonTable.add(this.optionsCancelButton).right().space(20);

        //    Audio
        this.musicSlider = new Slider(0f, 1f, 0.05f, false, skin.get(DEFAULT_KEY, SliderStyle.class));
        this.audioTable = new Table();
        this.audioTable.add(this.musicSlider).minHeight(20).minWidth(600).maxWidth(1000).expandX();
        this.audioTable.row();

        this.optionsPane.addTab("Audio", this.audioTable);

        //   Graphics
        this.graphicsTable = new Table();

        this.optionsPane.addTab("Graphics", this.graphicsTable);

        this.optionsTable.add(this.optionsLabel).left().padTop(25).minWidth(600);
        this.optionsTable.row();
        this.optionsTable.add(this.optionsPane).expandY().fill().top();
        this.optionsTable.row();
        this.optionsTable.add(this.optionsButtonTable).padBottom(25);

        this.optionsDialog.getContentTable().add(this.optionsTable).expand().fill();

        // Credits Dialog
        this.creditsDialog = new Dialog("", skin.get(DEFAULT_KEY, WindowStyle.class));
        this.creditsLabel = new Label(EuropaGame.game.getCredits(), skin.get(INFO_STYLE_KEY, LabelStyle.class));
        this.creditsScrollPane = new ScrollPane(this.creditsLabel, skin.get(DEFAULT_KEY, ScrollPaneStyle.class));
        this.creditsOkButton = new TextButton("Return to Menu", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.creditsOkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.creditsOkButton.isDisabled()) {
                    MainMenuScreen.this.creditsDialog.hide();
                }
            }
        });

        this.creditsDialog.getContentTable().add(this.creditsScrollPane).row();
        this.creditsDialog.getContentTable().add(this.creditsOkButton).center();
    }

    private void onNewGameClick() {
        this.createCharacterDialog = new CreateCharacterDialog();
        this.createCharacterDialog.addDialogListener(this::onCharacterCreationCompleted, DialogEvents.HIDDEN);
        this.showDialog(this.createCharacterDialog);
    }

    private void onLoadGameClick() {
        this.loadGameDialog.show(this.stage);
        this.loadGameDialog.setSize(this.stage.getWidth(), this.stage.getHeight());
    }

    private void onMultiplayerClick() {
        System.out.println("Multiplayer is not yet implemented!");
    }

    private void onOptionsClick() {
        this.optionsDialog.show(this.stage);
        this.optionsDialog.setSize(this.stage.getWidth(), this.stage.getHeight());
    }

    private void onCreditsClick() {
        this.creditsDialog.show(this.stage);
        this.creditsDialog.setSize(this.stage.getWidth(), this.stage.getHeight());
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
        Path saveFile = FileLocations.SAVE_DIRECTORY.resolve(this.loadGameList.getSelected().toString() + "." + SaveGame.SAVE_EXTENSION);
        if (!Files.exists(saveFile)) {
            this.cancelLoadGame();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(saveFile)) {
            Json json = new Json();
            JsonValue value = new JsonReader().parse(reader);
            SaveGame save = json.fromJson(SaveGame.class, value.toString());
            EuropaGame.game.startGame(save);
        }
        catch (IOException ex) {
            this.cancelLoadGame();
        }
    }

    private void cancelLoadGame() {
        this.loadGameDialog.hide();
    }

    private void onDeleteSaveGameClick() {
        String selection = this.loadGameList.getSelected().toString();
        this.confirmDeleteLabel.setText("Are you sure you want to delete the save game \"" + selection + "\"?");
        this.confirmDeleteDialog.show(this.stage);
        this.confirmDeleteDialog.setSize(this.stage.getWidth(), this.stage.getHeight());
    }

    private void deleteSaveGame() {
        EuropaGame.game.deleteSave(this.loadGameList.getSelected().toString());
        this.loadGameList.setItems((Object[]) EuropaGame.game.getSaveNames());
        this.loadGameButton.setDisabled(EuropaGame.game.getSaveNames().length == 0);
        this.confirmDeleteDialog.hide();
    }

    private void loadSettings() {
        this.musicSlider.setValue(EuropaGame.game.getSettings().musicVolume.get());
    }

    private void applySettings() {
        EuropaGame.game.getSettings().musicVolume.set(this.musicSlider.getValue());
        EuropaGame.game.saveSettings();
    }
    
    private void showDialog(Dialog dialog) {
        dialog.show(this.stage).setSize(this.size.width, this.size.height);
    }

}
