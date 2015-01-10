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
 *
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
 */
package com.jupiter.europa.entity.trait;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.trait.feat.Feat;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;

/**
 *
 * @author Nathan Templon
 */
public class FeatPresentQualifications implements Qualifications {
    
    // Fields
    private final Class<? extends Feat> type;


    // Initialization
    public FeatPresentQualifications(Class<? extends Feat> type) {
        this.type = type;
    }


    @Override
    public boolean qualifies(Entity entity) {
        if (Families.classed.matches(entity)) {
            CharacterClass charClass = Mappers.characterClass.get(entity).getCharacterClass();
            if (charClass != null) {
                return !charClass.getFeatPool().getSelections().stream().noneMatch((Feat feat) ->
                        (type.isAssignableFrom(feat.getClass())));
            }
        }
        return false;
    }
    
}
