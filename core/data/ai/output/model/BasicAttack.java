// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/27/2015 15:00:42
// ******************************************************* 
package com.jupiter.europa.entity.behavior.model;

/** ModelAction class created from MMPM action BasicAttack. */
public class BasicAttack extends jbt.model.task.leaf.action.ModelAction {
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
	 * Constructor. Constructs an instance of BasicAttack.
	 * 
	 * @param targetID
	 *            value of the parameter "targetID", or null in case it should
	 *            be read from the context. If null, <code>targetIDLoc</code>
	 *            cannot be null.
	 * @param targetIDLoc
	 *            in case <code>targetID</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public BasicAttack(jbt.model.core.ModelTask guard,
			java.lang.Integer targetID, java.lang.String targetIDLoc) {
		super(guard);
		this.targetID = targetID;
		this.targetIDLoc = targetIDLoc;
	}

	/**
	 * Returns a com.jupiter.europa.entity.behavior.exec.BasicAttack task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.jupiter.europa.entity.behavior.exec.BasicAttack(this,
				executor, parent, this.targetID, this.targetIDLoc);
	}
}