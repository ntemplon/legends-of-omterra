/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.scene2d.ui.EuropaButton;
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent;
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
    private static Skin getDefaultSkin() {
        return MainMenuScreen.getMainMenuSkin();
    }
    
    
    // Fields
    private final Skin skin = getDefaultSkin();
    
    private Table mainTable;
    private Table wrapperTable;
    private ScrollPane scrollPane;
    private Label label;
    private EuropaButton returnButton;
    
    
    // Initialization
    public CreditsDialog() {
        super(DIALOG_NAME, getDefaultSkin().get(WindowStyle.class));
        
        this.initComponents();
    }
    
    
    // Private Methods
    private void initComponents() {
        // Credits Dialog
        this.label = new Label(EuropaGame.game.getCredits(), skin.get(INFO_STYLE_KEY, Label.LabelStyle.class));
        this.scrollPane = new ScrollPane(this.label, skin.get(DEFAULT_KEY, ScrollPane.ScrollPaneStyle.class));
        this.returnButton = new EuropaButton("Return to Menu", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.returnButton.addClickListener((ClickEvent e) -> this.hide());
        
        this.wrapperTable = new Table();
        this.wrapperTable.add(this.scrollPane).pad(MainMenuScreen.LIST_WRAPPER_PADDING).expand().fill();
        this.wrapperTable.background(this.skin.get(MainMenuScreen.CREDITS_BACKGROUND_KEY, SpriteDrawable.class));
        
        this.mainTable = new Table();

        this.mainTable.add(this.wrapperTable).row();
        this.mainTable.add(this.returnButton).center().space(MainMenuScreen.COMPONENT_SPACING);
        
        this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
        
        this.getContentTable().add(this.mainTable).expandY().fillY().minWidth(MainMenuScreen.DIALOG_WIDTH).center();
    }
    
}
