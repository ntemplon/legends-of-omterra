package com.jupiter.europa.entity.ability.spell

import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import org.reflections.Reflections
import kotlin.reflect.KClass

/**
 * Created by nathan on 5/25/15.
 */
public object Spells {

    // Constants
    private val spellReflections = Reflections("com.jupiter.europa")
    private val spellTypes: Set<Class<out Ability>> = spellReflections.getTypesAnnotatedWith(javaClass<Spell>(), true)
            .filter { type -> javaClass<Ability>().isAssignableFrom(type) }
            .map { type -> type as? java.lang.Class<out Ability> }
            .filterNotNull()
            .toSet()
    private val SPELL_LIST: Map<KClass<out CharacterClass>, Map<Int, Set<Class<out Ability>>>> = spellTypes
            .groupBy({ type -> type.getAnnotation(javaClass<Spell>()).characterClass })
            .mapValues { entry ->
                entry.getValue().groupBy { spellType ->
                    spellType.getAnnotation(javaClass<Spell>()).level
                }
                        .mapValues { entry -> entry.getValue().toSet() }
            }
    private val EMPTY: Set<Class<out Ability>> = setOf()


    // Static Methods
    public fun getSpells(characterClass: Class<out CharacterClass>, level: Int?): Set<Class<out Ability>> {
        if (SPELL_LIST.containsKey(characterClass)) {
            val spells = SPELL_LIST.get(characterClass)
            if (spells != null && spells.containsKey(level)) {
                return spells.get(level) ?: EMPTY
            }
        }
        return EMPTY
    }
}
