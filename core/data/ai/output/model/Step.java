// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/27/2015 15:00:42
// ******************************************************* 
package com.jupiter.europa.entity.behavior.model;

/** ModelAction class created from MMPM action Step. */
public class Step extends jbt.model.task.leaf.action.ModelAction {
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
	 * Constructor. Constructs an instance of Step.
	 * 
	 * @param direction
	 *            value of the parameter "direction", or null in case it should
	 *            be read from the context. If null, <code>directionLoc</code>
	 *            cannot be null.
	 * @param directionLoc
	 *            in case <code>direction</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public Step(jbt.model.core.ModelTask guard, java.lang.Integer direction,
			java.lang.String directionLoc) {
		super(guard);
		this.direction = direction;
		this.directionLoc = directionLoc;
	}

	/**
	 * Returns a com.jupiter.europa.entity.behavior.exec.Step task that is able
	 * to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.jupiter.europa.entity.behavior.exec.Step(this, executor,
				parent, this.direction, this.directionLoc);
	}
}