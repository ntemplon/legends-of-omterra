package com.jupiter.europa.entity.ability;

import com.jupiter.ganymede.util.Category;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.Set;

/**
 * Created by nathan on 5/20/15.
 */
public interface AbilityCategory extends Category<AbilityCategory> {

    public static final Set<Class<? extends AbilityCategory>> categories = Collections.unmodifiableSet(new Reflections("com.jupiter.europa").getSubTypesOf(AbilityCategory.class));

    @Override
    AbilityCategory getParent();
    String getName();
}
