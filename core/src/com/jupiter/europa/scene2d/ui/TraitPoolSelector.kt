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
 *
 */

package com.jupiter.europa.scene2d.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.jupiter.europa.entity.traits.Trait
import com.jupiter.europa.entity.traits.TraitPool
import com.jupiter.europa.screen.MainMenuScreen
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener
import java.util.Collections
import java.util.Comparator
import java.util.TreeMap

/**
 * @param
 * *
 * @author Nathan Templon
 */
public class TraitPoolSelector<T : Trait>(style: TraitPoolSelector.TraitPoolSelectorStyle, private val pool: TraitPool<T>) : Table() {
    // TODO: Recompute qualifications on feat addition, and remove any unqualified feats if necessary on feat removal.

    // Properties
    private val traitClicked = Event<TraitSelectorEventArgs<T>>()
    private val sourceMap = TreeMap<T, SourceTrait<T>>()
    private val selectedMap = TreeMap<T, SelectedTrait<T>>()

    private val add: Drawable?
    private val remove: Drawable?
    private val spacing: Int
    private val labelStyle: LabelStyle?
    private val scrollPaneStyle: ScrollPaneStyle?
    private val backImage: Drawable?
    private val selectedBackground: Drawable?
    private val unselectedBackground: Drawable?

    private var sourcePane: ScrollPane? = null
    private var selectedPane: ScrollPane? = null
    private var sourceTable: VerticalGroup? = null
    private var selectedTable: VerticalGroup? = null
    private var sourceLabel: Label? = null
    private var selectedLabel: Label? = null
    private var remainingLabel: Label? = null

    public var sourceTitle: String = "Available"
    public var selectedTitle: String = "Selected"


    init {
        this.add = style.add
        this.remove = style.remove
        this.spacing = style.spacing
        this.labelStyle = style.labelStyle
        this.scrollPaneStyle = style.scrollPaneStyle
        this.backImage = style.background
        this.selectedBackground = style.selectedBackground
        this.unselectedBackground = style.unselectedBackground

        this.initComponent()
    }

    public constructor(skin: Skin, pool: TraitPool<T>) : this(skin.get(javaClass<TraitPoolSelectorStyle>()), pool) {
    }

    public constructor(skin: Skin, styleName: String, pool: TraitPool<T>) : this(skin.get(styleName, javaClass<TraitPoolSelectorStyle>()), pool) {
    }

    // Public Methods

    /**
     * Applies the changes selected by the user to the provided trait pool.
     */
    public fun applyChanges() {
        this.selectedTable?.getChildren()
                ?.map { actor -> actor as? SelectedTrait<T> }
                ?.filterNotNull()
                ?.filter { it.isEnabled() }
                ?.forEach { wrapper -> this.pool.select(wrapper.getTrait()) }
    }

