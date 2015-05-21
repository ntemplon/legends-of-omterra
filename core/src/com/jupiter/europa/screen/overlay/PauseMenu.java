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
package com.jupiter.europa.screen.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.screen.OverlayableScreen;

import static com.jupiter.europa.screen.MainMenuScreen.BUTTON_FONT;
import static com.jupiter.europa.screen.MainMenuScreen.TITLE_FONT;

/**
 *
 * @author Nathan Templon
 */
public class PauseMenu extends Scene2DOverlay {

    // Constants
    private static final Color TRANSPARENT = new Color(1, 1, 1, 0);
    private static final Color BACKGROUND_TINT = new Color(Color.LIGHT_GRAY);

    private static Skin pauseMenuSkin;


    // Static Methods
    public static Skin getPauseMenuSkin() {
        if (pauseMenuSkin == null) {
            buildPauseMenuSkin();
        }
        return pauseMenuSkin;
    }

    private static void buildPauseMenuSkin() {
        Skin skin = new Skin();

        skin.add("button-font", EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(BUTTON_FONT).toString()));
        skin.add("title-font", EuropaGame.game.getAssetManager().get(FileLocations.FONTS_DIRECTORY.resolve(TITLE_FONT).toString()));

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
        textButtonStyle.fontColor = Color.TEAL;
        textButtonStyle.overFontColor = Color.YELLOW;
        textButtonStyle.disabledFontColor = Color.GRAY;
        textButtonStyle.pressedOffsetX = 2f;
        textButtonStyle.pressedOffsetY = -3f;
        skin.add("default", textButtonStyle);
//        
//        skin = EmergenceGame.game.getAssetManager().get(
//                new File(FileLocations.SKINS_DIRECTORY, "main_menu.skin").getPath());
        pauseMenuSkin = skin;
    }


    // Fields
    private int exitKey = Input.Keys.ESCAPE;

    private Table buttonTable;
    private TextButton resumeButton;
    private TextButton returnToMenuButton;
    private TextButton exitButton;


    // Properties
    public final int getExitKey() {
        return this.exitKey;
    }

    public final void setExitKey(int key) {
        this.exitKey = key;
    }

    @Override
    public Color getBackgroundTint() {
        return BACKGROUND_TINT;
    }


    // Initialization
    public PauseMenu() {
        super(true);
    }


    // Public Methods
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == this.getExitKey()) {
            this.getScreen().removeOverlay(this);
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public void added(OverlayableScreen screen) {
        super.added(screen);
        this.initializeComponents();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        this.buttonTable.setSize(width, height);
        this.buttonTable.invalidate();
    }


    // Private Methods
    private void initializeComponents() {
        this.buttonTable = new Table();
        this.buttonTable.setFillParent(true);
        this.buttonTable.center();

        this.resumeButton = new TextButton("Resume", getPauseMenuSkin());
        this.resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !PauseMenu.this.resumeButton.isDisabled()) {
                    PauseMenu.this.resumeGame();
                }
            }
        });

        this.returnToMenuButton = new TextButton("Return to Menu", getPauseMenuSkin());
        this.returnToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !PauseMenu.this.returnToMenuButton.isDisabled()) {
                    PauseMenu.this.returnToMenu();
                }
            }
        });

        this.exitButton = new TextButton("Exit", getPauseMenuSkin());
        this.exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !PauseMenu.this.exitButton.isDisabled()) {
                    Gdx.app.exit();
                }
            }
        });

        // Table configuration
        this.buttonTable.add(this.resumeButton);
        this.buttonTable.row();
        this.buttonTable.add(this.returnToMenuButton);
        this.buttonTable.row();
        this.buttonTable.add(this.exitButton);
        this.buttonTable.row();

        this.getStage().addActor(this.buttonTable);
    }

    private void resumeGame() {
        this.getScreen().removeOverlay(this);
    }

    private void returnToMenu() {
        PauseMenu.this.resumeGame();
        EuropaGame.game.setState(EuropaGame.GameStates.MAIN_MENU);
    }

}
