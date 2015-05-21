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

import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.stats.AttributeSet.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Nathan Templon
 */
public class AttributeSelector extends MultipleNumberSelector {

    // Constants
    private static final List<String> attributeNamesInternal = new ArrayList<>(AttributeSet.PRIMARY_ATTRIBUTES.size());

    static {
        attributeNamesInternal.addAll(
                AttributeSet.PRIMARY_ATTRIBUTES.stream()
                .map((Attributes attr) -> attr.getDisplayName())
                        .collect(Collectors.toList())
        );
    }

    public static final List<String> ATTRIBUTE_NAMES = Collections.unmodifiableList(attributeNamesInternal);


    // Properties
    public final AttributeSet getAttributes() {
        AttributeSet set = new AttributeSet();

        Map<String, Integer> values = this.getValues();
        values.keySet().stream().forEach((String attrName) -> {
            set.setAttribute(Attributes.getByDisplayName(attrName), values.get(attrName));
        });

        return set;
    }


    // Initialization
    public AttributeSelector(int maxSelection, MultipleNumberSelectorStyle style, int columns) {
        super(maxSelection, style, ATTRIBUTE_NAMES, columns);
    }

    public AttributeSelector(int maxSelection, MultipleNumberSelectorStyle style) {
        this(maxSelection, style, 1);
    }

}
