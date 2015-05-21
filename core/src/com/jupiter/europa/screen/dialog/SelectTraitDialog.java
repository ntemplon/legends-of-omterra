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

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.jupiter.europa.entity.traits.Trait;
import com.jupiter.europa.entity.traits.TraitPool;
import com.jupiter.europa.scene2d.ui.EuropaButton;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.scene2d.ui.TraitPoolSelector;
import com.jupiter.europa.scene2d.ui.TraitPoolSelector.TraitPoolSelectorStyle;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.europa.screen.MainMenuScreen.DialogExitStates;

/**
 * @param <T>
 * @author Nathan Templon
 */
public class SelectTraitDialog<T extends Trait> extends ObservableDialog {

    // Fields
    private final String title;
    private final Skin skin;
    private final TraitPool<T> pool;
    private final TraitPoolSelectorStyle selectorStyle;

    private Table mainTable;
    private Label titleLabel;
    private TraitPoolSelector<T> selector;
    private Label featNameLabel;
    private Label featDescLabel;
    private ScrollPane featDescPane;
    private Table buttonTable;
    private EuropaButton nextButton;
    private EuropaButton backButton;

    private Drawable dialogBackground;
    private DialogExitStates exitState;


    // Properties
    public final TraitPool<T> getPool() {
        return this.pool;
    }

    public final Drawable getDialogBackground() {
        return this.dialogBackground;
    }

    public final void setDialogBackground(Drawable drawable) {
        this.dialogBackground = drawable;
        this.mainTable.background(this.dialogBackground);
    }

    public DialogExitStates getExitState() {
        return this.exitState;
    }


    // Initialization
    public SelectTraitDialog(String title, Skin skin, TraitPool<T> pool) {
        super("", skin.get(WindowStyle.class));

        this.title = title;
        this.pool = pool;
        this.selectorStyle = skin.get(TraitPoolSelectorStyle.class);
        this.skin = skin;

        this.initComponent();
    }

    // Public Methods
    public void applyChanges() {
        this.selector.applyChanges();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
    }


    // Private Methods
    private void initComponent() {
        this.mainTable = new Table();

        // Title
        this.titleLabel = new Label(this.title, this.skin.get(Label.LabelStyle.class));

        this.mainTable.add(this.titleLabel).expandX().center().top();
        this.mainTable.row();

        // Selector
        this.selector = new TraitPoolSelector<>(this.selectorStyle, this.pool);
        this.selector.addTraitClickListener(args -> {
            this.featNameLabel.setText(args.trait.getName());
            this.featDescLabel.setText(args.trait.getDescription());
        });

        this.mainTable.add(this.selector).center().top().expand().fill().space(MainMenuScreen.COMPONENT_SPACING);
        this.mainTable.row();

        // Feat Descriptions
        this.featNameLabel = new Label("", this.skin.get(Label.LabelStyle.class));
        this.featNameLabel.setAlignment(Align.top);
        this.featDescLabel = new Label("", this.skin.get(MainMenuScreen.INFO_STYLE_KEY, Label.LabelStyle.class));
        this.featDescLabel.setAlignment(Align.topLeft);
        this.featDescPane = new ScrollPane(this.featDescLabel, this.skin.get(ScrollPane.ScrollPaneStyle.class));

        this.mainTable.add(this.featNameLabel).center().expandX().fillX().top().space(MainMenuScreen.COMPONENT_SPACING);
        this.mainTable.row();
        this.mainTable.add(this.featDescPane).center().expandX().fillX().top().space(MainMenuScreen.COMPONENT_SPACING).height(300);
        this.mainTable.row();

        // Buttons
        this.buttonTable = new Table();
        this.nextButton = new EuropaButton("Next", this.skin.get(TextButton.TextButtonStyle.class));
        this.nextButton.addClickListener(this::onNextButtonClick);
        this.backButton = new EuropaButton("Back", this.skin.get(TextButton.TextButtonStyle.class));
        this.backButton.addClickListener(this::onBackButtonClick);

        this.buttonTable.add(this.backButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right().expandX();
        this.buttonTable.add(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING).width(MainMenuScreen.DIALOG_BUTTON_WIDTH).right();

        this.mainTable.pad(MainMenuScreen.TABLE_PADDING);
        this.mainTable.add(this.buttonTable).space(MainMenuScreen.COMPONENT_SPACING).expandX().fillX();

        this.getContentTable().add(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH);
    }

    private void onNextButtonClick(EuropaButton.ClickEvent event) {
        this.exitState = DialogExitStates.NEXT;
        this.hide();
    }

    private void onBackButtonClick(EuropaButton.ClickEvent even) {
        this.exitState = DialogExitStates.BACK;
        this.hide();
    }
}
