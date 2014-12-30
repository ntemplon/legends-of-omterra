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
package com.emergence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.emergence.EmergenceGame;
import static com.emergence.audio.AudioService.TITLE_MUSIC;
import com.emergence.io.FileLocations;
import com.emergence.save.SaveGame;
import com.emergence.world.World;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Nathan Templon
 */
public class MainMenuScreen implements Screen, InputProcessor {

    // Constants
    public static final String TITLE_FONT = "arial48-bold.fnt";
    public static final String BUTTON_FONT = "arial40.fnt";
    public static final String LIST_FONT = "arial32.fnt";
    public static final String TEXT_FIELD_FONT = "arial32.fnt";
    public static final String INFO_LABEL_FONT = "arial24.fnt";

    private static final String DEFAULT_KEY = "default";
    private static final String INFO_STYLE_KEY = "info";

    private static final String SOLID_TEXTURE_KEY = "solid-texture";
    private static final String TITLE_FONT_KEY = "title-font";
    private static final String BUTTON_FONT_KEY = "button-font";
    private static final String TEXT_FIELD_FONT_KEY = "text-field-font";
    private static final String LIST_FONT_KEY = "list-font";
    private static final String INFO_LABEL_FONT_KEY = "info-label-font";

    private static Skin mainMenuSkin;

    private static final int TITLE_PADDING = 25;
    private static final int BUTTON_WIDTH = 225;
    private static final int BUTTON_SPACING = 0;

    private static final Color BACKGROUND_COLOR = new Color(Color.WHITE);
    private static final Color SELECTION_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.2f);
    private static final Color TRANSPARENT = new Color(1, 1, 1, 0);


    // Static Methods
    public static Skin getMainMenuSkin() {
        if (mainMenuSkin == null) {
            buildMainMenuSkin();
        }
        return mainMenuSkin;
    }

    private static void buildMainMenuSkin() {
        Skin skin = new Skin();

        skin.add(BUTTON_FONT_KEY, EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                BUTTON_FONT).getPath()));
        skin.add(TITLE_FONT_KEY, EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                TITLE_FONT).getPath()));
        skin.add(LIST_FONT_KEY, EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                LIST_FONT).getPath()));
        skin.add(TEXT_FIELD_FONT_KEY, EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                TEXT_FIELD_FONT).getPath()));
        skin.add(INFO_LABEL_FONT_KEY, EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                INFO_LABEL_FONT).getPath()));

        // Set the background texture
        Pixmap pixmap = new Pixmap(1, (int) 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add(SOLID_TEXTURE_KEY, new Texture(pixmap));
        Drawable transparentDrawable = skin.newDrawable(SOLID_TEXTURE_KEY, TRANSPARENT);

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

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = transparentDrawable;
        textButtonStyle.down = transparentDrawable;
        textButtonStyle.checked = transparentDrawable;
        textButtonStyle.over = transparentDrawable;
        textButtonStyle.disabled = transparentDrawable;
        textButtonStyle.font = skin.getFont(BUTTON_FONT_KEY);
        textButtonStyle.fontColor = new Color(Color.BLACK);
        textButtonStyle.overFontColor = new Color(Color.BLUE);
        textButtonStyle.disabledFontColor = new Color(Color.GRAY);
        textButtonStyle.pressedOffsetX = 2f;
        textButtonStyle.pressedOffsetY = -3f;
        skin.add(DEFAULT_KEY, textButtonStyle);

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
        listStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(0f, 0f, 0f, 0.1f));
        skin.add(DEFAULT_KEY, listStyle);

        // Create a Scroll Pane Style
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.background = transparentDrawable;
        skin.add(DEFAULT_KEY, scrollPaneStyle);

        // Create a Dialog Style
        WindowStyle dialogStyle = new WindowStyle();
        dialogStyle.background = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(Color.WHITE));
        dialogStyle.stageBackground = skin.newDrawable(SOLID_TEXTURE_KEY, new Color(Color.WHITE));
        dialogStyle.titleFont = skin.getFont(TITLE_FONT_KEY);
        dialogStyle.titleFontColor = new Color(Color.BLACK);
        skin.add(DEFAULT_KEY, dialogStyle);
