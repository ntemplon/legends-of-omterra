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
package com.jupiter.europa.util;

/**
 *
 * @author Nathan Templon
 */
public class GameTimer {

    // Fields
    private boolean started = false;
    private long lastTickTime;

    private boolean paused;
    private long pauseStartTime;
    private long pauseTimeThisTick;


    // Properties
    public final boolean isStarted() {
        return this.started;
    }

    public final boolean isPaused() {
        return this.paused;
    }


    // Initialization
    public GameTimer() {

    }


    // Public Methods
    public void start() {
        this.started = true;
        this.lastTickTime = System.nanoTime();
        this.pauseTimeThisTick = 0;
    }

    public void pause() {
        if (!this.isPaused() && this.isStarted()) {
            this.paused = true;
            this.pauseStartTime = System.nanoTime();
        }
    }

    public void resume() {
        if (this.isPaused() && this.isStarted()) {
            this.paused = false;
            this.pauseTimeThisTick += System.nanoTime() - this.pauseStartTime;
        }
    }

    public float tick() {
        if (this.isPaused() || !this.isStarted()) {
            return 0.0f;
        }

        long currentTime = System.nanoTime();
        long elapsed = currentTime - this.lastTickTime - this.pauseTimeThisTick;
        this.pauseTimeThisTick = 0;
        this.lastTickTime = currentTime;

        return elapsed / 1e9f;
    }

}
