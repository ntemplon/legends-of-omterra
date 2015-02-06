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
package com.jupiter.europa.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jupiter.europa.util.ArrayUtils;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

/**
 *
 * @author Nathan Templon
 */
public class ObservableDialog extends Dialog {

    // Enumerations
    public enum DialogEvents {

        SHOWN,
        HIDDEN
    }


    // Fields
    private final Event<DialogEventArgs> shown = new Event<>();
    private final Event<DialogEventArgs> hidden = new Event<>();


    // Initialization
    public ObservableDialog(String title, Skin skin) {
        super(title, skin);
    }

    public ObservableDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ObservableDialog(String title, WindowStyle style) {
        super(title, style);
    }


    // Public Methods
    @Override
    public final Dialog show(Stage stage) {
        return super.show(stage); // the super implementation calls show(Stage, Action)
    }

    @Override
    public final Dialog show(Stage stage, Action action) {
        Dialog result = super.show(stage, action);
        this.shown.dispatch(new DialogEventArgs(this, DialogEvents.SHOWN));
        return result;
    }

    @Override
    public final void hide() {
        super.hide(); // the super implementation calls hide(Action)
    }

    @Override
    public final void hide(Action action) {
        super.hide(action);
        this.hidden.dispatch(new DialogEventArgs(this, DialogEvents.HIDDEN));
    }

    public final boolean addDialogListener(Listener<DialogEventArgs> listener, DialogEvents... events) {
        boolean added = false;
        if (ArrayUtils.contains(events, DialogEvents.SHOWN)) {
            boolean addedThisTime = this.shown.addListener(listener);
            added &= addedThisTime;
        }
        if (ArrayUtils.contains(events, DialogEvents.HIDDEN)) {
            boolean addedThisTime = this.hidden.addListener(listener);
            added &= addedThisTime;
        }
        return added;
    }

    public final boolean addDialogListener(Listener<DialogEventArgs> listener) {
        return this.addDialogListener(listener, DialogEvents.values());
    }

    public final boolean removeDialogListener(Listener<DialogEventArgs> listener, DialogEvents... events) {
        boolean removed = false;
        if (ArrayUtils.contains(events, DialogEvents.SHOWN)) {
            boolean removedThisTime = this.shown.removeListener(listener);
            removed &= removedThisTime;
        }
        if (ArrayUtils.contains(events, DialogEvents.HIDDEN)) {
            boolean removedThisTime = this.hidden.removeListener(listener);
            removed &= removedThisTime;
        }
        return removed;
    }

    public final boolean removeDialogListener(Listener<DialogEventArgs> listener) {
        return this.removeDialogListener(listener, DialogEvents.values());
    }


    // Nested Classes
    public static class DialogEventArgs {

        // Fields
        public final ObservableDialog sender;
        public final DialogEvents event;


        // Initialization
        public DialogEventArgs(ObservableDialog sender, DialogEvents event) {
            this.sender = sender;
            this.event = event;
        }

    }

}
