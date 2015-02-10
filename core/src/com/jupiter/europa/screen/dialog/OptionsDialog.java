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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jupiter.europa.EuropaGame;
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

    private Table optionsTable;
    private Label optionsLabel;
    private TabbedPane optionsPane;
    private TextButton optionsAcceptButton;
    private TextButton optionsCancelButton;
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
        
        this.addDialogListener((args) -> this.loadSettings(), DialogEvents.SHOWN);
    }


    // Private Methods
    private void initComponent() {
        this.optionsTable = new Table();
        this.optionsTable.center();
        this.optionsLabel = new Label("Options", skin.get(DEFAULT_KEY, Label.LabelStyle.class));
        this.optionsPane = new TabbedPane(skin.get(TAB_STYLE_KEY, TextButton.TextButtonStyle.class));
        this.optionsAcceptButton = new TextButton("Accept", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.optionsAcceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !OptionsDialog.this.optionsAcceptButton.isDisabled()) {
                    OptionsDialog.this.applySettings();
                    OptionsDialog.this.hide();
                }
            }
        });
        this.optionsCancelButton = new TextButton("Cancel", skin.get(DEFAULT_KEY, TextButton.TextButtonStyle.class));
        this.optionsCancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !OptionsDialog.this.optionsCancelButton.isDisabled()) {
                    OptionsDialog.this.hide();
                }
            }
        });

        this.optionsButtonTable = new Table();
        this.optionsButtonTable.add(this.optionsAcceptButton).right();
        this.optionsButtonTable.add(this.optionsCancelButton).right().space(20);

        // Create and Add Tabs
        this.audioTable = new AudioOptionsTable(this.skin);
        this.graphicsTable = new GraphicOptionsTable(this.skin);
        this.optionsPane.addTab("Audio", this.audioTable);
        this.optionsPane.addTab("Graphics", this.graphicsTable);

        this.optionsTable.add(this.optionsLabel).left().padTop(25).minWidth(600);
        this.optionsTable.row();
        this.optionsTable.add(this.optionsPane).expandY().fill().top();
        this.optionsTable.row();
        this.optionsTable.add(this.optionsButtonTable).padBottom(25);

        this.getContentTable().add(this.optionsTable).expand().fill();
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

            this.musicSlider = new Slider(0f, 1f, 0.05f, false, skin.get(DEFAULT_KEY, Slider.SliderStyle.class));
            this.add(this.musicSlider).minHeight(20).minWidth(600).maxWidth(1000).expandX();
            this.row();
        }

    }


    private static class GraphicOptionsTable extends Table {


        // Fields
        private final Skin skin;


        // Initialization
        private GraphicOptionsTable(Skin skin) {
            this.skin = skin;
        }

    }

}
