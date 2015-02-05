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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.EuropaEntity;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.entity.component.MovementResourceComponent;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.io.FileLocations;
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
    }


    // Public Methods
    @Override
    public Dialog show(Stage stage) {
        Dialog result = super.show(stage);

        this.selectRaceClass.show(stage);

        return result;
    }


    // Private Methods
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
        private static final String DIALOG_NAME = "Select a Race and Class";


        // Fields
        private final Skin skin;

        private Table selectRaceClassTable;
        private Table selectRaceClassBoxTable;
        private SelectBox<Race> raceSelectBox;
        private SelectBox<String> classSelectBox;
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
            this.raceLabel = new Label("Race: ", skin.get(LabelStyle.class));
            this.classLabel = new Label("Class: ", skin.get(LabelStyle.class));
            this.raceClassPreview = new Image(EuropaGame.game.getAssetManager()
                    .get(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(),
                            TextureAtlas.class).findRegion("human-champion-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME));
            this.raceClassPreview.setScale(3);

            this.raceSelectBox = new SelectBox(skin.get(SelectBox.SelectBoxStyle.class));
            this.raceSelectBox.setItems(Race.PlayerRaces.values());
            this.raceSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SelectRaceClassDialog.this.updateNewCharacterPreview();
                }
            });

            this.classSelectBox = new SelectBox(skin.get(SelectBox.SelectBoxStyle.class));
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

            this.selectRaceClassTable = new Table();

            this.selectRaceClassBoxTable = new Table();
            this.selectRaceClassBoxTable.add(this.raceLabel).left();
            this.selectRaceClassBoxTable.row();
            this.selectRaceClassBoxTable.add(this.raceSelectBox).left();
            this.selectRaceClassBoxTable.row();
            this.selectRaceClassBoxTable.add(this.classLabel).left();
            this.selectRaceClassBoxTable.row();
            this.selectRaceClassBoxTable.add(this.classSelectBox).left();
            this.selectRaceClassBoxTable.row();

            this.selectRaceClassTable.add(this.selectRaceClassBoxTable).left();
            this.selectRaceClassTable.add(this.raceClassPreview).expandX().center();
            this.selectRaceClassTable.row();

            Table selectRaceClassButtonTable = new Table();
            selectRaceClassButtonTable.add(this.raceClassNextButton).space(20).right().expandX();
            selectRaceClassButtonTable.add(this.raceClassBackButton).space(20).right();

            this.getContentTable().add(this.selectRaceClassTable).minWidth(450);
            this.getButtonTable().add(selectRaceClassButtonTable).minWidth(450);
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
