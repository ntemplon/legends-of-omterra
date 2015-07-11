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

package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.fsm.StackStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.MovementSystem
import com.jupiter.europa.entity.MovementSystem.MovementDirections
import java.awt.Point

/**

 * @author Hortator
 */
public class WalkComponent @jvmOverloads constructor(speed: Float = WalkComponent.DEFAULT_WALK_SPEED) : Component(), Serializable {


    // Enumerations
    public enum class WalkStates : State<WalkComponent> {

        STANDING {
            override fun enter(e: WalkComponent) {
            }

            override fun update(e: WalkComponent) {
            }

            override fun exit(e: WalkComponent) {
            }

            override fun onMessage(e: WalkComponent, tlgrm: Telegram): Boolean {
                return false
            }
        },
        WALK_UP {
            override fun enter(e: WalkComponent) {
                this.elapsedTime = 0.0f
            }

            override fun update(e: WalkComponent) {
                if (elapsedTime > e.timePerStep) {
                    e.setState(FINISHED_WALKING)
                }
            }

            override fun exit(e: WalkComponent) {
            }

            override fun onMessage(e: WalkComponent, tlgrm: Telegram): Boolean {
                return false
            }
        },
        WALK_DOWN {
            override fun enter(e: WalkComponent) {
                this.elapsedTime = 0.0f
            }

            override fun update(e: WalkComponent) {
                if (elapsedTime > e.timePerStep) {
                    e.setState(FINISHED_WALKING)
                }
            }

            override fun exit(e: WalkComponent) {
            }

            override fun onMessage(e: WalkComponent, tlgrm: Telegram): Boolean {
                return false
            }
        },
        WALK_LEFT {
            override fun enter(e: WalkComponent) {
                this.elapsedTime = 0.0f
            }

            override fun update(e: WalkComponent) {
                if (elapsedTime > e.timePerStep) {
                    e.setState(FINISHED_WALKING)
                }
            }

            override fun exit(e: WalkComponent) {
            }

            override fun onMessage(e: WalkComponent, tlgrm: Telegram): Boolean {
                return false
            }
        },
        WALK_RIGHT {
            override fun enter(e: WalkComponent) {
                this.elapsedTime = 0.0f
            }

            override fun update(e: WalkComponent) {
                if (elapsedTime > e.timePerStep) {
                    e.setState(FINISHED_WALKING)
                }
            }

            override fun exit(e: WalkComponent) {
            }

            override fun onMessage(e: WalkComponent, tlgrm: Telegram): Boolean {
                return false
            }
        },
        FINISHED_WALKING {

            override fun enter(e: WalkComponent) {
            }

            override fun update(e: WalkComponent) {
            }

            override fun exit(e: WalkComponent) {
            }

            override fun onMessage(e: WalkComponent, tlgrm: Telegram): Boolean {
                return false
            }

        };

        var elapsedTime: Float = 0.0f

        public fun updateTimeElapsed(deltaT: Float) {
            this.elapsedTime += deltaT
        }
    }


    // Properties
    private val stateMachine = StackStateMachine(this)
    public var speed: Float = speed
        private set(value) {
            this.$speed = value
            this.timePerStep = 1.0f / value
        }
    public var timePerStep: Float = 1.0f / this.speed
        private set
    public var walkTarget: Point = Point()
        private set
    public var walkDirection: MovementDirections = MovementDirections.UP
        private set

    public fun getElapsedTime(): Float {
        return this.getState().elapsedTime
    }

    public fun getCompletionPercentage(): Float {
        return this.getState().elapsedTime / this.timePerStep
    }

    init {
        this.stateMachine.changeState(WalkStates.STANDING)
    }


    // Public Methods
    public fun update(deltaT: Float) {
        val state = this.stateMachine.getCurrentState()
        if (state is WalkStates) {
            state.updateTimeElapsed(deltaT)
        }
        this.stateMachine.update()
    }

    public fun setState(state: WalkStates) {
        this.stateMachine.changeState(state)
    }

    public fun getState(): WalkStates {
        return this.stateMachine.getCurrentState() as WalkStates
    }

    public fun startWalking(direction: MovementDirections, walkTarget: Point) {
        when (direction) {
            MovementSystem.MovementDirections.UP -> this.setState(WalkStates.WALK_UP)
            MovementSystem.MovementDirections.DOWN -> this.setState(WalkStates.WALK_DOWN)
            MovementSystem.MovementDirections.RIGHT -> this.setState(WalkStates.WALK_RIGHT)
            MovementSystem.MovementDirections.LEFT -> this.setState(WalkStates.WALK_LEFT)
        }

        this.walkDirection = direction
        this.walkTarget = walkTarget
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(SPEED_KEY, this.speed)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        this.speed = jsonData.getFloat(SPEED_KEY)
    }

    companion object {

        // Constants
        public val DEFAULT_WALK_SPEED: Float = 5.0f
        public val SPEED_KEY: String = "speed"
    }

}
