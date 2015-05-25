package com.jupiter.europa.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.entity.ability.Ability;
import com.jupiter.europa.entity.ability.AbilityCategory;
import com.jupiter.europa.entity.ability.BasicAbilityCategories;
import com.jupiter.ganymede.util.CategorizedTree;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nathan on 5/21/15.
 */
public class AbilityComponent extends Component implements Json.Serializable {

    // Fields
    private final CategorizedTree<Ability, AbilityCategory> abilities = new CategorizedTree<>(BasicAbilityCategories.ALL_ABILITIES);


    // Public Methods
    public void add(Ability ability) {
        this.abilities.add(ability);
    }

    public void remove(Ability ability) {
        this.abilities.remove(ability);
    }

    public Set<Ability> getAbilities(AbilityCategory category) {
        return new TreeSet<>(this.abilities.getItems(category));
    }

    public Set<AbilityCategory> getSubcategories(AbilityCategory category) {
        return new TreeSet<>(this.abilities.getChildren(category));
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonValue) {

    }

}
