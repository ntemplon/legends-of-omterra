package com.jupiter.europa.entity.behavior;

import com.jupiter.europa.entity.ContextData;

import java.util.Collection;

/**
 * Created by nathan on 5/25/15.
 */
public interface Node {
    void init(ContextData data);

    BehaviorTree.NodeStates tick();

    BehaviorTree.NodeStates lastTick();

    Node getParent();

    Collection<Node> getChildren();
}
