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

package com.jupiter.europa.entity.ability.spell

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import com.jupiter.europa.entity.traits.EffectPool

/**
 * Created by nathan on 8/8/15.
 */
public class SpellPool : EffectPool<SpellEffect> {

    private var characterClass: Class<out CharacterClass>? = null
        get() = this.$characterClass
        set(value) {
            this.$characterClass = value
            this.attemptDefaultSources()
        }
    private var category: SpellCategories? = null
        get() = this.$category
        set(value) {
            this.$category = value
            this.name = value?.name ?: DEFAULT_NAME
            this.attemptDefaultSources()
        }
    private var loadedSources = false

    private val loadSourceLock = Object()

    public constructor() : super(DEFAULT_NAME) {
    }

    public constructor(characterClass: Class<out CharacterClass>, category: SpellCategories) : this() {
        this.characterClass = characterClass
        this.category = category
    }

    private fun attemptDefaultSources() {
        synchronized(loadSourceLock) {
            if (this.loadedSources) {
                return
            }
            this.addSource(Spells.getSpells(this.characterClass ?: return, this.category?.level ?: return).map { SpellEffect(it) })
            this.loadedSources = true
        }
    }

    override fun write(json: Json) {
        val cc = this.characterClass
        if (cc != null) {
            json.writeValue(CLASS_KEY, cc.getName())
        }

        val cat = this.category
        if (cat != null) {
            json.writeValue(CATEGORY_KEY, cat.toString())
        }

        super.write(json)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(CLASS_KEY)) {
            this.characterClass = Class.forName(jsonData.getString(CLASS_KEY)) as? Class<out CharacterClass>
        }

        if (jsonData.has(CATEGORY_KEY)) {
            this.category = SpellCategories.valueOf(jsonData.getString(CATEGORY_KEY))
        }

        super.read(json, jsonData)
    }

    companion object {
        private val DEFAULT_NAME: String = "Default Spell Name"
        private val CLASS_KEY = "class"
        private val CATEGORY_KEY = "category"
    }

}