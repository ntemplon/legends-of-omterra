package com.jupiter.europa.entity.ability;

import java.util.List;

/**
 * Created by nathan on 5/20/15.
 */
public abstract class Action {

    // TODO: Figure out this API

    public boolean requiresTargets() {
        return this.getTargetCount() != 0;
    }

    public int getTargetCount() {
        return this.getTargetRequirements().size();
    }

    public abstract List<TargetInfo> getTargetRequirements();

    public abstract void apply(List<Target> targets);
}
