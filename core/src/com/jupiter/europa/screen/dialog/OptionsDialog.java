/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.scene2d.ui.EuropaButton;
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.scene2d.ui.TabbedPane;
import com.jupiter.europa.screen.MainMenuScreen;
import static com.jupiter.europa.screen.MainMenuScreen.DEFAULT_KEY;
import static com.jupiter.europa.screen.MainMenuScreen.TAB_STYLE_KEY;

/**
 *
 * @author Nathan Templon
 */
public class OptionsDialog extends ObservableDialog {

    // Enumerations
    public enum OptionsDialogExitStates {

        ACCEPT,
        CANCEL
    }


    // Constants
    private static final String DIALOG_NAME = "";


    // Static Methods
    private static Skin getSkin() {
        return MainMenuScreen.getMainMenuSkin();
    }


    // Fields
    private final Skin skin;

    private Table mainTable;
    private Label optionsLabel;
    private TabbedPane optionsPane;
    private EuropaButton optionsAcceptButton;
    private EuropaButton optionsCancelButton;
    private Table optionsButtonTable;

    private AudioOptionsTable audioTable;
    private GraphicOptionsTable graphicsTable;

    private OptionsDialogExitStates exitState;


    // Properties
    public final OptionsDialogExitStates getExitState() {
        return this.exitState;
    }


    // Initialization
    public OptionsDialog() {
        super(DIALOG_NAME, getSkin().get(WindowStyle.class));

        this.skin = getSkin();

        this.initComponent();

        this.addDialogListener((DialogEventArgs args) -> this.loadSettings(), DialogEvents.SHOWN);
    }


    // Private Methods
    private void initComponent() {
        this.mainTable = new Table();
        this.optionsLabel = new Label("Options", skin.get(DEFAULT_KEY, Label.LabelStyle.class));
        this.optionsPane = new TabbedPane(skin.get(TAB_STYLE_KEY, TextButton.TextButtonStyle.class));

        this.optionsAcceptButton = new EuropaButton("Accept", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.optionsAcceptButton.addClickListener(this::onAcceptClick);

        this.optionsCancelButton = new EuropaButton("Cancel", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.optionsCancelButton.addClickListener(this::onCancelClick);

        this.optionsButtonTable = new Table();
        this.optionsButtonTable.add(this.optionsCancelButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right()
                .expandX();
        this.optionsButtonTable.add(this.optionsAcceptButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();

        // Create and Add Tabs
        this.audioTable = new AudioOptionsTable(this.skin);
        this.graphicsTable = new GraphicOptionsTable(this.skin);
        this.optionsPane.addTab("Audio", this.audioTable);
        this.optionsPane.addTab("Graphics", this.graphicsTable);

        this.mainTable.add(this.optionsLabel).left();
        this.mainTable.row();
        this.mainTable.add(this.optionsPane).expand().fill().top();
        this.mainTable.row();
        this.mainTable.add(this.optionsButtonTable).expandX().fillX().right();

        this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
        this.mainTable.background(this.skin.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, SpriteDrawable.class));

        this.getContentTable().add(this.mainTable).center().expandY().fillY().width(MainMenuScreen.DIALOG_WIDTH);
    }

    private void onAcceptClick(ClickEvent event) {
        this.applySettings();
        this.hide();
    }
    
    private void onCancelClick(ClickEvent event) {
        this.hide();
    }

    private void loadSettings() {
        this.audioTable.setMusicVolume(EuropaGame.game.getSettings().musicVolume.get());
    }

    private void applySettings() {
        EuropaGame.game.getSettings().musicVolume.set(this.audioTable.getMusicVolume());
        EuropaGame.game.saveSettings();
    }


    // Nested Classes
    private static class AudioOptionsTable extends Table {

        // Fields
        private final Skin skin;
        private final Label volumeLabel;
        private final Slider musicSlider;


        // Properties
        public final void setMusicVolume(float volume) {
            this.musicSlider.setValue(volume);
        }

        public final float getMusicVolume() {
            return this.musicSlider.getValue();
        }


        // Initialization
        private AudioOptionsTable(Skin skin) {
            this.skin = skin;

            this.volumeLabel = new Label("Volumne:", skin.get(MainMenuScreen.INFO_STYLE_KEY, LabelStyle.class));

            this.musicSlider = new Slider(0f, 1f, 0.05f, false, skin.get(DEFAULT_KEY, Slider.SliderStyle.class));

            this.add(this.volumeLabel).space(MainMenuScreen.COMPONENT_SPACING).center().left();
            this.add(this.musicSlider).space(MainMenuScreen.COMPONENT_SPACING).center().minHeight(20).expandX().fillX();
            this.row();

            this.pad(MainMenuScreen.TABLE_PADDING);
        }

    }


    private static class GraphicOptionsTable extends Table {


        // Fields
        private final Skin skin;


        // Initialization
        private GraphicOptionsTable(Skin skin) {
            this.skin = skin;

            this.pad(MainMenuScreen.TABLE_PADDING);
        }

    }

}
