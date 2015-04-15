/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.jupiter.europa.entity.trait.Trait;
import com.jupiter.europa.entity.trait.TraitPool;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public class TraitPoolSelector<T extends Trait> extends Table {

    // Fields
    private final Drawable add;
    private final Drawable remove;
    private final int spacing;
    private final LabelStyle labelStyle;
    private final ScrollPaneStyle scrollPaneStyle;
    private final TraitPool<T> pool;

    private ScrollPane sourcePane;
    private ScrollPane selectedPane;
    private Table sourceTable;
    private Table selectedTable;


    // Initialization
    public TraitPoolSelector(TraitPoolSelectorStyle style, TraitPool<T> pool) {
        this.add = style.add;
        this.remove = style.remove;
        this.spacing = style.spacing;
        this.labelStyle = style.labelStyle;
        this.scrollPaneStyle = style.scrollPaneStyle;
        this.pool = pool;

        this.initComponent();
    }

    public TraitPoolSelector(Skin skin, TraitPool<T> pool) {
        this(skin.get(TraitPoolSelectorStyle.class), pool);
    }

    public TraitPoolSelector(Skin skin, String styleName, TraitPool<T> pool) {
        this(skin.get(styleName, TraitPoolSelectorStyle.class), pool);
    }


    // Public Methods
    /**
     * Applies the changes selected by the user to the provided trait pool.
     */
    public void applyChanges() {

    }


    // Private Methods
    private void initComponent() {
        this.sourceTable = new Table();
        this.pool.getQualified().stream()
                .map((T sourceItem) -> new SourceTrait<>(sourceItem))
                .forEach((SourceTrait<T> entry) -> {
                    entry.addClickListener(this::onAddTraitClick);
                    this.sourceTable.add(entry).left().space(this.spacing).fillX().expandX();
                    this.sourceTable.row();
                });
        this.sourcePane = new ScrollPane(this.sourceTable, this.scrollPaneStyle);

        this.selectedTable = new Table();
        this.pool.getSelections().stream()
                .map((T sourceItem) -> new SelectedTrait<>(sourceItem))
                .forEach((SelectedTrait<T> entry) -> {
                    entry.addClickListener(this::onRemoveTraitClick);
                    this.selectedTable.add(entry).left().space(MainMenuScreen.COMPONENT_SPACING).fillX().expandX();
                    this.selectedTable.row();
                });
        this.selectedPane = new ScrollPane(this.selectedTable, this.scrollPaneStyle);
        
        this.add(this.sourcePane).center().expand().space(this.spacing);
        this.add(this.selectedPane).center().expand().space(this.spacing);
    }

    private void onAddTraitClick(TraitSelectorEventArgs<T> args) {

    }

    private void onRemoveTraitClick(TraitSelectorEventArgs<T> args) {

    }


    // Nested Classes
    public static class TraitPoolSelectorStyle {

        // Fields
        public Drawable add;
        public Drawable remove;
        public int spacing;
        public LabelStyle labelStyle;
        public ScrollPaneStyle scrollPaneStyle;


        // Initialization
        public TraitPoolSelectorStyle() {

        }

    }

    class SourceTrait<T extends Trait> extends Table {

        // Fields
        private final Event<TraitSelectorEventArgs<T>> addClicked = new Event<>();

        private final T trait;

        private Label nameLabel;
        private Button addButton;


        // Properties
        T getTraitName() {
            return this.trait;
        }


        // Initialization
        SourceTrait(T traitName) {
            this.trait = traitName;
            this.initComponent();
        }


        // Package Protected Methods
        boolean addClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.addClicked.addListener(listener);
        }

        boolean removeClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.addClicked.removeListener(listener);
        }


        // Private Methods
        private void initComponent() {
            this.nameLabel = new Label(this.trait.getName(), TraitPoolSelector.this.labelStyle);
            this.add(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING).fillX().expandX();
            this.row();

            this.addButton = new Button(TraitPoolSelector.this.add);
            this.add(this.addButton).left().space(MainMenuScreen.COMPONENT_SPACING);
        }

    }

    class SelectedTrait<T extends Trait> extends Table {

        // Fields
        private final Event<TraitSelectorEventArgs<T>> removedClicked = new Event<>();

        private final T trait;

        private Label nameLabel;
        private Button removeButton;


        // Properties
        T getTraitName() {
            return this.trait;
        }


        // Initialization
        SelectedTrait(T traitName) {
            this.trait = traitName;
            this.initComponent();
        }


        // Package Protected Methods
        boolean addClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.removedClicked.addListener(listener);
        }

        boolean removeClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.removedClicked.removeListener(listener);
        }


        // Private Methods
        private void initComponent() {
            this.nameLabel = new Label(this.trait.getName(), TraitPoolSelector.this.labelStyle);
            this.add(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING).fillX().expandX();
            this.row();

            this.removeButton = new Button(TraitPoolSelector.this.remove);
            this.removeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    SelectedTrait.this.removedClicked.dispatch(new TraitSelectorEventArgs<>(SelectedTrait.this.trait,
                            TraitSelectorEventArgs.TraitSelectorEventTypes.REMOVED));
                }
            });
            this.add(this.removeButton).left().space(MainMenuScreen.COMPONENT_SPACING);
        }

    }

    private static class TraitSelectorEventArgs<T extends Trait> {

        // Enumerations
        private enum TraitSelectorEventTypes {

            SELECTED,
            REMOVED;
        }


        // Fields
        private final T trait;
        private final TraitSelectorEventTypes type;


        // Initialization
        private TraitSelectorEventArgs(T trait, TraitSelectorEventTypes type) {
            this.trait = trait;
            this.type = type;
        }

    }

}
