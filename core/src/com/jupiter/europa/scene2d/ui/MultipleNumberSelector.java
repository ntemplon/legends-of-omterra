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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jupiter.europa.scene2d.ui.NumberSelector.NumberSelectorStyle;
import com.jupiter.europa.scene2d.ui.NumberSelector.ValueChangedEventArgs;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public class MultipleNumberSelector extends Table {

    // Static Methods
    private static String toConventionalCase(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }


    // Fields
    private final int maxPoints;
    private final int numColumns;
    private final LabelStyle labelStyle;
    private final NumberSelectorStyle numberStyle;
    private final int spacing;
    private final Collection<String> values;

    private final Map<String, NumberSelector> selectors = new LinkedHashMap<>();

    private Label totalLabel;
    private Label totalValueLabel;
    private boolean useMaximumNumber;
    private boolean useMinimumNumber;
    private int maximumNumber;
    private int minimumNumber;


    // Properties
    public final int getMaxPoints() {
        return this.maxPoints;
    }

    public final int getTotalSelected() {
        return this.selectors.keySet().stream().mapToInt((String name) -> this.selectors.get(name).getValue()).sum();
    }

    public final Map<String, Integer> getValues() {
        Map<String, Integer> currentValues = new HashMap<>();

        this.selectors.keySet().stream().forEach((String key) -> currentValues.put(key, this.selectors.get(key).getValue()));

        return Collections.unmodifiableMap(currentValues);
    }

    public final void setValues(Map<String, Integer> values) {
        this.selectors.keySet().stream().filter((String val) -> this.selectors.containsKey(val)).forEach((String val) -> {
            this.selectors.get(val).setValue(values.get(val));
        });
    }

    public final boolean isUseMaximumNumber() {
        return useMaximumNumber;
    }

    public final void setUseMaximumNumber(boolean useMaximumNumber) {
        this.useMaximumNumber = useMaximumNumber;
        this.onValueChanged(null);
    }

    public final boolean isUseMinimumNumber() {
        return useMinimumNumber;
    }

    public final void setUseMinimumNumber(boolean useMinimumNumber) {
        this.useMinimumNumber = useMinimumNumber;
        this.onValueChanged(null);
    }

    public final int getMaximumNumber() {
        return maximumNumber;
    }

    public final void setMaximumNumber(int maximumNumber) {
        this.maximumNumber = maximumNumber;
        this.onValueChanged(null);
    }

    public final int getMinimumNumber() {
        return minimumNumber;
    }

    public final void setMinimumNumber(int minimumNumber) {
        this.minimumNumber = minimumNumber;
        this.onValueChanged(null);
    }

    public final void setIncrement(int increment) {
        this.selectors.keySet().stream().forEach((String key) -> this.selectors.get(key).setChangeAmount(increment));
    }


    // Initialization
    public MultipleNumberSelector(int maxPoints, MultipleNumberSelectorStyle style, Collection<String> values, int numColumns) {
        this.maxPoints = maxPoints;
        this.numColumns = numColumns;

        this.labelStyle = style.labelStyle;
        this.numberStyle = style.numberSelectorStyle;
        this.spacing = style.spacing;
        this.values = values;

        this.setMaximumNumber(this.maxPoints);
        this.setMinimumNumber(0);
        this.setUseMaximumNumber(true);
        this.setUseMinimumNumber(true);

        this.initComponents();

        this.onValueChanged(null);
    }

    public MultipleNumberSelector(int maxPoints, MultipleNumberSelectorStyle style, Collection<String> values) {
        this(maxPoints, style, values, 1);
    }


    // Private Methods
    private void initComponents() {
        this.totalLabel = new Label("Points Remaining:", this.labelStyle);
        this.totalValueLabel = new Label(this.maxPoints + "", this.labelStyle);

        Table totalTable = new Table();
        totalTable.add(this.totalLabel).left().space(this.spacing);
        totalTable.add(totalValueLabel).left().expandX().space(this.spacing);
        this.add(totalTable).colspan(2 * this.numColumns);
        this.row();

        int addedToRow = 0;
        for (String value : this.values) {
            Label label = new Label(value, this.labelStyle);
            NumberSelector selector = new NumberSelector(numberStyle);
            this.selectors.put(value, selector);

            if (addedToRow >= 1) {
                this.add(label).left().space(spacing * 2).padTop(10).padLeft(30);
            }
            else {
                this.add(label).left().space(spacing * 2).padTop(10);
            }

            if (addedToRow == this.numColumns - 1) {
                this.add(selector).left().expandX().fillX().space(this.spacing);
                this.row();
                addedToRow = 0;
            }
            else {
                this.add(selector).left().space(this.spacing);
                addedToRow++;
            }

            selector.addValueChangedListener(this::onValueChanged);
        }
    }

    private void onValueChanged(ValueChangedEventArgs args) {
        int total = this.getTotalSelected();
        if (total >= this.getMaxPoints()) {
            if (args != null) {
                args.sender.setDecreaseEnabled(!(this.isUseMinimumNumber() && args.sender.getValue() <= this.getMinimumNumber()));
            }
            this.selectors.keySet().stream().forEach((String val) -> this.selectors.get(val).setIncreaseEnabled(false));
        }
        else {
            this.selectors.keySet().stream().forEach((String val) -> {
                NumberSelector selector = this.selectors.get(val);
                selector.setIncreaseEnabled(!(this.isUseMaximumNumber() && selector.getValue() >= this.getMaximumNumber()));
                selector.setDecreaseEnabled(!(this.isUseMinimumNumber() && selector.getValue() <= this.getMinimumNumber()));
            });
        }
        if (this.totalValueLabel != null) {
            this.totalValueLabel.setText((this.getMaxPoints() - total) + "");
        }
        this.invalidate();
    }


    // NestedClasses
    public static class MultipleNumberSelectorStyle {

        // Fields
        public LabelStyle labelStyle;
        public NumberSelectorStyle numberSelectorStyle;
        public int spacing;


        // Initialization
        public MultipleNumberSelectorStyle() {

        }

    }

}
