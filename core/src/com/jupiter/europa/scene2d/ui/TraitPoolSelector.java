/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.jupiter.europa.entity.traits.Trait;
import com.jupiter.europa.entity.traits.TraitPool;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @param <T>
 * @author Nathan Templon
 */
public class TraitPoolSelector<T extends Trait> extends Table {

    // Constants
    private static final String REMAINING_HEADER = "Selections Remaining: ";


    // Fields
    private final Event<TraitSelectorEventArgs<T>> traitClicked = new Event<>();
    private final Map<T, SourceTrait<T>> sourceMap = new TreeMap<>((first, second) -> {
        return first.getName().compareTo(second.getName());
    });
    private final Map<T, SelectedTrait<T>> selectedMap = new TreeMap<>((first, second) -> {
        return first.getName().compareTo(second.getName());
    });

    private final Drawable add;
    private final Drawable remove;
    private final int spacing;
    private final LabelStyle labelStyle;
    private final ScrollPaneStyle scrollPaneStyle;
    private final Drawable backImage;
    private final Drawable selectedBackground;
    private final Drawable unselectedBackground;
    private final TraitPool<T> pool;

    private ScrollPane sourcePane;
    private ScrollPane selectedPane;
    private VerticalGroup sourceTable;
    private VerticalGroup selectedTable;
    private Label sourceLabel;
    private Label selectedLabel;
    private Label remainingLabel;

    private String sourceTitle = "Available";
    private String selectedTitle = "Selected";


