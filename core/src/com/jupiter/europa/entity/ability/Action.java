package com.jupiter.europa.entity.ability;

/**
 * Created by nathan on 5/20/15.
 */
public interface Action {

    default boolean requiresTargets() {
        return this.getTargetCount() != 0;
    }

    int getTargetCount();

    void apply();
}
