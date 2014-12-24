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
package com.omterra.entity;

import com.omterra.entity.component.CollisionComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.omterra.entity.component.PositionComponent;
import com.omterra.entity.component.RenderComponent;
import com.omterra.entity.component.SizeComponent;
import com.omterra.entity.component.TextureResourceComponent;
import com.omterra.entity.component.WalkComponent;

/**
 *
 * @author Nathan Templon
 */
public final class Mappers {
    
    // Constants
    public static final ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
    public static final ComponentMapper<SizeComponent> size = ComponentMapper.getFor(SizeComponent.class);
    public static final ComponentMapper<TextureResourceComponent> texture = ComponentMapper.getFor(TextureResourceComponent.class);
    public static final ComponentMapper<WalkComponent> walk = ComponentMapper.getFor(WalkComponent.class);
    
    
    // Initialization
    private Mappers() {
        
    }
    
}
