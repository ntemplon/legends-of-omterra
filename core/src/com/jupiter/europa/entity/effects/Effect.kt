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

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.traits.Qualifications
import com.jupiter.europa.entity.traits.Qualifier

/**

 * @author Nathan Templon
 */
public interface Effect : Serializable, Comparable<Effect> {

    public val qualifier: Qualifier

    public val icon: Sprite
    public val name: String
    public val description: String

    public fun onAdd(entity: Entity)

    public fun onRemove()
    public fun update(deltaT: Float)
    public fun onCombatTurnStart()
    public fun onCombatTurnEnd()
    public fun onOutOfCombatTurn()

    override fun compareTo(other: Effect): Int {
        return this.name.compareTo(other.name)
    }

    companion object {
        public val NO_OP: Effect = object : Effect {
            override val qualifier = Qualifications.ACCEPT
            override val icon = Sprite()
            override val name = "NO_OP"
            override val description = "Effect that performs no operation."

            override fun onAdd(entity: Entity) {
            }

            override fun onRemove() {
            }

            override fun update(deltaT: Float) {
            }

            override fun onCombatTurnStart() {
            }

            override fun onCombatTurnEnd() {
            }

            override fun onOutOfCombatTurn() {
            }

            override fun write(json: Json) {
            }

            override fun read(json: Json, value: JsonValue) {
            }
        }
    }

}
