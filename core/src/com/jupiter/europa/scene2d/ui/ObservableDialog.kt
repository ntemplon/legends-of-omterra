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

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener

/**

 * @author Nathan Templon
 */
public open class ObservableDialog : Dialog {

    // Enumerations
    public enum class DialogEvents {
        SHOWN,
        HIDDEN
    }


    // Properties
    private val shown = Event<DialogEventArgs>()
    private val hidden = Event<DialogEventArgs>()


    // Initialization
    public constructor(title: String, skin: Skin) : super(title, skin) {
    }

    public constructor(title: String, skin: Skin, windowStyleName: String) : super(title, skin, windowStyleName) {
    }

    public constructor(title: String, style: Window.WindowStyle) : super(title, style) {
    }


    // Public Methods
    override fun show(stage: Stage): Dialog {
        return super.show(stage) // the super implementation calls show(Stage, Action)
    }

    override fun show(stage: Stage, action: Action?): Dialog {
        val result = super.show(stage, action)
        this.shown.dispatch(DialogEventArgs(this, DialogEvents.SHOWN))
        return result
    }

    override fun hide() {
        super.hide() // the super implementation calls hide(Action)
    }

    override fun hide(action: Action?) {
        super.hide(action)
        this.hidden.dispatch(DialogEventArgs(this, DialogEvents.HIDDEN))
    }

    public fun addDialogListener(listener: Listener<DialogEventArgs>, vararg events: DialogEvents): Boolean {
        var added = false
        if (events.contains(DialogEvents.SHOWN)) {
            val addedThisTime = this.shown.addListener(listener)
            added = added || addedThisTime
        }
        if (events.contains(DialogEvents.HIDDEN)) {
            val addedThisTime = this.hidden.addListener(listener)
            added = added || addedThisTime
        }
        return added
    }

    public fun addDialogListener(listener: Listener<DialogEventArgs>): Boolean {
        return this.addDialogListener(listener, *DialogEvents.values())
    }

    public fun removeDialogListener(listener: Listener<DialogEventArgs>, vararg events: DialogEvents): Boolean {
        var removed = false
        if (events.contains(DialogEvents.SHOWN)) {
            val removedThisTime = this.shown.removeListener(listener)
            removed = removed || removedThisTime
        }
        if (events.contains(DialogEvents.HIDDEN)) {
            val removedThisTime = this.hidden.removeListener(listener)
            removed = removed || removedThisTime
        }
        return removed
    }

    public fun removeDialogListener(listener: Listener<DialogEventArgs>): Boolean {
        return this.removeDialogListener(listener, *DialogEvents.values())
    }


    // Nested Classes
    public data class DialogEventArgs(public val sender: ObservableDialog, public val event: DialogEvents)

}
