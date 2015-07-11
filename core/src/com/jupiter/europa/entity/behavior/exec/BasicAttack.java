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
 * ExecutionAction class created from MMPM action BasicAttack.
 */
public class BasicAttack extends jbt.execution.task.leaf.action.ExecutionAction {
    /**
     * Value of the parameter "targetID" in case its value is specified at
     * construction time. null otherwise.
     */
    private java.lang.Integer targetID;
    /**
     * Location, in the context, of the parameter "targetID" in case its value
     * is not specified at construction time. null otherwise.
     */
    private java.lang.String targetIDLoc;

    /**
     * Constructor. Constructs an instance of BasicAttack that is able to run a
     * com.jupiter.europa.entity.behavior.model.BasicAttack.
     *
     * @param targetID    value of the parameter "targetID", or null in case it should
     *                    be read from the context. If null,
     *                    <code>targetIDLoc<code> cannot be null.
     * @param targetIDLoc in case <code>targetID</code> is null, this variable
     *                    represents the place in the context where the parameter's
     *                    value will be retrieved from.
     */
    public BasicAttack(
            com.jupiter.europa.entity.behavior.model.BasicAttack modelTask,
            jbt.execution.core.BTExecutor executor,
            jbt.execution.core.ExecutionTask parent,
            java.lang.Integer targetID, java.lang.String targetIDLoc) {
        super(modelTask, executor, parent);

        this.targetID = targetID;
        this.targetIDLoc = targetIDLoc;
    }

    /**
     * Returns the value of the parameter "targetID", or null in case it has not
     * been specified or it cannot be found in the context.
     */
    public java.lang.Integer getTargetID() {
        if (this.targetID != null) {
            return this.targetID;
        } else {
            return (java.lang.Integer) this.getContext().getVariable(
                    this.targetIDLoc);
        }
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