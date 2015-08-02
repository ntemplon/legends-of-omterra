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

package com.jupiter.europa.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.MovementSystem
import com.jupiter.europa.entity.component.WalkComponent
import com.jupiter.europa.entity.messaging.ActionCompletedMessage
import com.jupiter.europa.entity.messaging.InputRequestMessage
import com.jupiter.europa.entity.messaging.Message
import com.jupiter.europa.screen.overlay.TileSelectionOverlay
import com.jupiter.europa.world.Level
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.Listener
import java.awt.Point
import java.util.Collections
import java.util.HashMap
import java.util.HashSet

/**
 * Created by nathan on 7/16/15.
 */
public class LevelInputProcessor(private val screen: LevelScreen) : InputProcessor, Listener<Message> {

    // Enumerations
    private enum class InputStates : State<LevelInputProcessor>, InputProcessor {
        NONE {

        },
        PLAYER {
            override fun keyDown(i: Int): Boolean {
                if (POLLED_INPUTS.contains(i)) {
                    this.processor?.keysDownInternal?.add(i)
                    return true
                } else if (i == this.processor?.screen?.pauseKey) {
                    if (!EuropaGame.game.isSuspended()) {
                        this.processor?.screen?.displayPauseMenu()
                    }
                }
                return false
            }

            override fun keyUp(i: Int): Boolean {
                return this.processor?.keysDownInternal?.remove(i) ?: false
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (button != Input.Buttons.LEFT || !(this.processor?.clickLegal() ?: false)) {
                    return false
                }

                val entity = this.processor?.screen?.level?.entityLayer?.entityAt(this.processor?.screen?.tileUnder(screenX, screenY) ?: return false) ?: return false

                this.processor?.entityClicked?.dispatch(entity)
                return true
            }
        },
        MENU {
            override fun enter(processor: LevelInputProcessor) {
                super.enter(processor)
                processor.keysDownInternal.clear()
            }
        },
        SELECTING {
            private var selecting: Boolean = false

            override fun enter(processor: LevelInputProcessor) {
                super.enter(processor)
                processor.screen.addOverlay(processor.selectionOverlay)
                this.selecting = false
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (!this.selecting || button != Input.Buttons.LEFT || !(this.processor?.clickLegal() ?: false)) {
                    return false
                }

                val point = this.processor?.screen?.tileUnder(screenX, screenY) ?: return false
                if (this.processor?.selectionOverlay?.approval?.invoke(this.processor?.screen?.level ?: return false, point) ?: false) {
                    this.processor?.onComplete?.invoke(point)
                    return true
                }
                return false
            }

            override fun update(processor: LevelInputProcessor) {
                if (!this.selecting && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    this.selecting = true
                }
            }

            override fun exit(processor: LevelInputProcessor) {
                super.exit(processor)
                processor.screen.removeOverlay(processor.selectionOverlay)
            }
        };

        // Properties
        protected var processor: LevelInputProcessor? = null


        // State Implementation
        override fun enter(processor: LevelInputProcessor) {
            this.processor = processor
        }

        override fun exit(processor: LevelInputProcessor) {
            this.processor = null
        }

        override fun update(processor: LevelInputProcessor) {

        }

        override fun onMessage(processor: LevelInputProcessor, message: Telegram): Boolean {
            return false
        }


        // Public Methods
        override fun keyDown(i: Int): Boolean {
            return false
        }


        override fun keyUp(i: Int): Boolean {
            return false
        }

        override fun keyTyped(c: Char): Boolean {
            return false
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            return false
        }

        override fun touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
            return false
        }

        override fun touchDragged(i: Int, i1: Int, i2: Int): Boolean {
            return false
        }

        override fun mouseMoved(x: Int, y: Int): Boolean {
            return false
        }

