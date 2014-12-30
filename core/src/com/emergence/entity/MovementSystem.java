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
package com.emergence.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.emergence.EmergenceGame;
import com.emergence.entity.component.PositionComponent;
import com.emergence.entity.component.RenderComponent;
import com.emergence.entity.component.MovementResourceComponent;
import com.emergence.entity.component.WalkComponent;
import com.emergence.entity.component.WalkComponent.WalkStates;
import com.emergence.entity.messaging.Message;
import com.emergence.entity.messaging.MessageListener;
import com.emergence.entity.messaging.MessageSystem;
import com.emergence.entity.messaging.WalkRequestMessage;
import com.emergence.entity.messaging.OffsetUpdatedMessage;
import com.emergence.entity.messaging.PositionChangedMessage;
import com.emergence.entity.messaging.SelfSubscribingListener;
import com.emergence.geometry.Size;
import com.emergence.world.Level;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Nathan Templon
 */
public class MovementSystem extends IteratingSystem implements MessageListener, SelfSubscribingListener {

    // Constants
    private static final float FIRST_WALK_SPRITE_BREAKPOINT = 0.3f;
    private static final float SECOND_WALK_SPRITE_BREAKPOINT = 0.7f;


    // Enumerations
    public enum MovementDirections {

        UP(0, 1),
        LEFT(-1, 0),
        DOWN(0, -1),
        RIGHT(1, 0);

        public static MovementDirections getSingleStepDirectionFor(int x, int y) {
            if (x == 0) {
                if (y > 0) {
                    return UP;
                }
                else if (y < 0) {
                    return DOWN;
                }
                else {
                    return null;
                }
            }
            else if (y == 0) {
                if (x > 0) {
                    return RIGHT;
                }
                else if (x < 0) {
                    return LEFT;
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        
        public final int deltaX;
        public final int deltaY;

        MovementDirections(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }
    }


    // Initialization
    public MovementSystem() {
        super(Families.positionables, EmergenceGame.MOVEMENT_SYSTEM_PRIORITY);
    }


    // Public Methods
    @Override
    public void handleMessage(Message message) {
        if (message instanceof WalkRequestMessage) {
            this.handleWalkRequest((WalkRequestMessage) message);
        }
    }

    @Override
    public void subscribe(Engine engine, MessageSystem system) {
        system.subscribe(this, WalkRequestMessage.class);
    }


    // Protected Methods
    @Override
    protected void processEntity(Entity entity, float deltaT) {
        if (Families.walkables.matches(entity)) {
            this.updateWalkSprite(entity, deltaT);
        }
    }


    // Private Methods
    private void moveEntityTo(Entity entity, Point location) {
        PositionComponent position = Mappers.position.get(entity);
        position.setTilePosition(location);
        EmergenceGame.game.getMessageSystem().publish(new PositionChangedMessage(entity, location));
    }

    private void handleWalkRequest(WalkRequestMessage message) {
        Entity entity = message.entity;
        MovementDirections direction = message.direction;
        if (Families.walkables.matches(entity)) {
            PositionComponent position = Mappers.position.get(entity);
            Point location = position.getTilePosition();
            Point requested = new Point(location.x + direction.deltaX, location.y + direction.deltaY);

            Level level = position.getLevel();
            Size size = Mappers.size.get(entity).getSize();
            Rectangle destination = new Rectangle(requested.x, requested.y, size.width, size.height);

            if (!level.collides(destination) && level.contains(destination)) {
                WalkComponent walk = Mappers.walk.get(entity);
                if (walk.getState().equals(WalkStates.STANDING)) {
                    walk.startWalking(direction, requested);
                    Mappers.moveTexture.get(entity).incrementWalkingTexture(direction);
                }
            }
            else {
                Mappers.render.get(entity).getSprite().setRegion(Mappers.moveTexture.get(entity).standingTextureFor(direction));
            }
        }
    }

    private void updateWalkPosition(Entity entity, WalkComponent walk) {
        float percentComplete = walk.getCompletionPercentage();
        int xOff = (int) (EmergenceGame.game.getCurrentLevel().getTileWidth() * percentComplete * walk.getWalkDirection().deltaX);
        int yOff = (int) (EmergenceGame.game.getCurrentLevel().getTileHeight() * percentComplete * walk.getWalkDirection().deltaY);
        Mappers.render.get(entity).setOffset(new Point(xOff, yOff));
        EmergenceGame.game.getMessageSystem().publish(new OffsetUpdatedMessage(entity));
    }

    private void updateWalkSprite(Entity entity, float deltaT) {
        WalkComponent walk = Mappers.walk.get(entity);
        walk.update(deltaT);

        float percentComplete = walk.getCompletionPercentage();
        RenderComponent render = Mappers.render.get(entity);
        Sprite sprite = render.getSprite();
        MovementResourceComponent textures = Mappers.moveTexture.get(entity);

        switch (walk.getState()) {
            case FINISHED_WALKING:
                walk.setState(WalkStates.STANDING);
                render.setOffset(new Point(0, 0));
                sprite.setRegion(textures.standingTextureFor(walk.getWalkDirection()));
                this.moveEntityTo(entity, walk.getWalkTarget());
                Mappers.position.get(entity).setFacingDirection(walk.getWalkDirection());
                break;
            case STANDING:
                break;
            case WALK_LEFT:
            case WALK_RIGHT:
            case WALK_UP:
            case WALK_DOWN:
                if (FIRST_WALK_SPRITE_BREAKPOINT < percentComplete && percentComplete < SECOND_WALK_SPRITE_BREAKPOINT) {
                    sprite.setRegion(textures.walkingTextureFont(walk.getWalkDirection()));
                }
                else {
                    sprite.setRegion(textures.standingTextureFor(walk.getWalkDirection()));
                }
                this.updateWalkPosition(entity, walk);
                break;
        }
    }

}
