package com.jupiter.europa.entity.behavior;

import com.jupiter.europa.entity.ContextData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by nathan on 5/25/15.
 */
public class BehaviorTree implements Node {

    // Enumerations
    public enum NodeStates {
        RUNNING,
        SUCCESS,
        FAILURE,
        ERROR;
    }


    // Fields
    private final Node root;
    private final Collection<Node> children;

    private Node parent;
    private NodeStates lastTickResult;
    private Node lastTicked;


    // Properties
    @Override
    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    @Override
    public Collection<Node> getChildren() {
        return this.children;
    }


    // Initialization
    public BehaviorTree(Node root) {
        this.root = root;

        Collection<Node> nodes = new HashSet<>();
        nodes.add(this.root);
        this.children = Collections.unmodifiableCollection(nodes);

        this.lastTicked = this.root;
    }


    // Public Methods
    @Override
    public void init(ContextData data) {

    }

    @Override
    public NodeStates tick() {
        // TODO: Change node on success, failure, or error
        this.lastTickResult = lastTicked.tick();
        return this.lastTickResult;
    }

    @Override
    public NodeStates lastTick() {
        return this.lastTickResult;
    }
}