        override fun scrolled(i: Int): Boolean {
            return false
        }
    }


    // Properties
    private val stateMachine: StateMachine<LevelInputProcessor> = DefaultStateMachine(this)
    private val keysDownInternal = HashSet<Int>()

    private var selectionOverlay: TileSelectionOverlay = TileSelectionOverlay()
    private var onComplete: (Point) -> Unit = {}

    protected val entityClicked: Event<Entity> = Event()

    public val keysDown: Set<Int> = Collections.unmodifiableSet(this.keysDownInternal)

    public var state: InputStates = InputStates.NONE
        get() = this.$state
        set(state) {
            this.$state = state
            this.stateMachine.changeState(state)
        }


    // Initialization
    init {
        val initialState = InputStates.PLAYER
        this.stateMachine.setInitialState(initialState)
        this.state = initialState

        EuropaGame.game.messageSystem.subscribe(this, javaClass<ActionCompletedMessage>(), javaClass<InputRequestMessage>())
    }


    // Public
    public fun controlPlayer() {
        this.state = InputStates.PLAYER
    }

    public fun enterMenuState() {
        this.state = InputStates.MENU
    }

    public fun beginSelection(filter: (Level, Point) -> Boolean, onComplete: (Point) -> Unit) {
        this.onComplete = onComplete
        this.selectionOverlay = TileSelectionOverlay(filter)
        this.state = InputStates.SELECTING
    }

    public fun addEntityListener(listener: (Entity) -> Unit): Boolean = this.entityClicked.addListener(listener)

    public fun update() {
        this.stateMachine.update()
    }

    override fun handle(message: Message) {
        if (message is ActionCompletedMessage && this.screen.level.controlledEntity === message.entity) {
            this.controlPlayer()
        } else if (message is InputRequestMessage) {
            when (message.request) {
                InputRequestMessage.InputRequests.NONE -> this.state = InputStates.NONE
            }
        }
    }

    public fun close() {
        EuropaGame.game.messageSystem.unsubscribe(this, javaClass<ActionCompletedMessage>(), javaClass<InputRequestMessage>())
    }


    // InputProcessor Implementation
    override fun keyDown(i: Int): Boolean {
        return this.state.keyDown(i)
    }

    override fun keyUp(i: Int): Boolean {
        return this.state.keyUp(i)
    }

    override fun keyTyped(c: Char): Boolean {
        return this.state.keyTyped(c)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return this.state.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return this.state.touchUp(i, i1, i2, i3)
    }

    override fun touchDragged(i: Int, i1: Int, i2: Int): Boolean {
        return this.state.touchDragged(i, i1, i2)
    }

    override fun mouseMoved(x: Int, y: Int): Boolean {
        return this.state.mouseMoved(x, y)
    }

    override fun scrolled(i: Int): Boolean {
        return this.state.scrolled(i)
    }


    // Private Methods
    private fun clickLegal(): Boolean {
        // Avoids shennanigans with the character moving after the menu appears
        if (!this.keysDownInternal.isEmpty()) {
            return false
        }

        val entity = this.screen.level.controlledEntity
        if (entity != null) {
            if (Families.walkables.matches(entity)) {
                val walk = Mappers.walk.get(entity)
                if (walk.getState() != WalkComponent.WalkStates.STANDING) {
                    return false
                }
            }
        }
        return true
    }


    // Static
    companion object {
        public val POLLED_INPUTS: Set<Int> = getUsedInputs()
        public val MOVEMENT_MAP: Map<Int, MovementSystem.MovementDirections> = getMovementKeys()


        private fun getUsedInputs(): Set<Int> {
            val keys = HashSet<Int>()

            keys.add(Input.Keys.W)
            keys.add(Input.Keys.A)
            keys.add(Input.Keys.S)
            keys.add(Input.Keys.D)

            return keys
        }

        private fun getMovementKeys(): Map<Int, MovementSystem.MovementDirections> {
            val keys = HashMap<Int, MovementSystem.MovementDirections>()

            keys.put(Input.Keys.W, MovementSystem.MovementDirections.UP)
            keys.put(Input.Keys.A, MovementSystem.MovementDirections.LEFT)
            keys.put(Input.Keys.S, MovementSystem.MovementDirections.DOWN)
            keys.put(Input.Keys.D, MovementSystem.MovementDirections.RIGHT)

            return keys
        }
    }
}