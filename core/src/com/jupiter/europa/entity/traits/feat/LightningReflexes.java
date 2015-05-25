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
public class LightningReflexes implements Feat {

    // Fields
    private final Qualifier quals = new FeatNotPresentQualifier(LightningReflexes.class);
    private final Effect effect = new LightningReflexesEffect();
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
        return "Lightning Reflexes";
    }

    @Override
    public String getDescription() {
        return "You gain 10 Reflexes.";
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
    private static class LightningReflexesEffect extends AttributeModifierEffect {

        // Fields
        private final Map<AttributeSet.Attributes, Integer> modifiers = new HashMap<>();


        // Properties
        @Override
        public Map<AttributeSet.Attributes, Integer> getModifiers() {
            return this.modifiers;
        }


        // Initialization
        public LightningReflexesEffect() {
            this.modifiers.put(AttributeSet.Attributes.REFLEXES, 10);
        }
    }
}