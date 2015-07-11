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

// *******************************************************
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/27/2015 15:00:42
// ******************************************************* 
package com.jupiter.europa.entity.behavior.model;

/**
 * ModelCondition class created from MMPM condition Controlled.
 */
public class Controlled extends jbt.model.task.leaf.condition.ModelCondition {

    /**
     * Constructor. Constructs an instance of Controlled.
     */
    public Controlled(jbt.model.core.ModelTask guard) {
        super(guard);
    }

    /**
     * Returns a com.jupiter.europa.entity.behavior.exec.Controlled task that is
     * able to run this task.
     */
    public jbt.execution.core.ExecutionTask createExecutor(
            jbt.execution.core.BTExecutor executor,
            jbt.execution.core.ExecutionTask parent) {
        return new com.jupiter.europa.entity.behavior.exec.Controlled(this,
                executor, parent);
    }
}