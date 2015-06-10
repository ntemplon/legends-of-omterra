package com.jupiter.europa.entity.traits.feat

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.effects.AttributeModifierEffect
import com.jupiter.europa.entity.effects.Effect
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.traits.FeatNotPresentQualifier
import com.jupiter.europa.entity.traits.Qualifier

/**
 * Created by nathan on 6/8/15.
 */
public class IronWill : Feat {

    // Properties
    private val qualifier = FeatNotPresentQualifier(javaClass<IronWill>())
    private val effect = IronWillEffect()

    private var sprite: Sprite? = null


    // Feat Implementation
    override fun getQualifier(): Qualifier? {
        return this.qualifier
    }

    override fun getIcon(): Sprite? {
        return this.sprite
    }

    override fun getName(): String? {
        return "Iron Will"
    }

    override fun getDescription(): String? {
        return "You gain +10 Will."
    }

    override fun getEffect(): Effect? {
        return this.effect
    }


    // Serializable Implementation
    override fun write(json: Json?) {

    }

    override fun read(json: Json?, jsonData: JsonValue?) {

    }

    private class IronWillEffect() : AttributeModifierEffect() {

        private val modifiers = java.util.EnumMap<AttributeSet.Attributes, Int>(javaClass<AttributeSet.Attributes>())

        init {
            this.modifiers.put(AttributeSet.Attributes.WILL, 10)
        }

        override fun getModifiers(): MutableMap<AttributeSet.Attributes, Int>? {
            return this.modifiers
        }

    }

}