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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.emergence.EmergenceGame;
import com.emergence.io.FileLocations;
import com.emergence.screen.overlay.PauseMenu;
import java.io.File;

/**
 *
 * @author Nathan Templon
 */
public class MainMenuScreen implements Screen, InputProcessor {

    // Constants
    public static final String TITLE_FONT = "arial48-bold.fnt";
    public static final String BUTTON_FONT = "arial40.fnt";

    private static Skin mainMenuSkin;

    private static final int TITLE_PADDING = 25;
    private static final int BUTTON_WIDTH = 225;
    private static final int BUTTON_HEIGHT = 60;
    private static final int BUTTON_SPACING = 0;

    private static final Color BACKGROUND_COLOR = Color.WHITE;
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

        skin.add("button-font", EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                BUTTON_FONT).getPath()));
        skin.add("title-font", EmergenceGame.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY,
                TITLE_FONT).getPath()));

        // Set the background texture
        Pixmap pixmap = new Pixmap(1, (int) 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        // Create a Label style for the title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.background = skin.newDrawable("background", TRANSPARENT);
        titleStyle.font = skin.getFont("title-font");
        titleStyle.fontColor = Color.BLACK;
        skin.add("default", titleStyle);

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", TRANSPARENT);
        textButtonStyle.down = skin.newDrawable("background", TRANSPARENT);
        textButtonStyle.checked = skin.newDrawable("background", TRANSPARENT);
        textButtonStyle.over = skin.newDrawable("background", TRANSPARENT);
        textButtonStyle.disabled = skin.newDrawable("background", TRANSPARENT);
        textButtonStyle.font = skin.getFont("button-font");
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.overFontColor = Color.BLUE;
        textButtonStyle.disabledFontColor = Color.GRAY;
        skin.add("default", textButtonStyle);
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
    private TextButton quitButton;


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
        this.init();
    }

    @Override
    public void hide() {

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

        // Create Buttons
        this.buttonTable = new Table();
        this.buttonTable.setFillParent(false);
        this.buttonTable.center();

        this.newGameButton = new TextButton("New Game", getMainMenuSkin()); // Use the initialized skin
        this.newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.newGameButton.isDisabled()) {
                    MainMenuScreen.this.onNewGameClick();
                }
            }
        });

        this.loadGameButton = new TextButton("Load Game", getMainMenuSkin());
        this.loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.loadGameButton.isDisabled()) {
                    MainMenuScreen.this.onLoadGameClick();
                }
            }
        });
        this.loadGameButton.setDisabled(true);

        this.multiplayerButton = new TextButton("Multiplayer", getMainMenuSkin());
        this.multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.multiplayerButton.isDisabled()) {
                    MainMenuScreen.this.onMultiplayerClick();
                }
            }
        });
        this.multiplayerButton.setDisabled(true);

        this.optionsButton = new TextButton("Options", getMainMenuSkin());
        this.optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.optionsButton.isDisabled()) {
                    MainMenuScreen.this.onOptionsClick();
                }
            }
        });
        this.optionsButton.setDisabled(true);

        this.quitButton = new TextButton("Exit", getMainMenuSkin());
        this.quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !MainMenuScreen.this.quitButton.isDisabled()) {
                    MainMenuScreen.this.onQuitClick();
                }
            }
        });

        // Configure Button Table
        this.buttonTable.add(this.newGameButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.loadGameButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.multiplayerButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.optionsButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).space(BUTTON_SPACING);
        this.buttonTable.row();
        this.buttonTable.add(this.quitButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).space(BUTTON_SPACING);
        this.buttonTable.row();

        // Title
        this.titleTable = new Table();
        this.titleTable.setFillParent(true);
        this.titleTable.center();

        this.titleLabel = new Label(EmergenceGame.TITLE, getMainMenuSkin());

        this.titleTable.add(this.titleLabel).top().pad(TITLE_PADDING);
        this.titleTable.row();
        this.titleTable.add(this.buttonTable).center().expandY();
        this.titleTable.row();

        this.stage.addActor(this.titleTable);
    }

    private void onNewGameClick() {
        EmergenceGame.game.startGame("Test Game", EmergenceGame.game.getWorld("omterra"));
    }

    private void onLoadGameClick() {
        System.out.println("Load Game Not Implemented!");
    }

    private void onMultiplayerClick() {
        System.out.println("Multiplayer is not yet implemented!");
    }

    private void onOptionsClick() {
        System.out.println("Options not implemented!");
    }

    private void onQuitClick() {
        Gdx.app.exit();
    }

}
