package com.jupiter.europa.entity.ability

import com.jupiter.ganymede.util.Category
import org.reflections.Reflections

/**
 * Created by nathan on 5/20/15.
 */
public interface AbilityCategory : Category<AbilityCategory> {

    override fun getParent(): AbilityCategory?
    override fun getName(): String

    companion object {
        public val categories: Set<Class<out AbilityCategory>> = Reflections("com.jupiter.europa").getSubTypesOf(javaClass<AbilityCategory>())
    }
}
