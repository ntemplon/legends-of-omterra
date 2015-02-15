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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 *
 * @author Nathan Templon
 */
public class NumberSelector extends Table {
    
    // Constants
    private final Color enabledColor =  new Color(Color.WHITE);
    private final Color disabledColor = new Color(0.6f, 0.6f, 0.6f, 1.0f);
    

    // Fields
    private final Label numberLabel;
    private final Drawable increase;
    private final Drawable decrease;
    private final int spacing;
    private final int minimumNumberSize;
    
    private Actor increaseActor;
    private Actor decreaseActor;

    private int changeAmount;
    private int value;


    // Properties
    public int getChangeAmount() {
        return this.changeAmount;
    }

    public void setChangeAmount(int change) {
        this.changeAmount = change;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(int value) {
        this.value = value;
        this.numberLabel.setText(value + "");
    }


    // Initialization
    public NumberSelector(NumberSelectorStyle style) {
        this.numberLabel = new Label("0", style.numberLabelStyle);
        this.increase = style.increase;
        this.decrease = style.decrease;
        this.spacing = style.spacing;
        this.minimumNumberSize = style.minimumNumberSize;
        
        this.initComponent();
    }
    
    
    // Private Methods
    private void initComponent() {
        this.increaseActor = new Image(this.increase);
        this.increaseActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT) {
                    NumberSelector.this.onIncreaseClicked();
                }
            }
        });
        
        this.decreaseActor = new Image(this.decrease);
        this.decreaseActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT) {
                    NumberSelector.this.onDecreaseClicked();
                }
            }
        });
        
        this.add(this.numberLabel).minWidth(this.minimumNumberSize).expandX().space(this.spacing);
        this.add(this.decreaseActor).space(this.spacing);
        this.add(this.increaseActor).space(this.spacing);
    }
    
    private void onIncreaseClicked() {
        
    }
    
    private void onDecreaseClicked() {
        
    }


    // Nested Classes
    public static class NumberSelectorStyle {

        // Fields
        public LabelStyle numberLabelStyle;
        public Drawable increase;
        public Drawable decrease;
        public int spacing;
        public int minimumNumberSize;


        // Initialization
        public NumberSelectorStyle() {

        }

    }

}
