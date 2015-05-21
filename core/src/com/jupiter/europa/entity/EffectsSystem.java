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
package com.jupiter.europa.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.component.EffectsComponent;
import com.jupiter.europa.entity.messaging.*;
import com.jupiter.ganymede.event.Listener;

/**
 * @author Nathan Templon
 */
public class EffectsSystem extends IteratingSystem implements Listener<Message>, SelfSubscribingListener {

    // Initialization
    public EffectsSystem() {
        super(Families.affectables);
    }


    // Public Methods
    @Override
    public void processEntity(Entity entity, float deltaT) {
        Mappers.effects.get(entity).effects.stream()
                .forEach(effect -> effect.update(deltaT));
    }

    @Override
    public void handle(Message message) {
        if (message instanceof RequestEffectAddMessage) {
            this.onEffectAddedRequest((RequestEffectAddMessage) message);
        } else if (message instanceof RequestEffectRemoveMessage) {

        }
    }

    @Override
    public void subscribe(Engine engine, MessageSystem system) {
        system.subscribe(this, RequestEffectAddMessage.class, RequestEffectRemoveMessage.class);
    }


    // Private Methods
    private void onEffectAddedRequest(RequestEffectAddMessage message) {
        if (Families.affectables.matches(message.entity)) {
            Mappers.effects.get(message.entity).effects.add(message.effect);
            message.effect.onAdd(message.entity);
            EuropaGame.game.getMessageSystem().publish(new EffectAddedMessage(message.entity, message.effect));
        }
    }

    private void onEffectRemoveRequest(RequestEffectRemoveMessage message) {
        if (Families.affectables.matches(message.entity)) {
            EffectsComponent component = Mappers.effects.get(message.entity);
            if (component.effects.contains(message.effect)) {
                component.effects.remove(message.effect);
                message.effect.onRemove();
                EuropaGame.game.getMessageSystem().publish(new EffectRemovedMessage(message.entity, message.effect));
            }
        }
    }
}
