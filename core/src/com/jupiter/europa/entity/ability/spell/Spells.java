package com.jupiter.europa.entity.ability.spell;

import com.jupiter.europa.entity.ability.Ability;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by nathan on 5/25/15.
 */
public final class Spells {

    // Constants
    private static final Reflections spellReflections = new Reflections("com.jupiter.europa");
    private static final Set<Class<? extends Ability>> spellTypes = spellReflections.getTypesAnnotatedWith(Spell.class, true).stream()
            .filter(type -> Ability.class.isAssignableFrom(type))
            .map(type -> (Class<? extends Ability>) type)
            .collect(Collectors.toSet());
    private static final Map<Class<? extends CharacterClass>, Map<Integer, Set<Class<? extends Ability>>>> SPELL_LIST = spellTypes.stream()
            .collect(Collectors.groupingBy(type -> type.getAnnotation(Spell.class).characterClass(),
                    Collectors.groupingBy(type -> type.getAnnotation(Spell.class).level(),
                            Collectors.toSet())
            ));
    private static final Set<Class<? extends Ability>> EMPTY = Collections.unmodifiableSet(new HashSet<>());


    // Static Methods
    public static Set<Class<? extends Ability>> getSpells(Class<? extends CharacterClass> characterClass, Integer level) {
        if (SPELL_LIST.containsKey(characterClass)) {
            Map<Integer, Set<Class<? extends Ability>>> spells = SPELL_LIST.get(characterClass);
            if (spells.containsKey(level)) {
                return Collections.unmodifiableSet(spells.get(level));
            }
        }
        return EMPTY;
    }


    // Initialization
    private Spells() {

    }
}
