package com.jupiter.europa.entity.ability;

/**
 * Created by nathan on 5/21/15.
 */
public class TargetInfo<T extends Target> {

    // Enumerations
    public enum TargetType {
        TILE,
        ENTITY;
    }


    // Fields
    private final TargetType type;
    private final TargetFilter<T> filter;


    // Initialization
    public TargetInfo(TargetType type, TargetFilter<T> filter) {
        this.type = type;
        this.filter = filter;
    }


    @FunctionalInterface
    public interface TargetFilter<T extends Target> {
        boolean accept(Target target);
    }
}
