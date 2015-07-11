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

package com.jupiter.europa.entity.traits

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.EuropaGame
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

/**

 * @author Nathan Templon
 * *
 * @param
 */
public abstract class TraitPool<T : Trait>(public val name: String) : Serializable {


    // Properties
    private val source = ArrayList<T>()
    private val qualifiedInternal = ArrayList<T>()
    private val selected = ArrayList<T>()
    private val selection = Event<TraitPoolEvent<T>>()

    public val sources: List<T> = Collections.unmodifiableList(this.source)
    public val qualified: List<T> = Collections.unmodifiableList<T>(this.qualifiedInternal)
    public val selections: List<T> = Collections.unmodifiableList(this.selected)
    public val full: Boolean
        get() = this.numberOfSelections >= this.capacity
    public val numberOfSelections: Int
        get() = this.selected.size()

    public var capacity: Int = 0
        private set
    public var owner: Entity? = null
    public var autoQualify: Boolean = false
    public var sorted: Boolean = true
    public var sourceComparator: Comparator<T> = Comparator { first, second ->
        first.name.compareTo(second.name)
    }


    // Initialization
    public constructor(owner: Entity, name: String) : this(name) {
        this.owner = owner
    }


    // Public Methods
    public fun addSource(source: T) {
        this.addSourceInternal(source, true)
    }

    public fun addSource(source: Iterable<T>) {
        source.forEach { item ->
            this.addSourceInternal(item, false)
        }
        this.sort()
    }

    public fun increaseCapacity(additional: Int) {
        if (additional <= 0) {
            return
        }

        this.capacity += additional
    }

    public fun reassesQualifications() {
        this.qualifiedInternal.clear()
        this.source.filter { item ->
            item.qualifier.qualifies(this.owner)
        }.forEach { item ->
            this.qualifiedInternal.add(item)
        }
    }

    public fun select(`trait`: T): Boolean {
        if (this.source.contains(`trait`)) {
            this.selected.add(`trait`)

            // Dispatch Message
            this.selection.dispatch(TraitPoolEvent(this, `trait`))

            if (this.autoQualify) {
                this.reassesQualifications()
            }

            return true
        }
        return false
    }

    public fun addSelectionListener(listener: Listener<TraitPoolEvent<T>>): Boolean {
        return this.selection.addListener(listener)
    }

    public fun removeSelectionListener(listener: Listener<TraitPoolEvent<T>>): Boolean {
        return this.selection.removeListener(listener)
    }


    // Private Methods
    private fun addSourceInternal(source: T, resort: Boolean) {
        this.source.add(source)
        if (this.autoQualify && source.qualifier.qualifies(this.owner)) {
            this.qualifiedInternal.add(source)
        }

        if (resort) {
            this.sort()
        }
    }

    private fun sort() {
        if (this.sorted) {
            Collections.sort(this.source, this.sourceComparator)
            Collections.sort(this.qualified, this.sourceComparator)
        }
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(CAPACITY_KEY, this.capacity)

        json.writeArrayStart(SELECTED_KEY)
        this.selected.forEach { item ->
            json.writeObjectStart()
            json.writeValue(ITEM_CLASS_KEY, item.javaClass.getName())
            json.writeValue(ITEM_DATA_KEY, item, item.javaClass)
            json.writeObjectEnd()
        }
        json.writeArrayEnd()
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(CAPACITY_KEY)) {
            this.capacity = jsonData.getInt(CAPACITY_KEY)
        }

        if (jsonData.has(SELECTED_KEY)) {
            val selectedData = jsonData.get(SELECTED_KEY)
            if (selectedData.isArray()) {
                selectedData.filter { value ->
                    value.has(ITEM_CLASS_KEY) && value.has(ITEM_DATA_KEY)
                }.forEach { value ->
                    val typeName = value.getString(ITEM_CLASS_KEY)
                    try {
                        val type = Class.forName(typeName)
                        if (javaClass<Trait>().isAssignableFrom(type)) {
                            this.selected.add(json.fromJson(type, value.get(ITEM_DATA_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS)) as T);
                        }
                    } catch(ex: Exception) {

                    }
                }
            }
        }
    }


    // Nested Classes
    public data class TraitPoolEvent<T : Trait>(public val pool: TraitPool<T>, public val `trait`: T)

    companion object {

        // Constants
        private val CAPACITY_KEY = "capacity"
        private val SELECTED_KEY = "selected"
        private val ITEM_CLASS_KEY = "item-class"
        private val ITEM_DATA_KEY = "item-data"
    }

}
