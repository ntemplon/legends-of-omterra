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
 * ExecutionCondition class created from MMPM condition Controlled.
 */
public class Controlled extends
        jbt.execution.task.leaf.condition.ExecutionCondition {

    /**
     * Constructor. Constructs an instance of Controlled that is able to run a
     * com.jupiter.europa.entity.behavior.model.Controlled.
     */
    public Controlled(
            com.jupiter.europa.entity.behavior.model.Controlled modelTask,
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