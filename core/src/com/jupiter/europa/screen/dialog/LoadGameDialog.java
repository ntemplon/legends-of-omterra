/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.scene2d.ui.EuropaButton;
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;

import static com.jupiter.europa.screen.MainMenuScreen.*;

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
    private static Skin getDefaultSkin() {
        return MainMenuScreen.getMainMenuSkin();
    }


    // Fields
    private final Skin skin = getDefaultSkin();

    private Table mainTable;
    private Label loadGameLabel;
    private Table listTable;
    private List gameList;
    private ScrollPane gameListPane;
    private Table buttonTable;
    private EuropaButton okButton;
    private EuropaButton cancelButton;
    private EuropaButton deleteButton;
    
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
        super(DIALOG_NAME, getDefaultSkin());

        this.initComponents();
    }


    // Public Methods
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (this.confirmDeleteDialog != null) {
            this.confirmDeleteDialog.setSize(width, this.height);
        }
        
        this.width = width;
        this.height = height;
    }


    // Private Methods
    private void initComponents() {
        this.mainTable = new Table();
        
        this.listTable = new Table();
        this.loadGameLabel = new Label("Save Games", skin.get(DEFAULT_KEY, Label.LabelStyle.class));
        this.gameList = new List(skin.get(DEFAULT_KEY, List.ListStyle.class));
        this.gameList.setItems((Object[]) EuropaGame.game.getSaveNames());
        this.gameListPane = new ScrollPane(this.gameList, skin.get(DEFAULT_KEY, ScrollPane.ScrollPaneStyle.class));
        
        this.listTable.add(this.gameListPane).pad(MainMenuScreen.LIST_WRAPPER_PADDING).expand().fill();
        this.listTable.background(skin.get(LIST_BACKGROUND_KEY, SpriteDrawable.class));

        this.okButton = new EuropaButton("Accept", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.okButton.addClickListener(this::onLoadClick);

        this.cancelButton = new EuropaButton("Cancel", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.cancelButton.addClickListener(this::onCancelClick);

        this.deleteButton = new EuropaButton("Delete", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.deleteButton.addClickListener(this::onDeleteClick);

        this.buttonTable = new Table();
        this.buttonTable.add(this.cancelButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
        this.buttonTable.add(this.deleteButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();
        this.buttonTable.add(this.okButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();
        
        this.mainTable.pad(MainMenuScreen.TABLE_PADDING);

        this.mainTable.add(this.loadGameLabel).center().left();
        this.mainTable.row();
        this.mainTable.add(this.listTable).expandY().fill();
        this.mainTable.row();
        this.mainTable.add(this.buttonTable).space(MainMenuScreen.COMPONENT_SPACING).right().expandX().fillX();
        this.mainTable.row();
        
        this.mainTable.background(this.skin.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, SpriteDrawable.class));

        this.getContentTable().add(this.mainTable).center().expandY().fillY().width(MainMenuScreen.DIALOG_WIDTH);
    }

    private void onLoadClick(ClickEvent event) {
        this.gameToLoad = this.gameList.getSelected().toString();
        this.exitState = LoadGameExitStates.LOAD;
        this.hide();
    }

    private void onCancelClick(ClickEvent event) {
        this.gameToLoad = "";
        this.exitState = LoadGameExitStates.CANCEL;
        this.hide();
    }

    private void onDeleteClick(ClickEvent event) {
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
            super(DIALOG_NAME, skin.get(MainMenuScreen.POPUP_DIALOG_STYLE_KEY, WindowStyle.class));

            this.saveName = saveName;
            this.skin = skin;

            this.initComponents();
        }
        
        
        // Public Methods
        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
        }


        // Private Methods
        private void initComponents() {
            this.titleLabel = new Label("Are you sure you want to delete the save game \"" + this.saveName + "\"?", skin.get(INFO_STYLE_KEY,
                    Label.LabelStyle.class));
            this.titleLabel.setWrap(true);
            this.titleLabel.setAlignment(Align.center);
            
            this.yesButton = new TextButton("Yes", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
            this.yesButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !ConfirmDeleteSaveDialog.this.yesButton.isDisabled()) {
                        ConfirmDeleteSaveDialog.this.onYesButtonClick();
                    }
                    ConfirmDeleteSaveDialog.this.yesButton.setChecked(false);
                }
            });
            
            this.noButton = new TextButton("No", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
            this.noButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !ConfirmDeleteSaveDialog.this.noButton.isDisabled()) {
                        ConfirmDeleteSaveDialog.this.onNoButtonClick();
                    }
                    ConfirmDeleteSaveDialog.this.noButton.setChecked(false);
                }
            });
            
            this.mainTable = new Table();
            this.mainTable.add(this.titleLabel).colspan(2).width(MainMenuScreen.DIALOG_WIDTH).expandX().fillX();
            this.mainTable.row();
            this.mainTable.add(this.noButton).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING).right();
            this.mainTable.add(this.yesButton).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).space(MainMenuScreen.COMPONENT_SPACING).left();
            
            this.mainTable.padLeft(MainMenuScreen.TABLE_PADDING);
            this.mainTable.padRight(MainMenuScreen.TABLE_PADDING);
            
            this.mainTable.background(this.skin.get(MainMenuScreen.POPUP_BACKGROUND_KEY, SpriteDrawable.class));
            this.getContentTable().add(this.mainTable).row();
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
