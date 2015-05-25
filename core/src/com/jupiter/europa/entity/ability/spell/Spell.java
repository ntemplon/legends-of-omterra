package com.jupiter.europa.entity.ability.spell;

import com.jupiter.europa.entity.stats.characterclass.CharacterClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nathan on 5/25/15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Spell {
    Class<? extends CharacterClass> characterClass();

    int level();
}
