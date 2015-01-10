/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
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
package com.jupiter.europa.entity;

import com.jupiter.europa.entity.component.CollisionComponent;
import com.badlogic.ashley.core.Family;
import com.jupiter.europa.entity.component.AttributesComponent;
import com.jupiter.europa.entity.component.CharacterClassComponent;
import com.jupiter.europa.entity.component.EffectsComponent;
import com.jupiter.europa.entity.component.PositionComponent;
import com.jupiter.europa.entity.component.RenderComponent;
import com.jupiter.europa.entity.component.SizeComponent;
import com.jupiter.europa.entity.component.MovementResourceComponent;
import com.jupiter.europa.entity.component.RaceComponent;
import com.jupiter.europa.entity.component.WalkComponent;

/**
 *
 * @author Nathan Templon
 */
public final class Families {
    
    // Constants
    public static final Family attributed = Family.all(AttributesComponent.class).get();
    public static final Family classed = Family.all(CharacterClassComponent.class).get();
    public static final Family collidables = Family.all(CollisionComponent.class).get();
    public static final Family affectables = Family.all(EffectsComponent.class).get();
    public static final Family positionables = Family.all(PositionComponent.class, SizeComponent.class).get();
    public static final Family raced = Family.all(RaceComponent.class).get();
    public static final Family renderables = Family.all(RenderComponent.class).get();
    public static final Family walkables = Family.all(PositionComponent.class, SizeComponent.class, WalkComponent.class,
            MovementResourceComponent.class).get();
    
    
    // Initialization
    private Families() {
        
    }
    
}