    public fun addTraitClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
        return this.traitClicked.addListener(listener)
    }

    public fun removeTraitClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
        return this.traitClicked.removeListener(listener)
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        this.getCell<VerticalGroup>(this.sourceTable).width(width / 2.0f)
        this.getCell<VerticalGroup>(this.selectedTable).width(width / 2.0f)
    }


    // Private Methods
    private fun initComponent() {
        this.background(this.backImage)

        // Remaining Label
        this.remainingLabel = Label(REMAINING_HEADER + (this.pool.capacity - this.pool.numberOfSelections), this.labelStyle)
        this.add<Label>(this.remainingLabel).center().colspan(2)
        this.row()

        // Configure and add Labels
        this.sourceLabel = Label(this.sourceTitle, this.labelStyle)
        this.selectedLabel = Label(this.selectedTitle, this.labelStyle)

        this.add<Label>(this.sourceLabel).center().space(this.spacing.toFloat())
        this.add<Label>(this.selectedLabel).center().space(this.spacing.toFloat())
        this.row()

        // Configure and add Tables
        this.sourceTable = VerticalGroup()
        this.sourceTable?.space(this.spacing.toFloat())
        this.sourceTable?.right()
        this.sourceTable?.fill()
        this.sourceMap.putAll(this.pool.sources.map { source ->
            Pair(source, SourceTrait(source))
        }.toMap())
        this.sourceMap.values().forEach { entry ->
            entry.addAdditionClickListener(Listener { args -> this.onAddTraitClick(args) })
            entry.addClickListener(Listener { args -> this.onTraitInfoClick(args) })
            entry.setEnabled(this.pool.qualified.contains(entry.getTrait()))
            this.sourceTable?.addActor(entry)
        }
        this.sourcePane = ScrollPane(this.sourceTable, this.scrollPaneStyle)

        this.selectedTable = VerticalGroup()
        this.selectedTable?.space(this.spacing.toFloat())
        this.selectedTable?.left()
        this.selectedTable?.fill()
        this.selectedMap.putAll(this.pool.selections.map { item ->
            Pair(item, SelectedTrait(item))
        }.toMap())
        this.selectedMap.values().forEach { entry ->
            entry.addRemovalClickListener(Listener { args -> this.onRemoveTraitClick(args) })
            entry.addClickListener(Listener { args -> this.onTraitInfoClick(args) })
            entry.setEnabled(false)
            this.selectedTable?.addActor(entry)
        }
        this.selectedPane = ScrollPane(this.selectedTable, this.scrollPaneStyle)

        this.add<ScrollPane>(this.sourcePane).center().expand().fill().space(this.spacing.toFloat()).padRight((3 * this.spacing).toFloat()).width(this.getWidth() / 2.0f)
        this.add<ScrollPane>(this.selectedPane).center().expand().fill().space(this.spacing.toFloat()).padLeft((3 * this.spacing).toFloat()).width(this.getWidth() / 2.0f)
    }

    private fun onAddTraitClick(args: TraitSelectorEventArgs<T>) {
        val source = this.sourceMap.get(args.`trait`)
        this.sourceMap.remove(args.`trait`)

        // Remove it from the table
        this.sourceTable!!.removeActor(source)

        // Add it to the other table
        val selected = SelectedTrait(args.`trait`)
        selected.addRemovalClickListener(Listener { args -> this.onRemoveTraitClick(args) })
        selected.addClickListener(Listener { args -> this.onTraitInfoClick(args) })
        this.selectedMap.put(args.`trait`, selected)

        // Move it to the correct position
        this.selectedTable!!.addActor(selected)

        this.updateRemaining()
    }

    private fun onRemoveTraitClick(args: TraitSelectorEventArgs<T>) {
        val selected = this.selectedMap.get(args.`trait`)
        this.selectedMap.remove(args.`trait`)

        // Remove it from the table
        this.selectedTable!!.removeActor(selected)

        // Add it to the source table
        val source = SourceTrait(args.`trait`)
        source.addAdditionClickListener(Listener { args -> this.onAddTraitClick(args) })
        source.addClickListener(Listener { args -> this.onTraitInfoClick(args) })
        this.sourceMap.put(args.`trait`, source)

        // Move it to the correct position
        val index = Collections.binarySearch<Actor>(this.sourceTable!!.getChildren().toArrayList(), source, Comparator { first, second ->
            if (first is Traitable<*> && second is Traitable<*>) {
                first.getTrait().name.compareTo(second.getTrait().name)
            } else {
                0
            }
        })
        this.sourceTable?.addActorAt(Math.abs(index) - 1, source)

        this.updateRemaining()
    }

    private fun onTraitInfoClick(args: TraitSelectorEventArgs<T>) {
        val wrapper = args.wrapper
        this.selectedMap.values()
                .filter { value -> value != wrapper }
                .forEach { other -> other.setShaded(false) }
        this.sourceMap.values()
                .filter { value -> value != wrapper }
                .forEach { other -> other.setShaded(false) }
        wrapper.setShaded(true)

        this.traitClicked.dispatch(args)
    }

    private fun updateRemaining() {
        val selectionsRemaining = this.pool.capacity - this.selectedMap.keySet().size()
        this.remainingLabel!!.setText(REMAINING_HEADER + selectionsRemaining)

        if (selectionsRemaining <= 0) {
            this.sourceMap.values().forEach { it.setEnabled(false) }
        } else {
            this.sourceMap.values().forEach { it.setEnabled(this.pool.qualified.contains(it.getTrait())) }
        }
    }


    // Nested Classes
    public class TraitPoolSelectorStyle {

        // Properties
        public var add: Drawable? = null
        public var remove: Drawable? = null
        public var spacing: Int = 0
        public var labelStyle: LabelStyle? = null
        public var scrollPaneStyle: ScrollPaneStyle? = null
        public var background: Drawable? = null
        public var selectedBackground: Drawable? = null
        public var unselectedBackground: Drawable? = null
    }

    interface Traitable<T : Trait> {
        public fun isShaded(): Boolean

        public fun setShaded(value: Boolean)

        public fun getTrait(): T
    }

    inner class SourceTrait<T : Trait>(private val `trait`: T) : Table(), Traitable<T> {

        // Fields
        private val addClicked = Event<TraitSelectorEventArgs<T>>()
        private val clicked = Event<TraitSelectorEventArgs<T>>()

        private var nameLabel: Label? = null
        private var addButton: Button? = null

        private var enabled = true
        private var shaded = false


        // Properties
        override fun isShaded(): Boolean {
            return this.shaded
        }

        override fun setShaded(value: Boolean) {
            val wasShaded = this.isShaded()
            this.shaded = value
            if (this.shaded && !wasShaded) {
                this.background(this@TraitPoolSelector.selectedBackground)
            } else if (!this.shaded && wasShaded) {
                this.background(this@TraitPoolSelector.unselectedBackground)
            }
        }

        override fun getTrait(): T {
            return this.`trait`
        }

        fun isEnabled(): Boolean {
            return this.enabled
        }

        fun setEnabled(enabled: Boolean) {
            this.enabled = enabled
            this.addButton!!.setVisible(this.enabled)
        }


        init {
            this.initComponent()
        }


        // Package Protected Methods
        fun addAdditionClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.addClicked.addListener(listener)
        }

        fun removeAdditionClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.addClicked.removeListener(listener)
        }

        fun addClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.clicked.addListener(listener)
        }

        fun removeClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.clicked.removeListener(listener)
        }


        // Private Methods
        private fun initComponent() {
            this.nameLabel = Label(this.`trait`.name, this@TraitPoolSelector.labelStyle)
            this.add<Label>(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING.toFloat()).fillX().expandX()

            this.addButton = Button(this@TraitPoolSelector.add)
            this.addButton!!.addListener(object : ClickListener() {
                override fun clicked(e: InputEvent?, x: Float, y: Float) {
                    if (this@SourceTrait.isEnabled()) {
                        this@SourceTrait.addClicked.dispatch(TraitSelectorEventArgs(this@SourceTrait.`trait`, this@SourceTrait))
                    }
                }
            })
            this.add<Button>(this.addButton).right().space(MainMenuScreen.COMPONENT_SPACING.toFloat())

            this.addListener(object : ClickListener() {
                override fun clicked(e: InputEvent?, x: Float, y: Float) {
                    this@SourceTrait.clicked.dispatch(TraitSelectorEventArgs(this@SourceTrait.`trait`, this@SourceTrait))
                }
            })
        }
    }

    inner class SelectedTrait<T : Trait>(private val `trait`: T) : Table(), Traitable<T> {

        // Fields
        private val removedClicked = Event<TraitSelectorEventArgs<T>>()
        private val clicked = Event<TraitSelectorEventArgs<T>>()

        private var nameLabel: Label? = null
        private var removeButton: Button? = null

        private var enabled = true
        private var shaded = false


        // Properties
        override fun isShaded(): Boolean {
            return this.shaded
        }

        override fun setShaded(value: Boolean) {
            val wasShaded = this.isShaded()
            this.shaded = value
            if (this.shaded && !wasShaded) {
                this.background(this@TraitPoolSelector.selectedBackground)
            } else if (!this.shaded && wasShaded) {
                this.background(this@TraitPoolSelector.unselectedBackground)
            }
        }

        override fun getTrait(): T {
            return this.`trait`
        }

        fun isEnabled(): Boolean {
            return this.enabled
        }

        fun setEnabled(enabled: Boolean) {
            this.enabled = enabled
            this.removeButton!!.setVisible(this.enabled)
        }


        init {
            this.initComponent()
        }


        // Package Protected Methods
        fun addRemovalClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.removedClicked.addListener(listener)
        }

        fun removeRemovalClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.removedClicked.removeListener(listener)
        }

        fun addClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.clicked.addListener(listener)
        }

        fun removeClickListener(listener: Listener<TraitSelectorEventArgs<T>>): Boolean {
            return this.clicked.removeListener(listener)
        }


        // Private Methods
        private fun initComponent() {
            this.removeButton = Button(this@TraitPoolSelector.remove)
            this.removeButton!!.addListener(object : ClickListener() {
                override fun clicked(e: InputEvent?, x: Float, y: Float) {
                    if (this@SelectedTrait.isEnabled()) {
                        this@SelectedTrait.removedClicked.dispatch(TraitSelectorEventArgs(this@SelectedTrait.`trait`, this@SelectedTrait))
                    }
                }
            })
            this.add<Button>(this.removeButton).left().space(MainMenuScreen.COMPONENT_SPACING.toFloat())

            this.nameLabel = Label(this.`trait`.name, this@TraitPoolSelector.labelStyle)
            this.nameLabel!!.setAlignment(Align.right)
            this.add<Label>(this.nameLabel).right().space(MainMenuScreen.COMPONENT_SPACING.toFloat()).fillX().expandX()

            this.addListener(object : ClickListener() {
                override fun clicked(e: InputEvent?, x: Float, y: Float) {
                    this@SelectedTrait.clicked.dispatch(TraitSelectorEventArgs(this@SelectedTrait.`trait`, this@SelectedTrait))
                }
            })
        }
    }

    public data class TraitSelectorEventArgs<T : Trait>(public val `trait`: T, public val wrapper: Traitable<out Trait>)

    companion object {
        // Constants
        private val REMAINING_HEADER = "Selections Remaining: "
    }
}
