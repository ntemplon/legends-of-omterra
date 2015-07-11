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
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 05/27/2015 15:00:42
// ******************************************************* 
package com.jupiter.europa.entity.behavior.exec;

/**
 * ExecutionCondition class created from MMPM condition Following.
 */
public class Following extends
        jbt.execution.task.leaf.condition.ExecutionCondition {

    /**
     * Constructor. Constructs an instance of Following that is able to run a
     * com.jupiter.europa.entity.behavior.model.Following.
     */
    public Following(
            com.jupiter.europa.entity.behavior.model.Following modelTask,
            jbt.execution.core.BTExecutor executor,
            jbt.execution.core.ExecutionTask parent) {
        super(modelTask, executor, parent);
    }

    protected void internalSpawn() {
        /*
		 * Do not remove this first line unless you know what it does and you
		 * need not do it.
		 */
        this.getExecutor().requestInsertionIntoList(
                jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		/* TODO: this method's implementation must be completed. */
        System.out.println(this.getClass().getCanonicalName() + " spawned");
    }

    protected jbt.execution.core.ExecutionTask.Status internalTick() {
		/*
		 * TODO: this method's implementation must be completed. This function
		 * should only return Status.SUCCESS, Status.FAILURE or Status.RUNNING.
		 * No other values are allowed.
		 */
        return jbt.execution.core.ExecutionTask.Status.SUCCESS;
    }

    protected void internalTerminate() {
		/* TODO: this method's implementation must be completed. */
    }

    protected void restoreState(jbt.execution.core.ITaskState state) {
		/* TODO: this method's implementation must be completed. */
    }

    protected jbt.execution.core.ITaskState storeState() {
		/* TODO: this method's implementation must be completed. */
        return null;
    }

    protected jbt.execution.core.ITaskState storeTerminationState() {
		/* TODO: this method's implementation must be completed. */
        return null;
    }
}