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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.jupiter.europa.entity.trait.Trait;
import com.jupiter.europa.entity.trait.TraitPool;
import com.jupiter.europa.scene2d.ui.ObservableDialog;
import com.jupiter.europa.scene2d.ui.TraitPoolSelector;
import com.jupiter.europa.scene2d.ui.TraitPoolSelector.TraitPoolSelectorStyle;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.europa.screen.MainMenuScreen.DialogExitStates;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public class SelectTraitDialog<T extends Trait> extends ObservableDialog {
    
    // Fields
    private final String title;
    private final TraitPool<T> pool;
    private final TraitPoolSelectorStyle selectorStyle;
    
    private Table mainTable;
    private TraitPoolSelector<T> selector;
    
    private Drawable dialogBackground;
    private DialogExitStates exitState;
    
    
    // Properties
    public final TraitPool<T> getPool() {
        return this.pool;
    }
    
    public final Drawable getDialogBackground() {
        return this.dialogBackground;
    }
    
    public final void setDialogBackground(Drawable drawable) {
        this.dialogBackground = drawable;
        this.mainTable.background(this.dialogBackground);
    }
    
    public DialogExitStates getExitState() {
        return this.exitState;
    }
    
    
    // Initialization
    public SelectTraitDialog(String title, Skin skin, TraitPool<T> pool) {
        this(title, skin.get(WindowStyle.class), skin.get(TraitPoolSelectorStyle.class), pool);
    }
    
    public SelectTraitDialog(String title, Skin skin, String styleName, TraitPool<T> pool) {
        this(title, skin.get(styleName, WindowStyle.class), skin.get(styleName, TraitPoolSelectorStyle.class), pool);
    }
    
    public SelectTraitDialog(String title, WindowStyle style, TraitPoolSelectorStyle selectorStyle, TraitPool<T> pool) {
        super("", style);
        this.title = title;
        this.pool = pool;
        this.selectorStyle = selectorStyle;
        this.initComponent();
    }
    
    
    // Private Methods
    private void initComponent() {
        this.mainTable = new Table();
        
        this.selector = new TraitPoolSelector<>(this.selectorStyle, this.pool);
        
        this.mainTable.add(this.selector).center().expandX().fillX();
        this.mainTable.row();
        
        this.getContentTable().add(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH);
    }
    
}