    // Properties
    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String value) {
        this.sourceTitle = value;
    }

    public String getSelectedTitle() {
        return this.selectedTitle;
    }

    public void setSelectedTitle(String value) {
        this.selectedTitle = value;
    }


    // Initialization
    public TraitPoolSelector(TraitPoolSelectorStyle style, TraitPool<T> pool) {
        this.add = style.add;
        this.remove = style.remove;
        this.spacing = style.spacing;
        this.labelStyle = style.labelStyle;
        this.scrollPaneStyle = style.scrollPaneStyle;
        this.backImage = style.background;
        this.selectedBackground = style.selectedBackground;
        this.unselectedBackground = style.unselectedBackground;
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
        Arrays.asList(this.selectedTable.getChildren().toArray()).stream()
                .filter(actor -> actor instanceof SelectedTrait)
                .map(actor -> (SelectedTrait<T>) actor)
                .filter(SelectedTrait<T>::isEnabled)
                .forEach(wrapper -> this.pool.select(wrapper.getTrait()));
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
        this.background(this.backImage);

        // Remaining Label
        this.remainingLabel = new Label(REMAINING_HEADER + (this.pool.getCapacity() - this.pool.getNumberOfSelections()), this.labelStyle);
        this.add(this.remainingLabel).center().colspan(2);
        this.row();

        // Configure and add Labels
        this.sourceLabel = new Label(this.getSourceTitle(), this.labelStyle);
        this.selectedLabel = new Label(this.getSelectedTitle(), this.labelStyle);

        this.add(this.sourceLabel).center().space(this.spacing);
        this.add(this.selectedLabel).center().space(this.spacing);
        this.row();

        // Configure and add Tables
        this.sourceTable = new VerticalGroup();
        this.sourceTable.space(this.spacing);
        this.sourceTable.right();
        this.sourceTable.fill();
        this.sourceMap.putAll(this.pool.getSources().stream()
                .collect(Collectors.toMap(
                        (T sourceItem) -> sourceItem,
                        SourceTrait<T>::new
                )));
        this.sourceMap.keySet().stream()
                .map(this.sourceMap::get)
                .forEach((SourceTrait<T> entry) -> {
                    entry.addAdditionClickListener(this::onAddTraitClick);
                    entry.addClickListener(this::onTraitInfoClick);
                    entry.setEnabled(this.pool.getQualified().contains(entry.trait));
                    this.sourceTable.addActor(entry);
                });
        this.sourcePane = new ScrollPane(this.sourceTable, this.scrollPaneStyle);

        this.selectedTable = new VerticalGroup();
        this.selectedTable.space(this.spacing);
        this.selectedTable.left();
        this.selectedTable.fill();
        this.selectedMap.putAll(this.pool.getSelections().stream()
                .collect(Collectors.toMap(
                        (T selectedItem) -> selectedItem,
                        (T selectedItem) -> new SelectedTrait<>(selectedItem)
                )));
        this.selectedMap.keySet().stream()
                .map(this.selectedMap::get)
                .forEach((SelectedTrait<T> entry) -> {
                    entry.addRemovalClickListener(this::onRemoveTraitClick);
                    entry.addClickListener(this::onTraitInfoClick);
                    entry.setEnabled(false);
                    this.selectedTable.addActor(entry);
                });
        this.selectedPane = new ScrollPane(this.selectedTable, this.scrollPaneStyle);

        this.add(this.sourcePane).center().expand().fill().space(this.spacing).padRight(3 * this.spacing).width(this.getWidth() / 2.0f);
        this.add(this.selectedPane).center().expand().fill().space(this.spacing).padLeft(3 * this.spacing).width(this.getWidth() / 2.0f);
    }

    private void onAddTraitClick(TraitSelectorEventArgs<T> args) {
        SourceTrait<T> source = this.sourceMap.get(args.trait);
        this.sourceMap.remove(args.trait);

        // Remove it from the table
        this.sourceTable.removeActor(source);

        // Add it to the other table
        SelectedTrait<T> selected = new SelectedTrait<>(args.trait);
        selected.addRemovalClickListener(this::onRemoveTraitClick);
        selected.addClickListener(this::onTraitInfoClick);
        this.selectedMap.put(args.trait, selected);

        // Move it to the correct position
        this.selectedTable.addActor(selected);

        this.updateRemaining();
    }

    private void onRemoveTraitClick(TraitSelectorEventArgs<T> args) {
        SelectedTrait<T> selected = this.selectedMap.get(args.trait);
        this.selectedMap.remove(args.trait);

        // Remove it from the table
        this.selectedTable.removeActor(selected);

        // Add it to the source table
        SourceTrait<T> source = new SourceTrait<>(args.trait);
        source.addAdditionClickListener(this::onAddTraitClick);
        source.addClickListener(this::onTraitInfoClick);
        this.sourceMap.put(args.trait, source);

        // Move it to the correct position
        int index = Collections.binarySearch(Arrays.asList(this.sourceTable.getChildren().toArray()), source, (Actor first, Actor second) -> {
            if (first instanceof Traitable && second instanceof Traitable) {
                Traitable tFirst = (Traitable) first;
                Traitable tSecond = (Traitable) second;
                return tFirst.getTrait().getName().compareTo(tSecond.getTrait().getName());
            } else {
                return 0;
            }
        });
        this.sourceTable.addActorAt(Math.abs(index) - 1, source);

        this.updateRemaining();
    }

    private void onTraitInfoClick(TraitSelectorEventArgs<T> args) {
        Traitable trait = args.wrapper;
        this.selectedMap.values().stream()
                .filter(other -> other != trait)
                .forEach(other -> other.setShaded(false));
        this.sourceMap.values().stream()
                .filter(other -> other != trait)
                .forEach(other -> other.setShaded(false));
        trait.setShaded(true);

        this.traitClicked.dispatch(args);
    }

    private void updateRemaining() {
        int selectionsRemaining = this.pool.getCapacity() - this.selectedMap.keySet().size();
        this.remainingLabel.setText(REMAINING_HEADER + selectionsRemaining);

        if (selectionsRemaining <= 0) {
            this.sourceMap.values().stream().forEach(trait -> trait.setEnabled(false));
        } else {
            this.sourceMap.values().stream().forEach(trait -> trait.setEnabled(true));
        }
    }


    // Nested Classes
    public static class TraitPoolSelectorStyle {

        // Fields
        public Drawable add;
        public Drawable remove;
        public int spacing;
        public LabelStyle labelStyle;
        public ScrollPaneStyle scrollPaneStyle;
        public Drawable background;
        public Drawable selectedBackground;
        public Drawable unselectedBackground;


        // Initialization
        public TraitPoolSelectorStyle() {

        }
    }

    interface Traitable<T extends Trait> {
        boolean isShaded();

        void setShaded(boolean value);

        T getTrait();
    }

    class SourceTrait<T extends Trait> extends Table implements Traitable<T> {

        // Fields
        private final Event<TraitSelectorEventArgs<T>> addClicked = new Event<>();
        private final Event<TraitSelectorEventArgs<T>> clicked = new Event<>();

        private final T trait;

        private Label nameLabel;
        private Button addButton;

        private boolean enabled = true;
        private boolean shaded = false;


        // Properties
        @Override
        public boolean isShaded() {
            return this.shaded;
        }

        @Override
        public void setShaded(boolean value) {
            boolean wasShaded = this.isShaded();
            this.shaded = value;
            if (this.shaded && !wasShaded) {
                this.background(TraitPoolSelector.this.selectedBackground);
            } else if (!this.shaded && wasShaded) {
                this.background(TraitPoolSelector.this.unselectedBackground);
            }
        }

        @Override
        public T getTrait() {
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
                        SourceTrait.this.addClicked.dispatch(new TraitSelectorEventArgs<>(SourceTrait.this.trait, SourceTrait.this));
                    }
                }
            });
            this.add(this.addButton).right().space(MainMenuScreen.COMPONENT_SPACING);

            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    SourceTrait.this.clicked.dispatch(new TraitSelectorEventArgs<>(SourceTrait.this.trait, SourceTrait.this));
                }
            });
        }
    }

    class SelectedTrait<T extends Trait> extends Table implements Traitable<T> {

        // Fields
        private final Event<TraitSelectorEventArgs<T>> removedClicked = new Event<>();
        private final Event<TraitSelectorEventArgs<T>> clicked = new Event<>();

        private final T trait;

        private Label nameLabel;
        private Button removeButton;

        private boolean enabled = true;
        private boolean shaded = false;


        // Properties
        @Override
        public boolean isShaded() {
            return this.shaded;
        }

        @Override
        public void setShaded(boolean value) {
            boolean wasShaded = this.isShaded();
            this.shaded = value;
            if (this.shaded && !wasShaded) {
                this.background(TraitPoolSelector.this.selectedBackground);
            } else if (!this.shaded && wasShaded) {
                this.background(TraitPoolSelector.this.unselectedBackground);
            }
        }

        @Override
        public T getTrait() {
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
                        SelectedTrait.this.removedClicked.dispatch(new TraitSelectorEventArgs<>(SelectedTrait.this.trait, SelectedTrait.this));
                    }
                }
            });
            this.add(this.removeButton).left().space(MainMenuScreen.COMPONENT_SPACING);

            this.nameLabel = new Label(this.trait.getName(), TraitPoolSelector.this.labelStyle);
            this.nameLabel.setAlignment(Align.right);
            this.add(this.nameLabel).right().space(MainMenuScreen.COMPONENT_SPACING).fillX().expandX();

            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    SelectedTrait.this.clicked.dispatch(new TraitSelectorEventArgs<>(SelectedTrait.this.trait, SelectedTrait.this));
                }
            });
        }
    }

    public static class TraitSelectorEventArgs<T extends Trait> {

        // Fields
        public final T trait;
        private final Traitable wrapper;


        // Initialization
        private TraitSelectorEventArgs(T trait, Traitable wrapper) {
            this.trait = trait;
            this.wrapper = wrapper;
        }
    }
}
