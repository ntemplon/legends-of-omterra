/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.jupiter.europa.entity.effects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.jupiter.europa.entity.ability.AttackAbility
import com.jupiter.europa.entity.ability.MoveAbility

/**
 * Created by nathan on 5/24/15.
 */
public class BasicAbilitiesEffect : MultiEffect() {

    // Properties
    public override var effects: List<Effect>
        get() {
            val ent = this.entity;
            if (this.effectsInternal.isEmpty() && ent != null) {
                synchronized(effectsLock, {
                    this.effectsInternal = this.generateEffects()
                })
            }
            return this.effectsInternal
        }
        private set(value) {
            this.effectsInternal = value
        }
    private var effectsInternal: List<Effect> = listOf()
    private val effectsLock = Object()

    override val icon: Sprite = Sprite()
    override val name = "Basic Abilities"
    override val description = "A character's basic abilities."


    // Private Methods
    private fun generateEffects(): List<Effect> {
        val ent = this.entity
        return if (ent != null) {
            listOf(
                    AbilityGrantingEffect(MoveAbility(ent)),
                    AbilityGrantingEffect(AttackAbility(ent))
            )
        } else {
            listOf()
        }
    }
}
