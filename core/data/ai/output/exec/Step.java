// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 05/27/2015 15:00:42
// ******************************************************* 
package com.jupiter.europa.entity.behavior.exec;

/** ExecutionAction class created from MMPM action Step. */
public class Step extends jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "direction" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Integer direction;
	/**
	 * Location, in the context, of the parameter "direction" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String directionLoc;

	/**
	 * Constructor. Constructs an instance of Step that is able to run a
	 * com.jupiter.europa.entity.behavior.model.Step.
	 * 
	 * @param direction
	 *            value of the parameter "direction", or null in case it should
	 *            be read from the context. If null,
	 *            <code>directionLoc<code> cannot be null.
	 * @param directionLoc
	 *            in case <code>direction</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public Step(com.jupiter.europa.entity.behavior.model.Step modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent,
			java.lang.Integer direction, java.lang.String directionLoc) {
		super(modelTask, executor, parent);

		this.direction = direction;
		this.directionLoc = directionLoc;
	}

	/**
	 * Returns the value of the parameter "direction", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public java.lang.Integer getDirection() {
		if (this.direction != null) {
			return this.direction;
		} else {
			return (java.lang.Integer) this.getContext().getVariable(
					this.directionLoc);
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