//        
//        skin = EmergenceGame.game.getAssetManager().get(
//                new File(FileLocations.SKINS_DIRECTORY, "main_menu.skin").getPath());
        mainMenuSkin = skin;
    }


    // Fields
    private Stage stage;

    private Table titleTable;
    private Table buttonTable;
    private Label titleLabel;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton multiplayerButton;
    private TextButton optionsButton;
    private TextButton creditsButton;
    private TextButton quitButton;

    private Dialog newGameDialog;
    private Table newGameTable;
    private Label newGameNameLabel;
    private TextField newGameNameField;
    private Label newGameWorldLabel;
    private List newGameWorldList;
    private ScrollPane newGameWorldPane;
    private Table newGameButtonTable;
    private TextButton newGameOkButton;
    private TextButton newGameCancelButton;

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
    private TextButton confirmDeleteYesButton;
    private TextButton confirmDeleteNoButton;


    // Initialization
    public MainMenuScreen() {

    }


    // Screen Implementation
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act();
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // True buts 0, 0 at the bottom left corner, false or omission buts 0, 0 at the center
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        EmergenceGame.game.inspectSaves();
        this.init();

        // Play Music
        EmergenceGame.game.getAudioService().playMusic(TITLE_MUSIC);
    }

    @Override
    public void hide() {
        EmergenceGame.game.getAudioService().stop();
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
        this.loadGameButton.setDisabled(EmergenceGame.game.getSaveNames().length == 0);

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
        this.optionsButton.setDisabled(true);

        this.creditsButton = new TextButton("Credits", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.creditsButton.isDisabled()) {
                    MainMenuScreen.this.onCreditsClick();
                }
            }
        });
        this.creditsButton.setDisabled(true);

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
        this.buttonTable.add(this.newGameButton).width(BUTTON_WIDTH).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.loadGameButton).width(BUTTON_WIDTH).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.multiplayerButton).width(BUTTON_WIDTH).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.optionsButton).width(BUTTON_WIDTH).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.creditsButton).width(BUTTON_WIDTH).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.quitButton).width(BUTTON_WIDTH).space(BUTTON_SPACING);
        this.buttonTable.row();

        // Title
        this.titleTable = new Table();
        this.titleTable.setFillParent(true);
        this.titleTable.center();

        this.titleLabel = new Label(EmergenceGame.TITLE, skin.get(DEFAULT_KEY, LabelStyle.class));

        this.titleTable.add(this.titleLabel).top().pad(TITLE_PADDING);
        this.titleTable.row();
        this.titleTable.add(this.buttonTable).center().expandY();
        this.titleTable.row();

        this.stage.addActor(this.titleTable);

        // New Game Table
        this.newGameTable = new Table();
        this.newGameTable.setFillParent(true);
        this.newGameTable.center();

        this.newGameNameLabel = new Label("Save Name: ", skin.get(DEFAULT_KEY, LabelStyle.class));
        this.newGameNameField = new TextField("default", skin.get(DEFAULT_KEY, TextFieldStyle.class));
        this.newGameWorldLabel = new Label("World:", skin.get(DEFAULT_KEY, LabelStyle.class));
        this.newGameWorldList = new List(skin.get(DEFAULT_KEY, ListStyle.class));
        this.newGameWorldList.setItems((Object[]) EmergenceGame.game.getWorldNames());
        this.newGameWorldPane = new ScrollPane(this.newGameWorldList, skin.get(DEFAULT_KEY, ScrollPaneStyle.class));

        this.newGameOkButton = new TextButton("Accept", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.newGameOkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.newGameOkButton.isDisabled()) {
                    MainMenuScreen.this.startNewGame();
                }
            }
        });

        this.newGameCancelButton = new TextButton("Cancel", skin.get(DEFAULT_KEY, TextButtonStyle.class));
        this.newGameCancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.LEFT && !MainMenuScreen.this.newGameCancelButton.isDisabled()) {
                    MainMenuScreen.this.cancelNewGame();
                }
            }
        });

        this.newGameButtonTable = new Table();
        this.newGameButtonTable.add(this.newGameOkButton).right();
        this.newGameButtonTable.add(this.newGameCancelButton).space(20).right();

        this.newGameTable.add(this.newGameNameLabel).top().height(50);
        this.newGameTable.add(this.newGameNameField).top().padTop(5).width(350).height(50);
        this.newGameTable.row();
        this.newGameTable.add(this.newGameWorldLabel).left();
        this.newGameTable.row();
        this.newGameTable.add(this.newGameWorldPane).colspan(2).height(300).fill();
        this.newGameTable.row();
        this.newGameTable.add(this.newGameButtonTable).colspan(2).right();
        this.newGameTable.row();

        this.newGameDialog = new Dialog("", skin.get(DEFAULT_KEY, WindowStyle.class));
        this.newGameDialog.getContentTable().add(this.newGameTable).expand().fill();
        
        // Load Game Table
        this.loadGameTable = new Table();
        this.loadGameTable.setFillParent(true);
        this.loadGameTable.center();

        this.loadGameLabel = new Label("Save Games", skin.get(DEFAULT_KEY, LabelStyle.class));
        this.loadGameList = new List(skin.get(DEFAULT_KEY, ListStyle.class));
        this.loadGameList.setItems((Object[]) EmergenceGame.game.getSaveNames());
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

        this.loadGameTable.add(this.loadGameLabel).top().height(50).width(600);
        this.loadGameTable.row();
        this.loadGameTable.add(this.loadGameListPane).height(300).fill();
        this.loadGameTable.row();
        this.loadGameTable.add(this.loadGameButtonTable).right();
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
        
        this.confirmDeleteDialog.text(this.confirmDeleteLabel);
        this.confirmDeleteDialog.getButtonTable().add(this.confirmDeleteYesButton).right();
        this.confirmDeleteDialog.getButtonTable().add(this.confirmDeleteNoButton).space(20).right();
    }

    private void onNewGameClick() {
        this.newGameDialog.show(this.stage);
        this.newGameNameField.setText("default");
    }

    private void onLoadGameClick() {
        this.loadGameDialog.show(this.stage);
    }

    private void onMultiplayerClick() {
        System.out.println("Multiplayer is not yet implemented!");
    }

    private void onOptionsClick() {
        System.out.println("Options not implemented!");
    }

    private void onCreditsClick() {

    }

    private void onQuitClick() {
        Gdx.app.exit();
    }

    private void startNewGame() {
        String gameName = this.newGameNameField.getText();
        if (gameName == null || gameName.equals("")) {
            gameName = "default";
        }

        World world = EmergenceGame.game.getWorld(this.newGameWorldList.getSelected().toString());

        EmergenceGame.game.startGame(gameName, world);
    }

    private void cancelNewGame() {
        this.newGameDialog.hide();
    }

    private void loadGame() {
        File saveFile = new File(FileLocations.SAVE_DIRECTORY,
                this.loadGameList.getSelected().toString() + "." + SaveGame.SAVE_EXTENSION);
        if (!saveFile.exists()) {
            this.cancelLoadGame();
            return;
        }

        try (FileReader fr = new FileReader(saveFile);
                BufferedReader reader = new BufferedReader(fr);) {
            Json json = new Json();
            JsonValue value = new JsonReader().parse(reader);
            SaveGame save = json.fromJson(SaveGame.class, value.toString());
            EmergenceGame.game.startGame(save);
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
    }
    
    private void deleteSaveGame() {
        EmergenceGame.game.deleteSave(this.loadGameList.getSelected().toString());
        this.loadGameList.setItems((Object[])EmergenceGame.game.getSaveNames());
        this.loadGameButton.setDisabled(EmergenceGame.game.getSaveNames().length == 0);
        this.confirmDeleteDialog.hide();
    }

}
