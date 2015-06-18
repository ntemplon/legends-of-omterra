package com.jupiter.europa.entity.ability

/**
 * Created by nathan on 5/21/15.
 */
public class TargetInfo<T : Target>// Initialization
(private val type: TargetInfo.TargetType, private val filter: TargetInfo.TargetFilter<T>) {

    // Enumerations
    public enum class TargetType {
        TILE,
        ENTITY
    }


    FunctionalInterface
    public interface TargetFilter<T : Target> {
        public fun accept(target: Target): Boolean
    }
}
