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

package com.jupiter.europa.entity.ability

import com.badlogic.ashley.core.Entity
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.messaging.ActionCompletedMessage
import com.jupiter.europa.world.Level
import java.awt.Point

/**
 * Created by nathan on 5/20/15.
 */
public open class Action(public val entity: Entity) {

    public open val targets: List<(Level, Point) -> Boolean> = listOf()
    public open fun apply(targets: List<Point>) {
        this.complete()
    }

    protected fun complete() {
        EuropaGame.game.messageSystem.publish(ActionCompletedMessage(this.entity, this))
    }
}
