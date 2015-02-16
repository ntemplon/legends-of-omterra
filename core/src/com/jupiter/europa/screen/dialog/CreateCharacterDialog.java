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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.EuropaEntity;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.entity.component.MovementResourceComponent;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.AttributeSet.Attributes;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector;
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector.AttributeSelectorStyle;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
    private final SelectAttributesDialog selectAttributes;
    private final Skin skin = getSkin();

    private CreateCharacterExitStates exitState = CreateCharacterExitStates.CANCELED;
    private EuropaEntity createdEntity;

    private Dialog lastDialog;
    private float width, height;


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
        this.selectAttributes = new SelectAttributesDialog(skin, new TextureRegionDrawable());
        this.selectAttributes.addDialogListener(this::onSelectAttributesHide, DialogEvents.HIDDEN);

        this.lastDialog = this.selectRaceClass;

        this.addDialogListener(this::onShown, DialogEvents.SHOWN);
    }


    // Public Methods
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.selectRaceClass.setSize(width, height);
        this.selectAttributes.setSize(width, height);

        this.width = width;
        this.height = height;
    }


    // Private Methods
    private void onShown(DialogEventArgs args) {
        this.showDialog(this.lastDialog);
    }

    private void onSelectRaceClassHide(DialogEventArgs args) {
        if (this.selectRaceClass.getExitState() == SelectRaceClassDialog.SelectRaceClassExitStates.OK) {
            this.selectAttributes.setCharacterPreview(this.selectRaceClass.getCharacterPortrait());
            this.showDialog(this.selectAttributes);
        }
        else {
            this.exitState = CreateCharacterExitStates.CANCELED;
            this.concludeDialog();
        }
    }

    private void onSelectAttributesHide(DialogEventArgs args) {
        if (this.selectAttributes.getExitState() == SelectAttributesDialog.SelectAttributesExitStates.NEXT) {
            this.exitState = CreateCharacterExitStates.OK;
            this.concludeDialog();
        }
        else {
            this.showDialog(this.selectRaceClass);
        }
    }

    private void showDialog(Dialog dialog) {
        dialog.show(this.getStage());
        dialog.setSize(this.width, this.height);
        this.lastDialog = dialog;
    }

    private void concludeDialog() {
        this.createdEntity = Party.createPlayer(this.selectRaceClass.getCharacterName(), CharacterClass.CLASS_LOOKUP
                .get(this.selectRaceClass.getSelectedClass()), this.selectRaceClass.getSelectedRace(), this.selectAttributes.getAttributes());
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
        private Table nameTable;
        private Label nameLabel;
        private TextField nameField;
        private SelectBox<Race> raceSelectBox;
        private SelectBox<String> classSelectBox;
        private Label titleLabel;
        private Label raceLabel;
        private Label classLabel;
        private Image raceClassPreview;
        private TextButton backButton;
        private TextButton nextButton;

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

        public final Drawable getCharacterPortrait() {
            return this.raceClassPreview.getDrawable();
        }

        public final String getCharacterName() {
            return this.nameField.getText();
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
            this.nameLabel = new Label("Name: ", skin.get(LabelStyle.class));

            this.nameField = new TextField("default", skin.get(TextFieldStyle.class));

            this.raceClassPreview = new Image(EuropaGame.game.getAssetManager()
                    .get(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(),
                            TextureAtlas.class).findRegion(Race.PlayerRaces.Human.getTextureString() + "-champion-"
                            + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME));
            this.raceClassPreview.setScale(IMAGE_SCALE);

            this.raceSelectBox = new SelectBox<>(skin.get(SelectBox.SelectBoxStyle.class));
            this.raceSelectBox.setItems(Race.PlayerRaces.values());
            this.raceSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SelectRaceClassDialog.this.updateNewCharacterPreview();
                }
            });

            this.classSelectBox = new SelectBox<>(skin.get(SelectBox.SelectBoxStyle.class));
            this.classSelectBox.setItems(CharacterClass.AVAILABLE_CLASSES);
            this.classSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SelectRaceClassDialog.this.updateNewCharacterPreview();
                }
            });

            this.backButton = new TextButton("Back", skin.get(TextButton.TextButtonStyle.class));
            this.backButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !SelectRaceClassDialog.this.backButton.isDisabled()) {
                        SelectRaceClassDialog.this.onRaceClassBackButton();
                    }
                }
            });

            this.nextButton = new TextButton("Next", skin.get(TextButton.TextButtonStyle.class));
            this.nextButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !SelectRaceClassDialog.this.nextButton.isDisabled()) {
                        SelectRaceClassDialog.this.onRaceClassNextButton();
                    }
                }
            });

            this.nameTable = new Table();
            this.nameTable.add(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING);
            this.nameTable.add(this.nameField).space(MainMenuScreen.COMPONENT_SPACING).padTop(10).expandX().fillX();

            this.mainTable = new Table();

            this.selectBoxTable = new Table();
            this.selectBoxTable.add(this.raceLabel).fillX();
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.raceSelectBox).minWidth(MainMenuScreen.TITLE_BUTTON_WIDTH).fillX();
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.classLabel).fillX();
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.classSelectBox).minWidth(MainMenuScreen.TITLE_BUTTON_WIDTH).fillX();
            this.selectBoxTable.row();

            this.mainTable.add(this.titleLabel).center().colspan(2);
            this.mainTable.row();
            this.mainTable.add(new Image()).expandY().fillY();
            this.mainTable.row();
            this.mainTable.add(this.nameTable).colspan(2).expandX().fillX().bottom();
            this.mainTable.row();
            this.mainTable.add(this.selectBoxTable).left();
            this.mainTable.add(this.raceClassPreview).center();
            this.mainTable.row();
            this.mainTable.add(new Image()).expandY().fillY();
            this.mainTable.row();

            Table buttonTable = new Table();
            buttonTable.add(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
            buttonTable.add(this.backButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();

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

    private static class SelectAttributesDialog extends ObservableDialog {

        // Enumerations
        public enum SelectAttributesExitStates {

            NEXT,
            BACK
        }


        // Constants
        private static final String DIALOG_NAME = "";
        private static final String TITLE = "Select Attributes";


        // Fields
        private final Image characterPreview;
        private final Skin skin;

        private Table mainTable;
        private Label titleLabel;
        private MultipleNumberSelector attributeSelector;
        private Table buttonTable;
        private TextButton nextButton;
        private TextButton backButton;

        private SelectAttributesExitStates exitState;


        // Properties
        public final SelectAttributesExitStates getExitState() {
            return this.exitState;
        }

        public final Drawable getCharacterPreview() {
            return this.characterPreview.getDrawable();
        }

        public final void setCharacterPreview(Drawable preview) {
            this.characterPreview.setDrawable(preview);
        }

        public final AttributeSet getAttributes() {
            AttributeSet set = new AttributeSet();
            
            Map<String, Integer> values = this.attributeSelector.getValues();
            values.keySet().stream().forEach((String attrName) -> {
                set.setAttribute(Attributes.getByDisplayName(attrName), values.get(attrName));
            });
            
            return set;
        }


        // Initialization
        private SelectAttributesDialog(Skin skin, Drawable characterPreview) {
            super(DIALOG_NAME, skin.get(WindowStyle.class));

            this.characterPreview = new Image(characterPreview);
            this.skin = skin;

            this.initComponents();
        }


        // Private Methods
        private void initComponents() {
            this.titleLabel = new Label(TITLE, skin.get(LabelStyle.class));

            this.mainTable = new Table();

            // Create Attribute Selector
            Set<String> attributeNames = new LinkedHashSet<>();
            AttributeSet.PRIMARY_ATTRIBUTES.stream().forEach((Attributes attr) -> attributeNames.add(attr.getDisplayName()));
            this.attributeSelector = new MultipleNumberSelector(50, this.skin.get(AttributeSelectorStyle.class), Collections.unmodifiableSet(attributeNames));

            this.nextButton = new TextButton("Next", skin.get(TextButton.TextButtonStyle.class));
            this.nextButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !SelectAttributesDialog.this.nextButton.isDisabled()) {
                        SelectAttributesDialog.this.onNextButton();
                    }
                }
            });

            this.backButton = new TextButton("Back", skin.get(TextButton.TextButtonStyle.class));
            this.backButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == Input.Buttons.LEFT && !SelectAttributesDialog.this.backButton.isDisabled()) {
                        SelectAttributesDialog.this.onBackButton();
                    }
                }
            });

            this.buttonTable = new Table();
            this.buttonTable.add(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
            this.buttonTable.add(this.backButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();

            this.mainTable.add(this.titleLabel).colspan(2).center().top();
            this.mainTable.row();
            this.mainTable.add(this.attributeSelector).expandY().center().left();
            this.mainTable.row();
            this.mainTable.add(buttonTable).space(MainMenuScreen.COMPONENT_SPACING).bottom().right().colspan(2).expandX().fillX();
            this.mainTable.row();

            this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
            this.mainTable.background(skin.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, SpriteDrawable.class));

            this.getContentTable().add(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH);
        }

        private void onNextButton() {
            this.exitState = SelectAttributesExitStates.NEXT;
            this.hide();
        }

        private void onBackButton() {
            this.exitState = SelectAttributesExitStates.BACK;
            this.hide();
        }

    }

}
