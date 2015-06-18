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
