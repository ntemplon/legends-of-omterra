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

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.jupiter.europa.scene2d.ui.NumberSelector.NumberSelectorStyle
import com.jupiter.europa.scene2d.ui.NumberSelector.ValueChangedEventArgs
import com.jupiter.ganymede.event.Listener
import java.util.ArrayList
import java.util.LinkedHashMap

/**

 * @author Nathan Templon
 */
public open class MultipleNumberSelector @jvmOverloads constructor(public val maxPoints: Int, style: MultipleNumberSelector.MultipleNumberSelectorStyle,
                                                                   private val values: List<String>, private val numColumns: Int = 1) : Table() {

    // Static Methods
    private fun toConventionalCase(word: String): String {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()
    }

    private val labelStyle: LabelStyle
    private val numberStyle: NumberSelectorStyle
    private val spacing: Int

    private val selectors = LinkedHashMap<String, NumberSelector>()

    private var totalLabel: Label? = null
    private var totalValueLabel: Label? = null
    private var useMaximumNumber: Boolean = false
    private var useMinimumNumber: Boolean = false
    public var maximumNumber: Int = 0
        set(value) {
            this.$maximumNumber = value
            this.onValueChanged(null)
        }
    public var minimumNumber: Int = 0
        set(value) {
            this.$minimumNumber = value
            this.onValueChanged(null)
        }

    public fun getTotalSelected(): Int = this.selectors.values().map { value ->
        value.value
    }.sum()

    public fun getValues(): Map<String, Int> = this.selectors.map { entry ->
        Pair(entry.getKey(), entry.getValue().value)
    }.toMap()

    public fun setValues(values: Map<String, Int>) {
        for ((name, selector) in this.selectors) {
            selector.value = values[name] ?: 0
        }
    }

    public fun isUseMaximumNumber(): Boolean {
        return useMaximumNumber
    }

    public fun setUseMaximumNumber(useMaximumNumber: Boolean) {
        this.useMaximumNumber = useMaximumNumber
        this.onValueChanged(null)
    }

    public fun isUseMinimumNumber(): Boolean {
        return useMinimumNumber
    }

    public fun setUseMinimumNumber(useMinimumNumber: Boolean) {
        this.useMinimumNumber = useMinimumNumber
        this.onValueChanged(null)
    }

    public fun setIncrement(increment: Int) {
        for (selector in this.selectors.values()) {
            selector.changeAmount = increment
        }
    }


    init {
        this.labelStyle = style.labelStyle!!
        this.numberStyle = style.numberSelectorStyle!!
        this.spacing = style.spacing

        this.maximumNumber = this.maxPoints
        this.minimumNumber = 0
        this.setUseMaximumNumber(true)
        this.setUseMinimumNumber(true)

        this.initComponents()

        this.onValueChanged(null)
    }


    // Private Methods
    private fun initComponents() {
        this.totalLabel = Label("Points Remaining:", this.labelStyle)
        this.totalValueLabel = Label(this.maxPoints.toString(), this.labelStyle)

        val totalTable = Table()
        totalTable.add<Label>(this.totalLabel).left().space(this.spacing.toFloat())
        totalTable.add<Label>(totalValueLabel).left().expandX().space(this.spacing.toFloat())
        this.add(totalTable).colspan(2 * this.numColumns)
        this.row()

        val sorted = ArrayList<String>(this.values.size())
        val index = this.values.size() / 2
        for (i in 0..index) {
            sorted.add(this.values.get(i))
            if (i + index + 1 < this.values.size()) {
                sorted.add(this.values.get(i + index + 1))
            }
        }

        var addedToRow = 0
        for (value in sorted) {
            val label = Label(value, this.labelStyle)
            val selector = NumberSelector(numberStyle)
            this.selectors.put(value, selector)

            if (addedToRow >= 1) {
                this.add(label).left().space((spacing * 2).toFloat()).padTop(10f).padLeft(30f)
            } else {
                this.add(label).left().space((spacing * 2).toFloat()).padTop(10f)
            }

            if (addedToRow == this.numColumns - 1) {
                this.add(selector).left().expandX().fillX().space(this.spacing.toFloat())
                this.row()
                addedToRow = 0
            } else {
                this.add(selector).left().space(this.spacing.toFloat())
                addedToRow++
            }

            selector.addValueChangedListener(Listener { args -> this.onValueChanged(args) })
        }
    }

    private fun onValueChanged(args: ValueChangedEventArgs?) {
        val total = this.getTotalSelected()
        if (total >= this.maxPoints) {
            if (args != null) {
                args.sender.decreaseEnabled = !(this.isUseMinimumNumber() && args.sender.value <= this.minimumNumber)
            }
            this.selectors.values().forEach { selector ->
                selector.increaseEnabled = false
            }
        } else {
            this.selectors.values().forEach { selector ->
                selector.increaseEnabled = !(this.isUseMaximumNumber() && selector.value >= this.maximumNumber)
                selector.decreaseEnabled = !(this.isUseMinimumNumber() && selector.value <= this.minimumNumber)
            }
        }
        if (this.totalValueLabel != null) {
            this.totalValueLabel!!.setText((this.maxPoints - total).toString())
        }
        this.invalidate()
    }


    // NestedClasses
    public data class MultipleNumberSelectorStyle {

        // Properties
        public var labelStyle: LabelStyle? = null
        public var numberSelectorStyle: NumberSelectorStyle? = null
        public var spacing: Int = 0

    }

}
