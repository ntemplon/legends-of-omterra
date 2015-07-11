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

import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.stats.AttributeSet.Attributes

/**

 * @author Nathan Templon
 */
public class AttributeSelector
@jvmOverloads constructor(maxSelection: Int, style: MultipleNumberSelector.MultipleNumberSelectorStyle, columns: Int = 1)
: MultipleNumberSelector(maxSelection, style, AttributeSelector.ATTRIBUTE_NAMES, columns) {


    // Properties
    public fun getAttributes(): AttributeSet {
        val set = AttributeSet()

        val values: Map<String, Int> = this.getValues()
        for ((name, value) in values) {
            val attribute = Attributes.getByDisplayName(name)
            if (attribute != null) {
                set.setAttribute(attribute, value)
            }
        }

        return set
    }

    companion object {

        // Constants
        public val ATTRIBUTE_NAMES: List<String> = AttributeSet.PRIMARY_ATTRIBUTES.map { attr ->
            attr.displayName
        }.toList()
    }

}
