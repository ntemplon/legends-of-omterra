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
package com.emergence.entity.trait.feat;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.entity.effects.AttributeModifierEffect;
import com.emergence.entity.effects.Effect;
import com.emergence.entity.trait.Qualifications;
import com.emergence.entity.trait.FeatNotPresentQualifications;

/**
 *
 * @author Nathan Templon
 */
public class GreatFortitude implements Feat {

    // Fields
    private final Qualifications qualifications = new FeatNotPresentQualifications(GreatFortitude.class);
    private final Effect effect = new GreatFortitudeEffect();
    
    @Override
    public Qualifications getQualifications() {
        return this.qualifications;
    }
    
    @Override
    public Effect getEffect() {
        return this.effect;
    }

    @Override
    public Sprite getIcon() {
        return new Sprite();
    }

    @Override
    public String getName() {
        return "Great Fortitude";
    }

    @Override
    public String getDescription() {
        return "You gain +10 Fortitude.";
    }

    @Override
    public void write(Json json) {
        
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        
    }
    
    
    // Effect
    private static class GreatFortitudeEffect extends AttributeModifierEffect {
        @Override
        public int getFortitudeModifier() {
            return 10;
        }
    }
    
}
