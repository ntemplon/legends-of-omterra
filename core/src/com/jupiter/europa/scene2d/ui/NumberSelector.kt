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

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.*
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener

/**

 * @author Nathan Templon
 */
public class NumberSelector(style: NumberSelector.NumberSelectorStyle) : Table() {

    // Constants
    private val DISABLED_COLOR = Color(0.6f, 0.6f, 0.6f, 1.0f)
    private val DEFAULT_CHANGE_AMOUNT = 5


    // Fields
    private val valueChanged = Event<ValueChangedEventArgs>()
    private val numberLabel: Label
    private val increase: ImageButton
    private val decrease: ImageButton
    private val spacing: Int
    private val minimumNumberSize: Int

    private val increaseBackground: Drawable
    private val decreaseBackground: Drawable
    private val increaseBackgroundDisabled: Drawable
    private val decreaseBackgroundDisabled: Drawable

    public var changeAmount: Int = 0
    public var value: Int = 0
        get() = this.$value
        set(newValue) {
            if (newValue != this.$value) {
                val oldValue = this.$value
                this.$value = newValue
                this.numberLabel.setText(newValue.toString())
                this.invalidate()
                this.valueChanged.dispatch(ValueChangedEventArgs(this, oldValue, newValue))
            }
        }
    public var decreaseEnabled: Boolean = false
        set(enabled) {
            this.$decreaseEnabled = enabled
            if (enabled) {
                this.decrease.setBackground(this.decreaseBackgroundDisabled)
            } else {
                this.decrease.setBackground(this.decreaseBackground)
            }
        }
    public var increaseEnabled: Boolean = false
        set(enabled) {
            this.$increaseEnabled = enabled
            if (enabled) {
                this.increase.setBackground(this.increaseBackgroundDisabled)
            } else {
                this.increase.setBackground(this.increaseBackground)
            }
        }


    init {
        this.numberLabel = Label("0", style.numberLabelStyle)
        this.increase = ImageButton(style.increase)
        this.decrease = ImageButton(style.decrease)
        this.spacing = style.spacing
        this.minimumNumberSize = style.minimumNumberSize

        this.increaseBackground = style.increase!!
        this.decreaseBackground = style.decrease!!
        if (style.increase is TextureRegionDrawable) {
            this.increaseBackgroundDisabled = (style.increase as TextureRegionDrawable).tint(DISABLED_COLOR)
        } else if (style.increase is NinePatchDrawable) {
            this.increaseBackgroundDisabled = (style.increase as NinePatchDrawable).tint(DISABLED_COLOR)
        } else if (style.increase is SpriteDrawable) {
            this.increaseBackgroundDisabled = (style.increase as SpriteDrawable).tint(DISABLED_COLOR)
        } else {
            this.increaseBackgroundDisabled = this.increaseBackground
        }
        if (style.decrease is TextureRegionDrawable) {
            this.decreaseBackgroundDisabled = (style.increase as TextureRegionDrawable).tint(DISABLED_COLOR)
        } else if (style.decrease is NinePatchDrawable) {
            this.decreaseBackgroundDisabled = (style.increase as NinePatchDrawable).tint(DISABLED_COLOR)
        } else if (style.decrease is SpriteDrawable) {
            this.decreaseBackgroundDisabled = (style.increase as SpriteDrawable).tint(DISABLED_COLOR)
        } else {
            this.decreaseBackgroundDisabled = this.decreaseBackground
        }

        this.increaseEnabled = true
        this.decreaseEnabled = true
        this.changeAmount = DEFAULT_CHANGE_AMOUNT

        this.initComponent()
    }


    // Public Methods
    public fun addValueChangedListener(listener: Listener<ValueChangedEventArgs>): Boolean {
        return this.valueChanged.addListener(listener)
    }

    public fun removeValueChangedListener(listener: Listener<ValueChangedEventArgs>): Boolean {
        return this.valueChanged.removeListener(listener)
    }


    // Private Methods
    private fun initComponent() {
        this.increase.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event!!.getButton() == Input.Buttons.LEFT && this@NumberSelector.increaseEnabled) {
                    this@NumberSelector.onIncreaseClicked()
                }
            }
        })
        this.decrease.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event!!.getButton() == Input.Buttons.LEFT && this@NumberSelector.decreaseEnabled) {
                    this@NumberSelector.onDecreaseClicked()
                }
            }
        })

        this.add(this.numberLabel).minWidth(this.minimumNumberSize.toFloat()).expandX().space(this.spacing.toFloat()).padBottom(6f)
        this.add(this.decrease).space(this.spacing.toFloat())
        this.add(this.increase).space(this.spacing.toFloat())
    }

    private fun onIncreaseClicked() {
        this.value = this.value + this.changeAmount
    }

    private fun onDecreaseClicked() {
        this.value = this.value - this.changeAmount
    }


    // Nested Classes
    public class NumberSelectorStyle {

        // Fields
        public var numberLabelStyle: LabelStyle? = null
        public var increase: Drawable? = null
        public var decrease: Drawable? = null
        public var spacing: Int = 0
        public var minimumNumberSize: Int = 0

    }

    public data class ValueChangedEventArgs(public val sender: NumberSelector, public val oldValue: Int, public val newValue: Int)

}
