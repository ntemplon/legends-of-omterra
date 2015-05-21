package com.jupiter.europa.entity.ability;

import org.reflections.Reflections;

import java.util.Collections;
import java.util.Set;

/**
 * Created by nathan on 5/20/15.
 */
public interface AbilityCategory {

    public static final Set<Class<? extends AbilityCategory>> categories = Collections.unmodifiableSet(new Reflections("com.jupiter.europa").getSubTypesOf(AbilityCategory.class));

    AbilityCategory getParent();

    String getName();
}
