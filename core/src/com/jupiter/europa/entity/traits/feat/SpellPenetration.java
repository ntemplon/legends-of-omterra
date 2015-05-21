package com.jupiter.europa.entity.traits.feat;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.entity.effects.AttributeModifierEffect;
import com.jupiter.europa.entity.effects.Effect;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.traits.FeatNotPresentQualifier;
import com.jupiter.europa.entity.traits.Qualifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nathan on 5/18/15.
 */
public class SpellPenetration implements Feat {

    // Fields
    private final Qualifier quals = new FeatNotPresentQualifier(SpellPenetration.class);
    private final Effect effect = new SpellPenetrationEffect();
    private final Sprite icon = new Sprite();


    // Feat Implementation
    @Override
    public Qualifier getQualifier() {
        return this.quals;
    }

    @Override
    public Sprite getIcon() {
        return this.icon;
    }

    @Override
    public String getName() {
        return "Spell Penetration";
    }

    @Override
    public String getDescription() {
        return "Your spells ignore 10 of their targets' Spell Resistance.";
    }

    @Override
    public Effect getEffect() {
        return this.effect;
    }


    // Serializable (json) implementation
    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }


    // Effect
    private static class SpellPenetrationEffect extends AttributeModifierEffect {

        // Fields
        private final Map<AttributeSet.Attributes, Integer> modifiers = new HashMap<>();


        // Properties
        @Override
        public Map<AttributeSet.Attributes, Integer> getModifiers() {
            return this.modifiers;
        }


        // Initialization
        public SpellPenetrationEffect() {
            this.modifiers.put(AttributeSet.Attributes.SPELL_PENETRATION, 10);
        }
    }
}
