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
package com.jupiter.europa.entity.ability

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.jupiter.europa.util.Categorized

/**

 * @author Nathan Templon
 */
public interface Ability : Categorized<AbilityCategory>, Comparable<Ability> {
    public val name: String
    public val description: String
    public val cost: Cost
    public val action: Action
    override val category: AbilityCategory
    public val icon: TextureRegion

    override fun compareTo(other: Ability): Int {
        return this.name.compareTo(other.name)
    }
}
