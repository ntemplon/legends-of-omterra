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

package com.jupiter.europa.util

/**

 * @author Nathan Templon
 */
public class GameTimer {

    // Fields
    private var started = false
    private var lastTickTime: Long = 0

    private var paused: Boolean = false
    private var pauseStartTime: Long = 0
    private var pauseTimeThisTick: Long = 0


    // Properties
    public fun isStarted(): Boolean {
        return this.started
    }

    public fun isPaused(): Boolean {
        return this.paused
    }


    // Public Methods
    public fun start() {
        this.started = true
        this.lastTickTime = System.nanoTime()
        this.pauseTimeThisTick = 0
    }

    public fun pause() {
        if (!this.isPaused() && this.isStarted()) {
            this.paused = true
            this.pauseStartTime = System.nanoTime()
        }
    }

    public fun resume() {
        if (this.isPaused() && this.isStarted()) {
            this.paused = false
            this.pauseTimeThisTick += System.nanoTime() - this.pauseStartTime
        }
    }

    public fun tick(): Float {
        if (this.isPaused() || !this.isStarted()) {
            return 0.0f
        }

        val currentTime = System.nanoTime()
        val elapsed = currentTime - this.lastTickTime - this.pauseTimeThisTick
        this.pauseTimeThisTick = 0
        this.lastTickTime = currentTime

        return elapsed / 1e9f
    }

}
