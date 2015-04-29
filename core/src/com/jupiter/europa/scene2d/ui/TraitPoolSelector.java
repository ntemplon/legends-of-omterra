/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.jupiter.europa.entity.trait.Trait;
import com.jupiter.europa.entity.trait.TraitPool;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public class TraitPoolSelector<T extends Trait> extends Table {

    // Fields
    private final Event<TraitSelectorEventArgs<T>> traitClicked = new Event<>();
    private final Map<T, SourceTrait<T>> sourceMap = new HashMap<>();
    private final Map<T, SelectedTrait<T>> selectedMap = new HashMap<>();

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

    public boolean addTraitClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
        return this.traitClicked.addListener(listener);
    }

    public boolean removeTraitClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
        return this.traitClicked.removeListener(listener);
    }
    
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.getCell(this.sourceTable).width(width / 2.0f);
        this.getCell(this.selectedTable).width(width / 2.0f);
    }


    // Private Methods
    private void initComponent() {
        this.sourceTable = new Table();
        this.sourceMap.putAll(this.pool.getQualified().stream()
                .collect(Collectors.toMap(
                                (T sourceItem) -> sourceItem,
                                (T sourceItem) -> new SourceTrait<>(sourceItem)
                        )));
        this.sourceMap.keySet().stream()
                .map((T item) -> this.sourceMap.get(item))
                .forEach((SourceTrait<T> entry) -> {
                    entry.addAdditionClickListener(this::onAddTraitClick);
                    entry.addClickListener(this::onTraitInfoClick);
                    this.sourceTable.add(entry).right().space(this.spacing).fillX();
                    this.sourceTable.row();
                });
        this.sourcePane = new ScrollPane(this.sourceTable, this.scrollPaneStyle);

        this.selectedTable = new Table();
        this.selectedMap.putAll(this.pool.getSelections().stream()
                .collect(Collectors.toMap(
                                (T selectedItem) -> selectedItem,
                                (T selectedItem) -> new SelectedTrait<>(selectedItem)
                        )));
        this.selectedMap.keySet().stream()
                .map((T item) -> this.selectedMap.get(item))
                .forEach((SelectedTrait<T> entry) -> {
                    entry.addRemovalClickListener(this::onRemoveTraitClick);
                    entry.addClickListener(this::onTraitInfoClick);
                    entry.setEnabled(false);
                    this.selectedTable.add(entry).left().space(MainMenuScreen.COMPONENT_SPACING).fillX();
                    this.selectedTable.row();
                });
        this.selectedPane = new ScrollPane(this.selectedTable, this.scrollPaneStyle);

        this.add(this.sourcePane).center().expand().fill().space(this.spacing).width(this.getWidth() / 2.0f);
        this.add(this.selectedPane).center().expand().fill().space(this.spacing).width(this.getWidth() / 2.0f);
    }

    private void onAddTraitClick(TraitSelectorEventArgs<T> args) {
        SourceTrait<T> source = this.sourceMap.get(args.trait);
        this.sourceMap.remove(args.trait);
        
        // Remove it from the table
        Array<Cell> cells = this.sourceTable.getCells();
        boolean removed = false;
        for (int i = 0; i < cells.size; i++) {
            Cell cell = cells.get(i);
            if (removed) {
                cells.set(i - 1, cell);
            }
            else if (cell.getActor().equals(source)) {
                removed = true;
                cell.clearActor();
            }
        }
        cells.removeIndex(cells.size - 1);

        // Add it to the other table
        SelectedTrait<T> selected = new SelectedTrait<>(args.trait);
        selected.addRemovalClickListener(this::onRemoveTraitClick);
        selected.addClickListener(this::onTraitInfoClick);
        this.selectedMap.put(args.trait, selected);
        this.selectedTable.add(selected).left().space(MainMenuScreen.COMPONENT_SPACING).fillX();
        this.selectedTable.row();
    }

    private void onRemoveTraitClick(TraitSelectorEventArgs<T> args) {
        SelectedTrait<T> selected = this.selectedMap.get(args.trait);
        this.selectedMap.remove(args.trait);
        
        // Remove it from the table
        Array<Cell> cells = this.selectedTable.getCells();
        boolean removed = false;
        for (int i = 0; i < cells.size; i++) {
            Cell cell = cells.get(i);
            if (removed) {
                cells.set(i - 1, cell);
            }
            else if (cell.getActor().equals(selected)) {
                removed = true;
                cell.clearActor();
            }
        }
        cells.removeIndex(cells.size - 1);

        // Add it to the source table
        SourceTrait<T> source = new SourceTrait<>(args.trait);
        source.addAdditionClickListener(this::onAddTraitClick);
        source.addClickListener(this::onTraitInfoClick);
        this.sourceMap.put(args.trait, source);
        this.sourceTable.add(source).right().space(MainMenuScreen.COMPONENT_SPACING).fillX();
        this.sourceTable.row();
    }

    private void onTraitInfoClick(TraitSelectorEventArgs<T> args) {
        this.traitClicked.dispatch(args);
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
        private final Event<TraitSelectorEventArgs<T>> clicked = new Event<>();

        private final T trait;

        private Label nameLabel;
        private Button addButton;

        private boolean enabled = true;


        // Properties
        T getTraitName() {
            return this.trait;
        }

        boolean isEnabled() {
            return this.enabled;
        }

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
            this.addButton.setVisible(this.enabled);
        }


        // Initialization
        SourceTrait(T traitName) {
            this.trait = traitName;
            this.initComponent();
        }


        // Package Protected Methods
        boolean addAdditionClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.addClicked.addListener(listener);
        }

        boolean removeAdditionClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.addClicked.removeListener(listener);
        }

        boolean addClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.clicked.addListener(listener);
        }

        boolean removeClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.clicked.removeListener(listener);
        }


        // Private Methods
        private void initComponent() {
            this.nameLabel = new Label(this.trait.getName(), TraitPoolSelector.this.labelStyle);
            this.add(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING).fillX().expandX();

            this.addButton = new Button(TraitPoolSelector.this.add);
            this.addButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    if (SourceTrait.this.isEnabled()) {
                        SourceTrait.this.addClicked.dispatch(new TraitSelectorEventArgs<>(SourceTrait.this.trait));
                    }
                }
            });
            this.add(this.addButton).right().space(MainMenuScreen.COMPONENT_SPACING);

            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    SourceTrait.this.clicked.dispatch(new TraitSelectorEventArgs<>(SourceTrait.this.trait));
                }
            });
        }

    }

    class SelectedTrait<T extends Trait> extends Table {

        // Fields
        private final Event<TraitSelectorEventArgs<T>> removedClicked = new Event<>();
        private final Event<TraitSelectorEventArgs<T>> clicked = new Event<>();

        private final T trait;

        private Label nameLabel;
        private Button removeButton;

        private boolean enabled = true;


        // Properties
        T getTraitName() {
            return this.trait;
        }

        boolean isEnabled() {
            return this.enabled;
        }

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
            this.removeButton.setVisible(this.enabled);
        }


        // Initialization
        SelectedTrait(T traitName) {
            this.trait = traitName;
            this.initComponent();
        }


        // Package Protected Methods
        boolean addRemovalClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.removedClicked.addListener(listener);
        }

        boolean removeRemovalClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.removedClicked.removeListener(listener);
        }

        boolean addClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.clicked.addListener(listener);
        }

        boolean removeClickListener(Listener<TraitSelectorEventArgs<T>> listener) {
            return this.clicked.removeListener(listener);
        }


        // Private Methods
        private void initComponent() {
            this.removeButton = new Button(TraitPoolSelector.this.remove);
            this.removeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    if (SelectedTrait.this.isEnabled()) {
                        SelectedTrait.this.removedClicked.dispatch(new TraitSelectorEventArgs<>(SelectedTrait.this.trait));
                    }
                }
            });
            this.add(this.removeButton).left().space(MainMenuScreen.COMPONENT_SPACING);

            this.nameLabel = new Label(this.trait.getName(), TraitPoolSelector.this.labelStyle);
            this.add(this.nameLabel).right().space(MainMenuScreen.COMPONENT_SPACING).fillX().expandX();

            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    SelectedTrait.this.clicked.dispatch(new TraitSelectorEventArgs<>(SelectedTrait.this.trait));
                }
            });
        }

    }

    public static class TraitSelectorEventArgs<T extends Trait> {

        // Fields
        public final T trait;


        // Initialization
        private TraitSelectorEventArgs(T trait) {
            this.trait = trait;
        }

    }

}
