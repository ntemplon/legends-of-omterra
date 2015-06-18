package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.ability.Ability
import com.jupiter.europa.entity.ability.AbilityCategory
import com.jupiter.europa.entity.ability.BasicAbilityCategories
import com.jupiter.ganymede.util.CategorizedTree
import java.util.TreeSet

/**
 * Created by nathan on 5/21/15.
 */
public class AbilityComponent : Component(), Json.Serializable {

    // Fields
    private val abilities = CategorizedTree<Ability, AbilityCategory>(BasicAbilityCategories.ALL_ABILITIES)


    // Public Methods
    public fun add(ability: Ability) {
        this.abilities.add(ability)
    }

    public fun remove(ability: Ability) {
        this.abilities.remove(ability)
    }

    public fun getAbilities(category: AbilityCategory): Set<Ability> {
        return TreeSet(this.abilities.getItems(category))
    }

    public fun getSubcategories(category: AbilityCategory): Set<AbilityCategory> {
        return TreeSet(this.abilities.getChildren(category))
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
    }

    override fun read(json: Json, jsonValue: JsonValue) {
    }

}
