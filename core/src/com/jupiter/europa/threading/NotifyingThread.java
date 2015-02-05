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
package com.jupiter.europa.threading;

import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

/**
 * A thread which runs, then notifies any number of listeners of completion
 * @author Nathan Templon
 */
public class NotifyingThread extends Thread {
    
    // Fields
    private final Event<ThreadCompleteArgs> completed = new Event<>();
    
    
    // Initialization
    public NotifyingThread(Runnable runnable) {
        super(runnable);
    }
    
    
    // Public Methods   
    @Override
    public final void run() {
        try {
            super.run();
        }
        finally {
            this.completed.dispatch(new ThreadCompleteArgs(this));
        }
    }
    
    public boolean addThreadCompleteListener(Listener<ThreadCompleteArgs> listener) {
        return this.completed.addListener(listener);
    }
    
    public boolean removeThreadCompleteListener(Listener<ThreadCompleteArgs> listener) {
        return this.completed.removeListener(listener);
    }
    
    
    // Neseted Classes
    public static class ThreadCompleteArgs {
        
        // Fields
        public final NotifyingThread thread;
        
        
        // Initialization
        public ThreadCompleteArgs(NotifyingThread thread) {
            this.thread = thread;
        }
        
    }
}
