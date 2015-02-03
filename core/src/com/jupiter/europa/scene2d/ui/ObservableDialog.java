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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public class ObservableDialog extends Dialog {
    
    // Fields
    private final Set<DialogListener> listeners = new LinkedHashSet<>();
    
    
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
    public boolean addDialogListener(DialogListener listener) {
        return this.listeners.add(listener);
    }
    
    public boolean removeDialogListener(DialogListener listener) {
        return this.listeners.remove(listener);
    }
    
    @Override
    public void hide() {
        super.hide();
        this.listeners.stream().forEach((DialogListener listener) -> listener.dialogHidden());
    }
    
    @Override
    public void hide(Action action) {
        super.hide(action);
        this.listeners.stream().forEach((DialogListener listener) -> listener.dialogHidden());
    }
    
}
