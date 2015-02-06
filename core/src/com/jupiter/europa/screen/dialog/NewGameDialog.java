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
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;
import static com.jupiter.europa.screen.MainMenuScreen.DEFAULT_KEY;

/**
 *
 * @author Nathan Templon
 */
public class NewGameDialog extends ObservableDialog {
    
    // Enumerations
    public enum NewGameExitStates {
        START_GAME,
        CANCEL
    }
    
    
    // Constants
    private static final String DIALOG_NAME = "";
    
    
    // Static Methods
    private static Skin getSkin() {
        return MainMenuScreen.getMainMenuSkin();
    }
    
    
    // Fields
    private final Skin skin = getSkin();
    
    private Table mainTable;
    private Label newGameNameLabel;
    private TextField newGameNameField;
    private Label newGameWorldLabel;
    private List newGameWorldList;
    private ScrollPane worldPane;
    private Table buttonTable;
    private TextButton nextButton;
    private TextButton backButton;
    
    private NewGameExitStates exitState;
    
    
    // Properties
    public NewGameExitStates getExitState() {
        return this.exitState;
    }
    
    public String getNewGameName() {
        return this.newGameNameField.getText();
    }
    
    public String getNewGameWorldName() {
        return this.newGameWorldList.getSelected().toString();
    }
    
    
    // Initialization
    public NewGameDialog() {
        super(DIALOG_NAME, getSkin());
        
        this.initComponents();
    }
    
    
    // Private Methods
    private void initComponents() {
        this.mainTable = new Table();

        this.newGameNameLabel = new Label("Save Name: ", skin.get(DEFAULT_KEY, Label.LabelStyle.class));
        this.newGameNameField = new TextField("default", skin.get(DEFAULT_KEY, TextField.TextFieldStyle.class));
        this.newGameNameField.setMaxLength(16);
        this.newGameWorldLabel = new Label("World:", skin.get(DEFAULT_KEY, Label.LabelStyle.class));
        this.newGameWorldList = new List(skin.get(DEFAULT_KEY, List.ListStyle.class));
        this.newGameWorldList.setItems((Object[]) EuropaGame.game.getWorldNames());
        this.worldPane = new ScrollPane(this.newGameWorldList, skin.get(DEFAULT_KEY, ScrollPane.ScrollPaneStyle.class));

        this.nextButton = new TextButton("Accept", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !NewGameDialog.this.nextButton.isDisabled()) {
                    NewGameDialog.this.startNewGame();
                }
            }
        });

        this.backButton = new TextButton("Back", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !NewGameDialog.this.backButton.isDisabled()) {
                    NewGameDialog.this.cancelNewGame();
                }
            }
        });

        this.buttonTable = new Table();
        this.buttonTable.add(this.nextButton).space(MainMenuScreen.BUTTON_SPACING).right().expandX();
        this.buttonTable.add(this.backButton).space(MainMenuScreen.BUTTON_SPACING).right();
        
        this.mainTable.padLeft(MainMenuScreen.TABLE_HORIZONTAL_PADDING);
        this.mainTable.padRight(MainMenuScreen.TABLE_HORIZONTAL_PADDING);

        this.mainTable.add(this.newGameNameLabel).center().left();
        this.mainTable.add(this.newGameNameField).center().left().padTop(15).expandX().fillX();
        this.mainTable.row();
        this.mainTable.add(this.newGameWorldLabel).colspan(2).left();
        this.mainTable.row();
        this.mainTable.add(this.worldPane).colspan(2).expandY().fill();
        this.mainTable.row();
        this.mainTable.add(this.buttonTable).colspan(2).right().minWidth(MainMenuScreen.DIALOG_MIN_WIDTH);
        this.mainTable.row();

        this.mainTable.background(this.skin.get(MainMenuScreen.BUTTON_TABLE_BACKGROUND_KEY, SpriteDrawable.class));

        this.getContentTable().add(this.mainTable).center().expandY().fillY();
    }
    
    private void startNewGame() {
        this.exitState = NewGameExitStates.START_GAME;
        this.hide();
    }
    
    private void cancelNewGame() {
        this.exitState = NewGameExitStates.CANCEL;
        this.hide();
    }
    
}
