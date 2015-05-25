package com.jupiter.europa.entity.ability;

/**
 * Created by nathan on 5/21/15.
 */
public class TargetInfo {

    // Enumerations
    public enum TargetType {
        TILE,
        ENTITY;
    }


    // Fields
    private final TargetType type;
    private final TargetFilter filter;


    // Initialization
    public TargetInfo(TargetType type, TargetFilter filter) {
        this.type = type;
        this.filter = filter;
    }


    @FunctionalInterface
    public interface TargetFilter {
        boolean accept(Target target);
    }
}
