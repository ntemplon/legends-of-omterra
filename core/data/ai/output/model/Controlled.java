// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/27/2015 15:00:42
// ******************************************************* 
package com.jupiter.europa.entity.behavior.model;

/** ModelCondition class created from MMPM condition Controlled. */
public class Controlled extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of Controlled. */
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