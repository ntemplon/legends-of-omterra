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
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import com.jupiter.europa.entity.traits.feat.Feat

/**
 * Created by nathan on 5/18/15.
 */
public class Qualifications private constructor(quals: (Entity?) -> Boolean) : Qualifier {

    // Properties
    private val quals: (Entity?) -> Boolean = quals


    // Initialization
    private constructor(quals: Qualifier) : this({ entity -> quals.qualifies(entity) })


    // Public Methods
    override fun qualifies(entity: Entity?): Boolean {
        return this.quals.invoke(entity)
    }

    public fun or(other: Qualifier): Qualifications {
        return Qualifications { entity ->
            this.qualifies(entity) || other.qualifies(entity)
        }
    }

    public fun and(other: Qualifier): Qualifications {
        return Qualifications { entity ->
            this.qualifies(entity) && other.qualifies(entity)
        }
    }

    public fun not(): Qualifications {
        return Qualifications { entity ->
            !this.qualifies(entity)
        }
    }

    companion object {

        public val ACCEPT: Qualifications = Qualifications({ entity -> false })
        public val REJECT: Qualifications = Qualifications({ entity -> false })

        // Static Methods
        public fun has(featClass: Class<out Feat>): Qualifications {
            return Qualifications(FeatPresentQualifier(featClass))
        }

        public fun lacks(featClass: Class<out Feat>): Qualifications {
            return Qualifications(FeatNotPresentQualifier(featClass))
        }

        public fun all(vararg quals: Qualifier): Qualifications {
            return Qualifications { entity ->
                quals.fold(true, { qualifies, nextQual ->
                    qualifies && nextQual.qualifies(entity)
                })
            }
        }

        public fun any(vararg quals: Qualifier): Qualifications {
            return Qualifications { entity ->
                quals.any { it.qualifies(entity) }
            }
        }

        public fun none(vararg quals: Qualifier): Qualifications {
            return Qualifications { entity ->
                quals.fold(true, { qualifies, nextQual ->
                    qualifies && !nextQual.qualifies(entity)
                })
            }
        }

        public fun minStat(attribute: AttributeSet.Attributes, minValue: Int): Qualifications {
            return Qualifications { entity ->
                entity != null &&
                        Families.attributed.matches(entity) &&
                        Mappers.attributes[entity].baseAttributes.getAttribute(attribute) >= minValue
            }
        }

        public fun level(characterClass: Class<out CharacterClass>, level: Int): Qualifications {
            return Qualifications { entity ->
                if (Families.classed.matches(entity)) {
                    val charClass = Mappers.characterClass[entity].characterClass
                    characterClass == charClass.javaClass && charClass.level >= level
                } else {
                    false
                }
            }
        }
    }
}
