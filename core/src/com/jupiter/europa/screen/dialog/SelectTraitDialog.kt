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

package com.jupiter.europa.screen.dialog

import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.jupiter.europa.entity.effects.Effect
import com.jupiter.europa.entity.traits.EffectPool
import com.jupiter.europa.scene2d.ui.EuropaButton
import com.jupiter.europa.scene2d.ui.ObservableDialog
import com.jupiter.europa.scene2d.ui.TraitPoolSelector
import com.jupiter.europa.scene2d.ui.TraitPoolSelector.TraitPoolSelectorStyle
import com.jupiter.europa.screen.MainMenuScreen
import com.jupiter.europa.screen.MainMenuScreen.DialogExitStates
import com.jupiter.ganymede.event.Listener

/**
 * @param
 * *
 * @author Nathan Templon
 */
public class SelectTraitDialog<T : Effect>(private val title: String, private val skinInternal: Skin, public val pool: EffectPool<T>) : ObservableDialog("", skinInternal.get(javaClass<Window.WindowStyle>())) {
    private val selectorStyle: TraitPoolSelectorStyle

    private var mainTable: Table? = null
    private var titleLabelInternal: Label? = null
    private var selector: TraitPoolSelector<T>? = null
    private var featNameLabel: Label? = null
    private var featDescLabel: Label? = null
    private var featDescPane: ScrollPane? = null
    private var buttonTableInternal: Table? = null
    private var nextButton: EuropaButton? = null
    private var backButton: EuropaButton? = null

    public var dialogBackgroundInternal: Drawable? = null
        private set
    public var exitState: DialogExitStates? = null
        private set

    public fun setDialogBackground(drawable: Drawable) {
        this.dialogBackgroundInternal = drawable
        this.mainTable!!.background(this.dialogBackgroundInternal)
    }


    init {
        this.selectorStyle = skinInternal.get(javaClass<TraitPoolSelectorStyle>())

        this.initComponent()
    }

    // Public Methods
    public fun applyChanges() {
        this.selector!!.applyChanges()
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
    }


    // Private Methods
    private fun initComponent() {
        this.mainTable = Table()

        // Title
        this.titleLabelInternal = Label(this.title, this.skinInternal.get(javaClass<Label.LabelStyle>()))

        this.mainTable!!.add<Label>(this.titleLabelInternal).expandX().center().top()
        this.mainTable!!.row()

        // Selector
        this.selector = TraitPoolSelector(this.selectorStyle, this.pool)
        this.selector!!.addTraitClickListener(Listener { args ->
            this.featNameLabel?.setText(args.`trait`.name)
            this.featDescLabel?.setText(args.`trait`.description)
        })

        this.mainTable!!.add<TraitPoolSelector<T>>(this.selector).center().top().expand().fill().space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.mainTable!!.row()

        // Feat Descriptions
        this.featNameLabel = Label("", this.skinInternal.get(javaClass<Label.LabelStyle>()))
        this.featNameLabel!!.setAlignment(Align.top)
        this.featDescLabel = Label("", this.skinInternal.get(MainMenuScreen.INFO_STYLE_KEY, javaClass<Label.LabelStyle>()))
        this.featDescLabel!!.setAlignment(Align.topLeft)
        this.featDescPane = ScrollPane(this.featDescLabel, this.skinInternal.get(javaClass<ScrollPane.ScrollPaneStyle>()))

        this.mainTable!!.add<Label>(this.featNameLabel).center().expandX().fillX().top().space(MainMenuScreen.COMPONENT_SPACING.toFloat())
        this.mainTable!!.row()
        this.mainTable!!.add<ScrollPane>(this.featDescPane).center().expandX().fillX().top().space(MainMenuScreen.COMPONENT_SPACING.toFloat()).height(300f)
        this.mainTable!!.row()

        // Buttons
        this.buttonTableInternal = Table()
        this.nextButton = EuropaButton("Next", this.skinInternal.get(javaClass<TextButton.TextButtonStyle>()))
        this.nextButton!!.addClickListener(Listener { args -> this.onNextButtonClick(args) })
        this.backButton = EuropaButton("Back", this.skinInternal.get(javaClass<TextButton.TextButtonStyle>()))
        this.backButton!!.addClickListener(Listener { args -> this.onBackButtonClick(args) })

        this.buttonTableInternal!!.add<EuropaButton>(this.backButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right().expandX()
        this.buttonTableInternal!!.add<EuropaButton>(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()

        this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())
        this.mainTable!!.add<Table>(this.buttonTableInternal).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).expandX().fillX()

        this.getContentTable().add<Table>(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH.toFloat())
    }

    private fun onNextButtonClick(event: EuropaButton.ClickEvent) {
        this.exitState = DialogExitStates.NEXT
        this.hide()
    }

    private fun onBackButtonClick(even: EuropaButton.ClickEvent) {
        this.exitState = DialogExitStates.BACK
        this.hide()
    }
}
