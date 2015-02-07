/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;
import static com.jupiter.europa.screen.MainMenuScreen.DEFAULT_KEY;

/**
 *
 * @author Nathan Templon
 */
public class LoadGameDialog extends ObservableDialog {
    
    // Enumerations
    public enum LoadGameExitStates {
        LOAD,
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
    private Label loadGameLabel;
    private List gameList;
    private ScrollPane gameListPane;
    private Table buttonTable;
    private TextButton okButton;
    private TextButton cancelButton;
    private TextButton deleteButton;
    
    private String gameToLoad = "";
    private LoadGameExitStates exitState;
    
    
    // Properties
    public final String getGameToLoad() {
        return this.gameToLoad;
    }
    
    public final LoadGameExitStates getExitState() {
        return this.exitState;
    }
    
    
    // Initialization
    public LoadGameDialog() {
        super(DIALOG_NAME, getSkin());
        
        this.initComponents();
    }
    
    
    // Public Methods
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
    }
    
    
    // Private Methods
    private void initComponents() {
        this.mainTable = new Table();
        this.mainTable.setFillParent(true);
        this.mainTable.center();

        this.loadGameLabel = new Label("Save Games", skin.get(DEFAULT_KEY, Label.LabelStyle.class));
        this.gameList = new List(skin.get(DEFAULT_KEY, List.ListStyle.class));
        this.gameList.setItems((Object[]) EuropaGame.game.getSaveNames());
        this.gameListPane = new ScrollPane(this.gameList, skin.get(DEFAULT_KEY, ScrollPane.ScrollPaneStyle.class));

        this.okButton = new TextButton("Accept", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !LoadGameDialog.this.okButton.isDisabled()) {
                    LoadGameDialog.this.onLoadClick();
                }
            }
        });

        this.cancelButton = new TextButton("Cancel", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !LoadGameDialog.this.cancelButton.isDisabled()) {
                    LoadGameDialog.this.onCancelClick();
                }
            }
        });

        this.deleteButton = new TextButton("Delete", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !LoadGameDialog.this.deleteButton.isDisabled()) {
                    LoadGameDialog.this.onDeleteClick();
                }
            }
        });

        this.buttonTable = new Table();
        this.buttonTable.add(this.okButton).right();
        this.buttonTable.add(this.cancelButton).space(20).right();
        this.buttonTable.add(this.deleteButton).space(20).right();

        this.mainTable.add(this.loadGameLabel).top().left().height(50).width(600).padTop(25);
        this.mainTable.row();
        this.mainTable.add(this.gameListPane).expandY().minWidth(600).fill();
        this.mainTable.row();
        this.mainTable.add(this.buttonTable).right().padBottom(25);
        this.mainTable.row();

        this.getContentTable().add(this.mainTable).expand().fill();
    }
    
    private void onLoadClick() {
        this.gameToLoad = this.gameList.getSelected().toString();
        this.exitState = LoadGameExitStates.LOAD;
        this.hide();
    }
    
    private void onCancelClick() {
        this.gameToLoad = "";
        this.exitState = LoadGameExitStates.CANCEL;
        this.hide();
    }
    
    private void onDeleteClick() {
        // Show Confirm Delete Dialog
    }
    
    
    // Nested Classes
    private static class ConfirmDeleteSaveDialog extends ObservableDialog {
        
        // Constants
        private static final String DIALOG_NAME = "";
        
        
        // Initialization
        private ConfirmDeleteSaveDialog(Skin skin) {
            super(DIALOG_NAME, skin);
            
            this.initComponents();
        }
        
        
        // Private Methods
        private void initComponents() {
            
        }
        
    }
    
}
