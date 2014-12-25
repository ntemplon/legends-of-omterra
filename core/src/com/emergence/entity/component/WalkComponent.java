/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emergence.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.emergence.entity.MovementSystem.MovementDirections;
import java.awt.Point;

/**
 *
 * @author Hortator
 */
public class WalkComponent extends Component implements Serializable {

    // Constants
    public static final float DEFAULT_WALK_SPEED = 4.0f;
    
    public static final String SPEED_KEY = "speed";
    

    // Enumerations
    public enum WalkStates implements State<WalkComponent> {

        STANDING() {
                    @Override
                    public void enter(WalkComponent e) {

                    }

                    @Override
                    public void update(WalkComponent e) {

                    }

                    @Override
                    public void exit(WalkComponent e) {

                    }

                    @Override
                    public boolean onMessage(WalkComponent e, Telegram tlgrm) {
                        return false;
                    }
                },
        WALK_UP() {
                    @Override
                    public void enter(WalkComponent e) {
                        this.elapsedTime = 0.0f;
                    }

                    @Override
                    public void update(WalkComponent e) {
                        if (elapsedTime > e.timePerStep) {
                            e.setState(FINISHED_WALKING);
                        }
                    }

                    @Override
                    public void exit(WalkComponent e) {

                    }

                    @Override
                    public boolean onMessage(WalkComponent e, Telegram tlgrm) {
                        return false;
                    }
                },
        WALK_DOWN() {
                    @Override
                    public void enter(WalkComponent e) {
                        this.elapsedTime = 0.0f;
                    }

                    @Override
                    public void update(WalkComponent e) {
                        if (elapsedTime > e.timePerStep) {
                            e.setState(FINISHED_WALKING);
                        }
                    }

                    @Override
                    public void exit(WalkComponent e) {

                    }

                    @Override
                    public boolean onMessage(WalkComponent e, Telegram tlgrm) {
                        return false;
                    }
                },
        WALK_LEFT() {
                    @Override
                    public void enter(WalkComponent e) {
                        this.elapsedTime = 0.0f;
                    }

                    @Override
                    public void update(WalkComponent e) {
                        if (elapsedTime > e.timePerStep) {
                            e.setState(FINISHED_WALKING);
                        }
                    }

                    @Override
                    public void exit(WalkComponent e) {

                    }

                    @Override
                    public boolean onMessage(WalkComponent e, Telegram tlgrm) {
                        return false;
                    }
                },
        WALK_RIGHT() {
                    @Override
                    public void enter(WalkComponent e) {
                        this.elapsedTime = 0.0f;
                    }

                    @Override
                    public void update(WalkComponent e) {
                        if (elapsedTime > e.timePerStep) {
                            e.setState(FINISHED_WALKING);
                        }
                    }

                    @Override
                    public void exit(WalkComponent e) {

                    }

                    @Override
                    public boolean onMessage(WalkComponent e, Telegram tlgrm) {
                        return false;
                    }
                },
        FINISHED_WALKING() {

                    @Override
                    public void enter(WalkComponent e) {
                        
                    }

                    @Override
                    public void update(WalkComponent e) {
                        
                    }

                    @Override
                    public void exit(WalkComponent e) {
                        
                    }

                    @Override
                    public boolean onMessage(WalkComponent e, Telegram tlgrm) {
                        return false;
                    }

                };

        protected float elapsedTime = 0.0f;

        WalkStates() {

        }

        public void updateTimeElapsed(float deltaT) {
            this.elapsedTime += deltaT;
        }
    }


    // Fields
    private final StateMachine<WalkComponent> stateMachine = new StackStateMachine<>(this);
    private float speed; // Speed, in steps per second
    private float timePerStep;
    private Point walkTarget;
    private MovementDirections walkDirection;


    // Properties
    public final float getSpeed() {
        return this.speed;
    }

    public final void setSpeed(float speed) {
        this.speed = speed;
        this.timePerStep = 1.0f / speed;
    }
    
    public final Point getWalkTarget() {
        return this.walkTarget;
    }
    
    public final float getElapsedTime() {
        return this.getState().elapsedTime;
    }
    
    public final float getTimePerStep() {
        return this.timePerStep;
    }
    
    public final MovementDirections getWalkDirection() {
        return this.walkDirection;
    }
    
    public final float getCompletionPercentage() {
        return this.getState().elapsedTime / this.getTimePerStep();
    }


    // Initialization
    public WalkComponent() {
        this(DEFAULT_WALK_SPEED);
    }

    public WalkComponent(float speed) {
        this.setSpeed(speed);
        this.stateMachine.changeState(WalkStates.STANDING);
    }


    // Public Methods
    public void update(float deltaT) {
        State<WalkComponent> state = this.stateMachine.getCurrentState();
        if (state instanceof WalkStates) {
            ((WalkStates) state).updateTimeElapsed(deltaT);
        }
        this.stateMachine.update();
    }

    public void setState(WalkStates state) {
        this.stateMachine.changeState(state);
    }

    public WalkStates getState() {
        return (WalkStates) this.stateMachine.getCurrentState();
    }

    public void startWalking(MovementDirections direction, Point walkTarget) {
        switch (direction) {
            case UP:
                this.setState(WalkStates.WALK_UP);
                break;
            case DOWN:
                this.setState(WalkStates.WALK_DOWN);
                break;
            case RIGHT:
                this.setState(WalkStates.WALK_RIGHT);
                break;
            case LEFT:
                this.setState(WalkStates.WALK_LEFT);
                break;
        }
        
        this.walkDirection = direction;
        this.walkTarget = walkTarget;
    }
    
    
    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(SPEED_KEY, this.getSpeed());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.setSpeed(jsonData.getFloat(SPEED_KEY));
    }

}
