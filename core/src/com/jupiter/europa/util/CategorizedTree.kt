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

package com.jupiter.europa.util

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.LinkedHashSet

/**
 * Created by nathan on 5/21/15.
 */
public class CategorizedTree<T : Categorized<C>, C : Category<C>>(root: C) {

    // Static Methods
    private fun <U : Category<U>> decompose(finalCategory: U): List<U> {
        val list = ArrayList<U>()
        var cat: U? = finalCategory
        do {
            list.add(0, cat)
            cat = cat?.parent
        } while (cat != null)
        return list
    }


    // Fields
    public val root: Node<T, C>


    init {
        this.root = Node<T, C>(root)
    }


    // Public Methods
    public fun add(item: T) {
        this.nodeFor(item.category)?.addValue(item)
    }

    public fun remove(item: T) {
        this.nodeFor(item.category)?.removeValue(item)
    }

    public fun getItems(category: C): Set<T> {
        val node = this.nodeFor(category)
        if (node != null) {
            return node.values
        } else {
            return setOf()
        }
    }

    public fun getChildren(category: C): Set<C> {
        val node = this.nodeFor(category)
        return if (node != null) {
            node.getChildren()
                    .map { it.category }
                    .toSet()
        } else {
            setOf()
        }
    }


    // Private Methods
    private fun nodeFor(category: C): Node<T, C>? {
        val categories = decompose(category)
        val index = categories.indexOf(this.root.category)
        return if (index != -1) {
            var node = this.root
            for (i in index + 1..categories.size() - 1) {
                node = node.getChild(categories.get(i))
            }
            node
        } else {
            null
        }
    }


    // Nested Classes
    public class Node<T : Categorized<C>, C : Category<C>>(public val category: C) {
        public val values: Set<T>
            get() {
                return this.valuesInternal.toSet()
            }

        private val children = LinkedHashMap<C, Node<T, C>>()
        private val valuesInternal = LinkedHashSet<T>()

        public fun getChild(category: C): Node<T, C> {
            if (!this.children.containsKey(category)) {
                this.children.put(category, Node<T, C>(category))
            }
            return this.children.get(category)
        }

        public fun getChildren(): Collection<Node<T, C>> = this.children.values().toSet()


        // Private Methods
        internal fun addChild(node: Node<T, C>) {
            this.children.put(node.category, node)
        }

        internal fun addValue(value: T) {
            this.valuesInternal.add(value)
        }

        internal fun removeValue(value: T) {
            this.valuesInternal.remove(value)
        }
    }
}
