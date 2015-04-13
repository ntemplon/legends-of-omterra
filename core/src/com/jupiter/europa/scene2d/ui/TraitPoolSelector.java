/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.jupiter.europa.entity.trait.Trait;
import com.jupiter.europa.entity.trait.TraitPool;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public class TraitPoolSelector<T extends Trait> extends Table {
    
    // Fields
    private final Drawable add;
    private final Drawable remove;
    private final int spacing;
    private final LabelStyle labelStyle;
    private final ScrollPaneStyle scrollPaneStyle;
    private final TraitPool<T> pool;
    
    
    // Initialization
    public TraitPoolSelector(TraitPoolSelectorStyle style, TraitPool<T> pool) {
        this.add = style.add;
        this.remove = style.remove;
        this.spacing = style.spacing;
        this.labelStyle = style.labelStyle;
        this.scrollPaneStyle = style.scrollPaneStyle;
        this.pool = pool;
        
        this.initComponent();
    }
    
    public TraitPoolSelector(Skin skin, TraitPool<T> pool) {
        this(skin.get(TraitPoolSelectorStyle.class), pool);
    }
    
    public TraitPoolSelector(Skin skin, String styleName, TraitPool<T> pool) {
        this(skin.get(styleName, TraitPoolSelectorStyle.class), pool);
    }
    
    
    // Public Methods
    /**
     * Applies the changes selected by the user to the provided trait pool.
     */
    public void applyChanges() {
        
    }
    
    
    // Private Methods
    private void initComponent() {
        
    }
    
    
    // Nested Classes
    public static class TraitPoolSelectorStyle {
        
        // Fields
        public Drawable add;
        public Drawable remove;
        public int spacing;
        public LabelStyle labelStyle;
        public ScrollPaneStyle scrollPaneStyle;
        
        
        // Initialization
        public TraitPoolSelectorStyle() {
            
        }
        
    }
    
}
