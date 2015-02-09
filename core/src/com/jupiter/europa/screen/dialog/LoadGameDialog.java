/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    private float width, height;

    private String gameToLoad = "";
    private LoadGameExitStates exitState;

    private ConfirmDeleteSaveDialog confirmDeleteDialog;


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
        if (this.confirmDeleteDialog != null) {
            this.confirmDeleteDialog.setSize(width, height);
        }
        
        this.width = width;
        this.height = height;
    }


    // Private Methods
    private void initComponents() {
        this.mainTable = new Table();

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
        this.buttonTable.add(this.okButton).space(MainMenuScreen.BUTTON_SPACING).right().expandX();
        this.buttonTable.add(this.cancelButton).space(MainMenuScreen.BUTTON_SPACING).right();
        this.buttonTable.add(this.deleteButton).space(MainMenuScreen.BUTTON_SPACING).right();
        
        this.mainTable.padLeft(MainMenuScreen.TABLE_HORIZONTAL_PADDING);
        this.mainTable.padRight(MainMenuScreen.TABLE_HORIZONTAL_PADDING);

        this.mainTable.add(this.loadGameLabel).center().left();
        this.mainTable.row();
        this.mainTable.add(this.gameListPane).expandY().fill();
        this.mainTable.row();
        this.mainTable.add(this.buttonTable).right().minWidth(MainMenuScreen.DIALOG_MIN_WIDTH);
        this.mainTable.row();
        
        this.mainTable.background(this.skin.get(MainMenuScreen.BUTTON_TABLE_BACKGROUND_KEY, SpriteDrawable.class));

        this.getContentTable().add(this.mainTable).center().expandY().fillY();
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
        this.confirmDeleteDialog = new ConfirmDeleteSaveDialog(this.gameList.getSelected().toString(), this.skin);
        this.confirmDeleteDialog.addDialogListener(this::onConfirmDeleteSaveDialogClose, DialogEvents.HIDDEN);
        this.confirmDeleteDialog.show(this.getStage());
        this.confirmDeleteDialog.setSize(this.width, this.height);
    }

    private void onConfirmDeleteSaveDialogClose(DialogEventArgs args) {
        if (this.confirmDeleteDialog.getExitState() == ConfirmDeleteSaveDialog.ConfirmDeleteExitStates.DELETE) {
            EuropaGame.game.deleteSave(this.gameList.getSelected().toString());
            this.gameList.setItems((Object[]) EuropaGame.game.getSaveNames());
            this.okButton.setDisabled(EuropaGame.game.getSaveNames().length == 0);
        }
    }


    // Nested Classes
    private static class ConfirmDeleteSaveDialog extends ObservableDialog {

        // Enumerations
        public enum ConfirmDeleteExitStates {

            DELETE,
            CANCEL
        }


        // Constants
        private static final String DIALOG_NAME = "";


        // Fields
        private final String saveName;
        private final Skin skin;

        private Label titleLabel;
        private Table mainTable;
        private TextButton yesButton;
        private TextButton noButton;

        private ConfirmDeleteExitStates exitState;


        // Properties
        public String getSaveToDelete() {
            return this.saveName;
        }

        public ConfirmDeleteExitStates getExitState() {
            return this.exitState;
        }


        // Initialization
        private ConfirmDeleteSaveDialog(String saveName, Skin skin) {
            super(DIALOG_NAME, skin.get(WindowStyle.class));

            this.saveName = saveName;
            this.skin = skin;

            this.initComponents();
        }


        // Private Methods
        private void initComponents() {
            this.titleLabel = new Label("Are you sure you want to delete the save game \"" + this.saveName + "\"?", skin.get(INFO_STYLE_KEY,
                    Label.LabelStyle.class));
            this.titleLabel.setWrap(true);
            
            this.yesButton = new TextButton("Yes", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
            this.yesButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !ConfirmDeleteSaveDialog.this.yesButton.isDisabled()) {
                        ConfirmDeleteSaveDialog.this.onYesButtonClick();
                    }
                }
            });
            
            this.noButton = new TextButton("No", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
            this.noButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !ConfirmDeleteSaveDialog.this.noButton.isDisabled()) {
                        ConfirmDeleteSaveDialog.this.onNoButtonClick();
                    }
                }
            });
            
            this.mainTable = new Table();
            this.mainTable.add(this.titleLabel).width(MainMenuScreen.DIALOG_MIN_WIDTH).center().expandY();
            this.mainTable.row();
            this.mainTable.add(this.yesButton).right().expandX();
            this.mainTable.add(this.noButton).space(MainMenuScreen.BUTTON_SPACING).right();
            
            this.mainTable.padLeft(MainMenuScreen.TABLE_HORIZONTAL_PADDING);
            this.mainTable.padRight(MainMenuScreen.TABLE_HORIZONTAL_PADDING);
            
            this.mainTable.background(this.skin.get(MainMenuScreen.BUTTON_TABLE_BACKGROUND_KEY, SpriteDrawable.class));

            this.getContentTable().add(this.mainTable).expandY().fillY();
        }

        private void onYesButtonClick() {
            this.exitState = ConfirmDeleteExitStates.DELETE;
            this.hide();
        }

        private void onNoButtonClick() {
            this.exitState = ConfirmDeleteExitStates.CANCEL;
            this.hide();
        }

    }

}
