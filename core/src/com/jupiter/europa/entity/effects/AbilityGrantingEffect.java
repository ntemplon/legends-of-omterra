package com.jupiter.europa.entity.effects;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.ability.Ability;

/**
 * Created by nathan on 5/21/15.
 */
public class AbilityGrantingEffect extends BaseEffect {

    // Fields
    private final Ability ability;


    // Properties
    public Ability getAbility() {
        return this.ability;
    }


    // Initialization
    public AbilityGrantingEffect() {
        this.ability = null;
    }

    public AbilityGrantingEffect(Ability ability) {
        this.ability = ability;
    }


    // Public Methods
    @Override
    public void onAdd(Entity entity) {
        super.onAdd(entity);
        if (Families.abilitied.matches(entity)) {
            Mappers.abilities.get(entity).add(this.getAbility());
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (Families.abilitied.matches(this.getEntity())) {
            Mappers.abilities.get(this.getEntity()).remove(this.ability);
        }
    }
}
