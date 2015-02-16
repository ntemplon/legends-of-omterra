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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

/**
 *
 * @author Nathan Templon
 */
public class NumberSelector extends Table {

    // Constants
    private final Color DISABLED_COLOR = new Color(0.6f, 0.6f, 0.6f, 1.0f);
    private final int DEFAULT_CHANGE_AMOUNT = 5;


    // Fields
    private final Event<ValueChangedEventArgs> valueChanged = new Event<>();
    private final Label numberLabel;
    private final ImageButton increase;
    private final ImageButton decrease;
    private final int spacing;
    private final int minimumNumberSize;

    private final Drawable increaseBackground;
    private final Drawable decreaseBackground;
    private final Drawable increaseBackgroundDisabled;
    private final Drawable decreaseBackgroundDisabled;

    private int changeAmount;
    private int value;
    private boolean decreaseEnabled;
    private boolean increaseEnabled;


    // Properties
    public final int getChangeAmount() {
        return this.changeAmount;
    }

    public final void setChangeAmount(int change) {
        this.changeAmount = change;
    }

    public final int getValue() {
        return this.value;
    }

    public final void setValue(int value) {
        if (value != this.value) {
            int oldValue = this.value;
            this.value = value;
            this.numberLabel.setText(this.value + "");
            this.invalidate();
            this.valueChanged.dispatch(new ValueChangedEventArgs(this, oldValue, this.value));
        }
    }

    public final boolean getDecreaseEnabled() {
        return this.decreaseEnabled;
    }

    public final void setDecreaseEnabled(boolean enabled) {
        this.decreaseEnabled = enabled;
        if (enabled) {
            this.decrease.setBackground(this.decreaseBackgroundDisabled);
        }
        else {
            this.decrease.setBackground(this.decreaseBackground);
        }
    }

    public final boolean getIncreaseEnabled() {
        return this.increaseEnabled;
    }

    public final void setIncreaseEnabled(boolean enabled) {
        this.increaseEnabled = enabled;
        if (enabled) {
            this.increase.setBackground(this.increaseBackgroundDisabled);
        }
        else {
            this.increase.setBackground(this.increaseBackground);
        }
    }


    // Initialization
    public NumberSelector(NumberSelectorStyle style) {
        this.numberLabel = new Label("0", style.numberLabelStyle);
        this.increase = new ImageButton(style.increase);
        this.decrease = new ImageButton(style.decrease);
        this.spacing = style.spacing;
        this.minimumNumberSize = style.minimumNumberSize;

        this.increaseBackground = style.increase;
        this.decreaseBackground = style.decrease;
        if (style.increase instanceof TextureRegionDrawable) {
            this.increaseBackgroundDisabled = ((TextureRegionDrawable) style.increase).tint(DISABLED_COLOR);
        }
        else if (style.increase instanceof NinePatchDrawable) {
            this.increaseBackgroundDisabled = ((NinePatchDrawable) style.increase).tint(DISABLED_COLOR);
        }
        else if (style.increase instanceof SpriteDrawable) {
            this.increaseBackgroundDisabled = ((SpriteDrawable) style.increase).tint(DISABLED_COLOR);
        }
        else {
            this.increaseBackgroundDisabled = this.increaseBackground;
        }
        if (style.decrease instanceof TextureRegionDrawable) {
            this.decreaseBackgroundDisabled = ((TextureRegionDrawable) style.increase).tint(DISABLED_COLOR);
        }
        else if (style.decrease instanceof NinePatchDrawable) {
            this.decreaseBackgroundDisabled = ((NinePatchDrawable) style.increase).tint(DISABLED_COLOR);
        }
        else if (style.decrease instanceof SpriteDrawable) {
            this.decreaseBackgroundDisabled = ((SpriteDrawable) style.increase).tint(DISABLED_COLOR);
        }
        else {
            this.decreaseBackgroundDisabled = this.decreaseBackground;
        }
        
        this.setIncreaseEnabled(true);
        this.setDecreaseEnabled(true);
        this.setChangeAmount(DEFAULT_CHANGE_AMOUNT);

        this.initComponent();
    }
    
    
    // Public Methods
    public boolean addValueChangedListener(Listener<ValueChangedEventArgs> listener) {
        return this.valueChanged.addListener(listener);
    }
    
    public boolean removeValueChangedListener(Listener<ValueChangedEventArgs> listener) {
        return this.valueChanged.removeListener(listener);
    }


    // Private Methods
    private void initComponent() {
        this.increase.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && NumberSelector.this.getIncreaseEnabled()) {
                    NumberSelector.this.onIncreaseClicked();
                }
            }
        });
        this.decrease.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && NumberSelector.this.getDecreaseEnabled()) {
                    NumberSelector.this.onDecreaseClicked();
                }
            }
        });

        this.add(this.numberLabel).minWidth(this.minimumNumberSize).expandX().space(this.spacing).padBottom(6);
        this.add(this.decrease).space(this.spacing);
        this.add(this.increase).space(this.spacing);
    }

    private void onIncreaseClicked() {
        this.setValue(this.getValue() + this.getChangeAmount());
    }

    private void onDecreaseClicked() {
        this.setValue(this.getValue() - this.getChangeAmount());
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
    
    public static class ValueChangedEventArgs {
        
        // Fields
        public final NumberSelector sender;
        public final int oldValue;
        public final int newValue;
        
        
        // Initialization
        public ValueChangedEventArgs(NumberSelector sender, int oldValue, int newValue) {
            this.sender = sender;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
        
    }

}
