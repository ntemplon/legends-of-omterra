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

/**
 * Created by nathan on 5/20/15.
 */
public abstract class Action {

    // TODO: Figure out this API

    public open fun requiresTargets(): Boolean {
        return this.getTargetCount() != 0
    }

    public open fun getTargetCount(): Int {
        return this.getTargetRequirements().size()
    }

    public abstract fun getTargetRequirements(): List<TargetInfo<Target>>
    public abstract fun apply(targets: List<Target>)

    companion object {
        public val NO_OP: Action = NoOpAction()
    }

    private class NoOpAction : Action() {
        override fun requiresTargets(): Boolean = false
        override fun getTargetCount(): Int = 0
        override fun getTargetRequirements(): List<TargetInfo<Target>> = listOf()
        override fun apply(targets: List<Target>) {
        }
    }
}
