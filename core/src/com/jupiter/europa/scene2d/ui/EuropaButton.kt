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
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener

/**

 * @author Nathan Templon
 */
public class EuropaButton : TextButton {

    // Fields
    private val clicked = Event<ClickEvent>()


    // Initialization
    public constructor(text: String, style: TextButton.TextButtonStyle) : super(text, style) {
        this.init()
    }

    public constructor(text: String, skin: Skin) : super(text, skin) {
        this.init()
    }


    // Public Methods
    public fun addClickListener(listener: Listener<ClickEvent>): Boolean {
        return this.clicked.addListener(listener)
    }

    public fun addClickListener(listener: (ClickEvent) -> Unit): Boolean = this.clicked.addListener(listener)

    public fun removeClickListener(listener: Listener<ClickEvent>): Boolean {
        return this.clicked.removeListener(listener)
    }

    public fun removeClickListener(listener: (ClickEvent) -> Unit): Boolean = this.clicked.removeListener(listener)


    // Private Methods
    private fun init() {
        this.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event!!.getButton() == Input.Buttons.LEFT && !this@EuropaButton.isDisabled()) {
                    this@EuropaButton.clicked.dispatch(ClickEvent(event, x, y))
                    this@EuropaButton.setChecked(false)
                }
            }
        })
    }


    // Nested Classes
    public inner data class ClickEvent(public val event: InputEvent, public val x: Float, public val y: Float)

}
