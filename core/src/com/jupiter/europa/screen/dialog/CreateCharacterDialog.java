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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.EuropaEntity;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.entity.component.CharacterClassComponent;
import com.jupiter.europa.entity.component.MovementResourceComponent;
import com.jupiter.europa.entity.component.SkillsComponent;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.SkillSet;
import com.jupiter.europa.entity.stats.SkillSet.Skills;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.scene2d.ui.AttributeSelector;
import com.jupiter.europa.scene2d.ui.EuropaButton;
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent;
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector;
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector.MultipleNumberSelectorStyle;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.europa.screen.MainMenuScreen.DialogExitStates;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final SelectRaceClassAttributesDialog selectRaceClass;
    private SelectSkillsDialog selectSkills;
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

        this.selectRaceClass = new SelectRaceClassAttributesDialog(skin);
        this.selectRaceClass.addDialogListener(this::onSelectRaceClassHide, DialogEvents.HIDDEN);
        this.selectSkills = new SelectSkillsDialog(skin, 8, 4, Arrays.asList(Skills.values()));
        this.selectSkills.addDialogListener(this::onSelectSkillsHide, DialogEvents.HIDDEN);

        this.lastDialog = this.selectRaceClass;

        this.addDialogListener(this::onShown, DialogEvents.SHOWN);
    }


    // Public Methods
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.selectRaceClass.setSize(width, height);
        if (this.selectSkills != null) {
            this.selectSkills.setSize(width, height);
        }

        this.width = width;
        this.height = height;
    }


    // Private Methods
    private void onShown(DialogEventArgs args) {
        this.showDialog(this.lastDialog);
    }

    private void onSelectRaceClassHide(DialogEventArgs args) {
        if (this.selectRaceClass.getExitState() == DialogExitStates.NEXT) {
            this.createEntity();

            SkillsComponent skills = Mappers.skills.get(this.createdEntity);
            List<Skills> skillList = skills.getClassSkills();
            CharacterClassComponent classComp = Mappers.characterClass.get(this.createdEntity);

            this.selectSkills = new SelectSkillsDialog(this.skin, classComp.getCharacterClass().getAvailableSkillPoints() - skills.getSkillPointsSpent(),
                    classComp.getCharacterClass().getMaxPointsPerSkill(), skillList);
            this.selectSkills.addDialogListener(this::onSelectSkillsHide, DialogEvents.HIDDEN);
            this.showDialog(this.selectSkills);
        }
        else {
            this.exitState = CreateCharacterExitStates.CANCELED;
            this.concludeDialog();
        }
    }

    private void onSelectSkillsHide(DialogEventArgs args) {
        if (this.selectSkills.getExitState() == DialogExitStates.NEXT) {
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
        if (this.createdEntity != null) {
            SkillsComponent comp = Mappers.skills.get(this.createdEntity);

            if (comp != null) {
                SkillSet skills = comp.getSkills();

                Map<Skills, Integer> skillLevels = this.selectSkills.getSelectedSkills();

                skillLevels.keySet().stream().forEach((Skills skill) ->
                        skills.setSkill(skill, skillLevels.get(skill))
                );
            }
        }

        this.hide();
    }

    private void createEntity() {
        this.createdEntity = Party.createPlayer(this.selectRaceClass.getCharacterName(), CharacterClass.CLASS_LOOKUP
                .get(this.selectRaceClass.getSelectedClass()), this.selectRaceClass.getSelectedRace(), this.selectRaceClass.getAttributes());
    }


    // Nested Classes
    private static class SelectRaceClassAttributesDialog extends ObservableDialog {

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
        private EuropaButton backButton;
        private EuropaButton nextButton;
        private AttributeSelector attributeSelector;

        private DialogExitStates exitState = DialogExitStates.BACK;


        // Properties
        public DialogExitStates getExitState() {
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

        public final AttributeSet getAttributes() {
            return this.attributeSelector.getAttributes();
        }


        // Initialization
        private SelectRaceClassAttributesDialog(Skin skin) {
            super(DIALOG_NAME, skin.get(WindowStyle.class));
            this.skin = skin;

            this.initComponents();

            this.addDialogListener((DialogEventArgs args) -> {
                this.nextButton.setChecked(false);
                this.backButton.setChecked(false);
            },
                    DialogEvents.SHOWN);
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
                    SelectRaceClassAttributesDialog.this.updateNewCharacterPreview();
                }
            });

            this.classSelectBox = new SelectBox<>(skin.get(SelectBox.SelectBoxStyle.class));
            this.classSelectBox.setItems(CharacterClass.AVAILABLE_CLASSES);
            this.classSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SelectRaceClassAttributesDialog.this.updateNewCharacterPreview();
                }
            });

            this.backButton = new EuropaButton("Back", skin.get(TextButton.TextButtonStyle.class));
            this.backButton.addClickListener(this::onRaceClassBackButton);

            this.nextButton = new EuropaButton("Next", skin.get(TextButton.TextButtonStyle.class));
            this.nextButton.addClickListener(this::onRaceClassNextButton);
            
            this.attributeSelector = new AttributeSelector(50, skin.get(MultipleNumberSelectorStyle.class), 2);

            this.nameTable = new Table();
            this.nameTable.add(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING);
            this.nameTable.add(this.nameField).space(MainMenuScreen.COMPONENT_SPACING).padTop(10).expandX().fillX();

            this.mainTable = new Table();

            this.selectBoxTable = new Table();
            this.selectBoxTable.add(this.raceLabel).fillX().space(MainMenuScreen.COMPONENT_SPACING);
            this.selectBoxTable.add(this.classLabel).fillX().space(MainMenuScreen.COMPONENT_SPACING).spaceLeft(5 * MainMenuScreen.COMPONENT_SPACING);
            this.selectBoxTable.row();
            this.selectBoxTable.add(this.raceSelectBox).minWidth(MainMenuScreen.TITLE_BUTTON_WIDTH).fillX();
            this.selectBoxTable.add(this.classSelectBox).minWidth(MainMenuScreen.TITLE_BUTTON_WIDTH).fillX().spaceLeft(5 * MainMenuScreen.COMPONENT_SPACING);
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
            this.mainTable.add(this.attributeSelector).colspan(2).center().top().padTop(40);
            this.mainTable.row();
            this.mainTable.add(new Image()).expandY().fillY();
            this.mainTable.row();

            Table buttonTable = new Table();
            buttonTable.add(this.backButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
            buttonTable.add(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();

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

        private void onRaceClassBackButton(ClickEvent event) {
            this.exitState = DialogExitStates.BACK;
            this.hide();
        }

        private void onRaceClassNextButton(ClickEvent event) {
            this.exitState = DialogExitStates.NEXT;
            this.hide();
        }

    }
    

    private static class SelectSkillsDialog extends ObservableDialog {

        // Constants
        private static final String DIALOG_TITLE = "";
        private static final String TITLE = "Select Skills";


        // Fields
        private final Skin skin;

        private final List<Skills> selectableSkills;
        private final int skillPointsAvailable;
        private final int maxPerSkill;

        private Table mainTable;
        private Label titleLabel;
        private MultipleNumberSelector skillSelector;
        private Table buttonTable;
        private EuropaButton nextButton;
        private EuropaButton backButton;

        private DialogExitStates exitState;


        // Properties
        public final DialogExitStates getExitState() {
            return this.exitState;
        }

        public final int getMaxPointsPerSkill() {
            return this.maxPerSkill;
        }

        public final Map<Skills, Integer> getSelectedSkills() {
            Map<Skills, Integer> values = new HashMap<>();
            Map<String, Integer> selected = this.skillSelector.getValues();
            selected.keySet().stream()
                    .forEach((String key) ->
                            values.put(Skills.getByDisplayName(key), selected.get(key))
                    );
            return values;
        }


        // Initialization
        private SelectSkillsDialog(Skin skin, int skillPointsAvailable, int maxPerSkill, List<Skills> selectableSkills) {
            super(DIALOG_TITLE, skin.get(WindowStyle.class));

            this.skin = skin;
            this.selectableSkills = selectableSkills;
            this.skillPointsAvailable = skillPointsAvailable;
            this.maxPerSkill = maxPerSkill;

            this.initComponent();
        }


        // Private Methods
        private void initComponent() {
            this.titleLabel = new Label(TITLE, skin.get(LabelStyle.class));

            this.mainTable = new Table();

            // Create Attribute Selector
            List<String> skillNames = this.selectableSkills.stream().map((Skills skill) -> skill.getDisplayName()).collect(Collectors.toList());
            this.skillSelector = new MultipleNumberSelector(this.skillPointsAvailable, this.skin.get(MultipleNumberSelectorStyle.class), Collections
                    .unmodifiableList(skillNames), 2);
            this.skillSelector.setUseMaximumNumber(true);
            this.skillSelector.setMaximumNumber(this.maxPerSkill);
            this.skillSelector.setIncrement(5);

            this.nextButton = new EuropaButton("Next", skin.get(TextButton.TextButtonStyle.class));
            this.nextButton.addClickListener(this::onNextButton);

            this.backButton = new EuropaButton("Back", skin.get(TextButton.TextButtonStyle.class));
            this.backButton.addClickListener(this::onBackButton);

            this.buttonTable = new Table();
            this.buttonTable.add(this.backButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
            this.buttonTable.add(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();

            this.mainTable.add(this.titleLabel).center().top();
            this.mainTable.row();
            this.mainTable.add(this.skillSelector).expandY().center().top();
            this.mainTable.row();
            this.mainTable.add(buttonTable).space(MainMenuScreen.COMPONENT_SPACING).bottom().right().expandX().fillX();
            this.mainTable.row();

            this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
            this.mainTable.background(skin.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, SpriteDrawable.class));

            this.getContentTable().add(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH);
        }

        private void onNextButton(ClickEvent event) {
            this.exitState = DialogExitStates.NEXT;
            this.hide();
        }

        private void onBackButton(ClickEvent event) {
            this.exitState = DialogExitStates.BACK;
            this.hide();
        }

    }

}
