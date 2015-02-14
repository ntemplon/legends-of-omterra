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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.EuropaEntity;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.entity.component.MovementResourceComponent;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.scene2d.ui.EuropaSelectBox;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;

/**
 *
 * @author Nathan Templon
 */
public class CreateCharacterDialog extends ObservableDialog {

    // Enumerations
    public enum CreateCharacterExitStates {

        OK,
        CANCELED
    }


    // Constants
    public static final String DIALOG_NAME = "Create a Character";
    
    
    // Static Methods
    private static Skin getSkin() {
        return MainMenuScreen.getMainMenuSkin();
    }


    // Fields
    private final SelectRaceClassDialog selectRaceClass;
    private final Skin skin = getSkin();

    private CreateCharacterExitStates exitState = CreateCharacterExitStates.CANCELED;
    private EuropaEntity createdEntity;


    // Properties
    public final CreateCharacterExitStates getExitState() {
        return this.exitState;
    }
    
    public final EuropaEntity getCreatedEntity() {
        return this.createdEntity;
    }


    // Initialization
    public CreateCharacterDialog() {
        super(DIALOG_NAME, getSkin().get(WindowStyle.class));

        this.selectRaceClass = new SelectRaceClassDialog(skin);
        this.selectRaceClass.addDialogListener(this::onSelectRaceClassHide, DialogEvents.HIDDEN);
        
        this.addDialogListener(this::onShown, DialogEvents.SHOWN);
    }
    
    
    // Public Methods
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.selectRaceClass.setSize(width, height);
    }


    // Private Methods
    private void onShown(DialogEventArgs args) {
        this.selectRaceClass.show(this.getStage());
    }
    
    private void onSelectRaceClassHide(DialogEventArgs args) {
        if (this.selectRaceClass.getExitState() == SelectRaceClassDialog.SelectRaceClassExitStates.OK) {
            this.exitState = CreateCharacterExitStates.OK;
        }
        else {
            this.exitState = CreateCharacterExitStates.CANCELED;
        }
        this.concludeDialog();
    }

    private void concludeDialog() {
        this.createdEntity = Party.createPlayer("Tharivol", CharacterClass.CLASS_LOOKUP.get(this.selectRaceClass.getSelectedClass()), this.selectRaceClass
                .getSelectedRace(), new AttributeSet());
        this.hide();
    }


    // Nested Classes
    private static class SelectRaceClassDialog extends ObservableDialog {

        // Enumerations
        public enum SelectRaceClassExitStates {

            OK,
            CANCELED
        }


        // Constants
        private static final String DIALOG_NAME = "";
        private static final String TITLE = "Select a Race and Class";
        
        private static final int IMAGE_SCALE = 4;
        


        // Fields
        private final Skin skin;

        private Table mainTable;
        private Table selectBoxTable;
        private EuropaSelectBox<Race> raceSelectBox;
        private EuropaSelectBox<String> classSelectBox;
        private Label titleLabel;
        private Label raceLabel;
        private Label classLabel;
        private Image raceClassPreview;
        private TextButton raceClassBackButton;
        private TextButton raceClassNextButton;

        private SelectRaceClassExitStates exitState = SelectRaceClassExitStates.CANCELED;


        // Properties
        public SelectRaceClassExitStates getExitState() {
            return this.exitState;
        }

        public final String getSelectedClass() {
            return this.classSelectBox.getSelected();
        }
        
        public final Race getSelectedRace() {
            return this.raceSelectBox.getSelected();
        }


        // Initialization
        private SelectRaceClassDialog(Skin skin) {
            super(DIALOG_NAME, skin.get(WindowStyle.class));
            this.skin = skin;

            this.initComponents();
        }


        // Private Methods
        private void initComponents() {
            this.titleLabel = new Label(TITLE, skin.get(LabelStyle.class));
            this.raceLabel = new Label("Race: ", skin.get(LabelStyle.class));
            this.classLabel = new Label("Class: ", skin.get(LabelStyle.class));
            this.raceClassPreview = new Image(EuropaGame.game.getAssetManager()
                    .get(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(),
                            TextureAtlas.class).findRegion(Race.PlayerRaces.Human.getTextureString() + "-champion-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME));
            this.raceClassPreview.setScale(IMAGE_SCALE);

            this.raceSelectBox = new EuropaSelectBox<>(skin.get(EuropaSelectBox.EuropaSelectBoxStyle.class));
            this.raceSelectBox.setItems(Race.PlayerRaces.values());
            this.raceSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SelectRaceClassDialog.this.updateNewCharacterPreview();
                }
            });

            this.classSelectBox = new EuropaSelectBox<>(skin.get(EuropaSelectBox.EuropaSelectBoxStyle.class));
            this.classSelectBox.setItems(CharacterClass.AVAILABLE_CLASSES);
            this.classSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SelectRaceClassDialog.this.updateNewCharacterPreview();
                }
            });

            this.raceClassBackButton = new TextButton("Back", skin.get(TextButton.TextButtonStyle.class));
            this.raceClassBackButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !SelectRaceClassDialog.this.raceClassBackButton.isDisabled()) {
                        SelectRaceClassDialog.this.onRaceClassBackButton();
                    }
                }
            });

            this.raceClassNextButton = new TextButton("Next", skin.get(TextButton.TextButtonStyle.class));
            this.raceClassNextButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !SelectRaceClassDialog.this.raceClassNextButton.isDisabled()) {
                        SelectRaceClassDialog.this.onRaceClassNextButton();
                    }
                }
            });

            this.mainTable = new Table();

            this.selectBoxTable = new Table();
            this.selectBoxTable.add(this.raceLabel).fillX();
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.raceSelectBox).fillX();
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.classLabel).fillX();
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.classSelectBox).fillX();
            this.selectBoxTable.row();

            this.mainTable.add(this.titleLabel).center().colspan(2);
            this.mainTable.row();
            this.mainTable.add(this.selectBoxTable).left().expandY();
            this.mainTable.add(this.raceClassPreview).center().expandY();
            this.mainTable.row();

            Table buttonTable = new Table();
            buttonTable.add(this.raceClassNextButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
            buttonTable.add(this.raceClassBackButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();
            
            this.mainTable.add(buttonTable).space(MainMenuScreen.COMPONENT_SPACING).right().colspan(2).expandX().fillX();
            this.mainTable.row();
            
            this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
            this.mainTable.background(skin.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, SpriteDrawable.class));

            this.getContentTable().add(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH);
        }

        private void updateNewCharacterPreview() {
            Race race = this.raceSelectBox.getSelected();
            Class<? extends CharacterClass> charClass = CharacterClass.CLASS_LOOKUP.get(this.classSelectBox.getSelected());
            try {
                CharacterClass classInstance = charClass.newInstance();
                String textureClassString = classInstance.getTextureSetName();
                String texture = race.getTextureString() + "-" + textureClassString + "-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME;
                TextureRegionDrawable drawable = new TextureRegionDrawable(EuropaGame.game.getAssetManager().get(FileLocations.SPRITES_DIRECTORY.resolve(
                        "CharacterSprites.atlas").toString(),
                        TextureAtlas.class).findRegion(texture));
                this.raceClassPreview.setDrawable(drawable);
            }
            catch (IllegalAccessException | InstantiationException ex) {

            }
        }

        private void onRaceClassBackButton() {
            this.exitState = SelectRaceClassExitStates.CANCELED;
            this.hide();
        }

        private void onRaceClassNextButton() {
            this.exitState = SelectRaceClassExitStates.OK;
            this.hide();
        }

    }

}
