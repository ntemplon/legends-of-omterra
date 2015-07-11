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

package com.jupiter.europa.entity

import com.badlogic.ashley.core.ComponentMapper
import com.jupiter.europa.entity.component.*

/**

 * @author Nathan Templon
 */
public object Mappers {

    // Constants
    public val abilities: ComponentMapper<AbilityComponent> = ComponentMapper.getFor(javaClass<AbilityComponent>())
    public val attributes: ComponentMapper<AttributesComponent> = ComponentMapper.getFor(javaClass<AttributesComponent>())
    public val characterClass: ComponentMapper<CharacterClassComponent> = ComponentMapper.getFor(javaClass<CharacterClassComponent>())
    public val collision: ComponentMapper<CollisionComponent> = ComponentMapper.getFor(javaClass<CollisionComponent>())
    public val effects: ComponentMapper<EffectsComponent> = ComponentMapper.getFor(javaClass<EffectsComponent>())
    public val moveTexture: ComponentMapper<MovementResourceComponent> = ComponentMapper.getFor(javaClass<MovementResourceComponent>())
    public val name: ComponentMapper<NameComponent> = ComponentMapper.getFor(javaClass<NameComponent>())
    public val position: ComponentMapper<PositionComponent> = ComponentMapper.getFor(javaClass<PositionComponent>())
    public val race: ComponentMapper<RaceComponent> = ComponentMapper.getFor(javaClass<RaceComponent>())
    public val render: ComponentMapper<RenderComponent> = ComponentMapper.getFor(javaClass<RenderComponent>())
    public val resources: ComponentMapper<ResourceComponent> = ComponentMapper.getFor(javaClass<ResourceComponent>())
    public val size: ComponentMapper<SizeComponent> = ComponentMapper.getFor(javaClass<SizeComponent>())
    public val skills: ComponentMapper<SkillsComponent> = ComponentMapper.getFor(javaClass<SkillsComponent>())
    public val walk: ComponentMapper<WalkComponent> = ComponentMapper.getFor(javaClass<WalkComponent>())

}
