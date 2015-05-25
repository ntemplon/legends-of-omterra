package com.jupiter.europa.entity.effects;

import com.jupiter.europa.entity.ability.AttackAbility;
import com.jupiter.europa.entity.ability.MoveAbility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nathan on 5/24/15.
 */
public class BasicAbilitiesEffect extends MultiEffect {

    // Fields
    private final List<Effect> effects = new ArrayList<>();
    private final List<Effect> effectsView = Collections.unmodifiableList(this.effects);


    // Public Methods
    @Override
    public List<Effect> getEffects() {
        if (this.effects.isEmpty()) {
            synchronized (this.effects) {
                if (this.effects.isEmpty()) {
                    this.generateEffects();
                }
            }
        }
        return this.effectsView;
    }


    // Private Methods
    private void generateEffects() {
        this.effects.add(new AbilityGrantingEffect(new MoveAbility(this.getEntity())));
        this.effects.add(new AbilityGrantingEffect(new AttackAbility(this.getEntity())));
    }
}
