/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;
import static com.jupiter.europa.screen.MainMenuScreen.DEFAULT_KEY;
import static com.jupiter.europa.screen.MainMenuScreen.INFO_STYLE_KEY;

/**
 *
 * @author Nathan Templon
 */
public class CreditsDialog extends ObservableDialog {
    
    // Constants
    public static final String DIALOG_NAME = "";
    
    
    // Static Methods
    private static Skin getSkin() {
        return MainMenuScreen.getMainMenuSkin();
    }
    
    
    // Fields
    private final Skin skin = getSkin();
    
    private Table mainTable;
    private ScrollPane scrollPane;
    private Label label;
    private TextButton returnButton;
    
    
    // Initialization
    public CreditsDialog() {
        super(DIALOG_NAME, getSkin().get(WindowStyle.class));
        
        this.initComponents();
    }
    
    
    // Private Methods
    private void initComponents() {
        // Credits Dialog
        this.label = new Label(EuropaGame.game.getCredits(), skin.get(INFO_STYLE_KEY, Label.LabelStyle.class));
        this.scrollPane = new ScrollPane(this.label, skin.get(DEFAULT_KEY, ScrollPane.ScrollPaneStyle.class));
        this.returnButton = new TextButton("Return to Menu", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !CreditsDialog.this.returnButton.isDisabled()) {
                    CreditsDialog.this.hide();
                }
            }
        });
        
        this.mainTable = new Table();

        this.mainTable.add(this.scrollPane).row();
        this.mainTable.add(this.returnButton).center();
        
        this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
//        this.mainTable.padRight(MainMenuScreen.TABLE_HORIZONTAL_PADDING);
        
        this.mainTable.background(this.skin.get(MainMenuScreen.BUTTON_TABLE_BACKGROUND_KEY, SpriteDrawable.class));
        
        this.getContentTable().add(this.mainTable).expandY().fillY().minWidth(MainMenuScreen.DIALOG_WIDTH);
    }
    
}
