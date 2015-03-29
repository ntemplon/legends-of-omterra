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
package com.jupiter.europa.screen.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jupiter.europa.entity.trait.Trait;
import com.jupiter.europa.entity.trait.TraitPool;
import com.jupiter.europa.scene2d.ui.ObservableDialog;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public class SelectTraitDialog<T extends Trait> extends ObservableDialog {
    
    // Fields
    private final TraitPool<T> pool;
    
    
    // Properties
    public final TraitPool<T> getPool() {
        return this.pool;
    }
    
    
    // Initialization
    public SelectTraitDialog(String title, Skin skin, TraitPool<T> pool) {
        super(title, skin);
        this.pool = pool;
    }
    
    public SelectTraitDialog(String title, Skin skin, String styleName, TraitPool<T> pool) {
        super(title, skin, styleName);
        this.pool = pool;
    }
    
    public SelectTraitDialog(String title, WindowStyle style, TraitPool<T> pool) {
        super(title, style);
        this.pool = pool;
    }
    
}
