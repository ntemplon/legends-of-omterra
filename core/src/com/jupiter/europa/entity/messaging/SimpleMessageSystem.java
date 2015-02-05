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
package com.jupiter.europa.entity.messaging;

import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple messaging system, with subscription based on class
 *
 * @author Nathan Templon
 */
public class SimpleMessageSystem implements MessageSystem {

    // Fields
    private final List<Message> queue;
    private final List<Message> publishedWhileUpdating;
    private final Map<Class<? extends Message>, Event<Message>> messageEvents = new HashMap<>();
    private final Event<Message> globalMessageEvent = new Event<>();

    private final Lock queueLock;
    private final Lock publishedWhileUpdatingLock;
    private final Lock listenersLock;
    private final Lock globalListenersLock;
    private volatile boolean isUpdating = false;

    private Thread updateThread;
    private Thread publishThread;


    // Initialization
    public SimpleMessageSystem() {
        super();
        this.queue = new ArrayList<>();
        this.publishedWhileUpdating = new ArrayList<>();

        this.queueLock = new ReentrantLock();
        this.publishedWhileUpdatingLock = new ReentrantLock();
        this.listenersLock = new ReentrantLock();
        this.globalListenersLock = new ReentrantLock();
    }


    // Public Methods
    /**
     * Sends all queued messages to all subscribed entities. If the previous call to update() has not yet completed,
     * this method will block until it has
     * @param blocking
     */
    @Override
    public void update(boolean blocking) {
        if (this.updateThread != null) {
            try {
                this.updateThread.join();
            }
            catch (InterruptedException ex) {

            }
        }

        if (!blocking) {
            this.updateThread = new Thread(() -> this.updateInternal(), "Message System Update Thread");
            this.updateThread.start();
        }
        else {
            this.updateInternal();
        }
    }

    /**
     * Subscribes the listener to messages of each of the following classes.
     *
     * @param listener
     * @param messageTypes
     */
    @Override
    public void subscribe(Listener<Message> listener, Class<? extends Message>... messageTypes) {
        this.listenersLock.lock();

        try {
            for (Class<? extends Message> type : messageTypes) {
                if (this.messageEvents.containsKey(type)) {
                    this.messageEvents.get(type).addListener(listener);
                }
                else {
                    Event<Message> newEvent = new Event<>();
                    newEvent.addListener(listener);
                    this.messageEvents.put(type, newEvent);
                }
            }
        }
        finally {
            this.listenersLock.unlock();
        }
    }

    /**
     * Subscribes the listener to all messages
     *
     * @param listener
     */
    @Override
    public void subscribe(Listener<Message> listener) {
        this.globalListenersLock.lock();

        try {
            this.globalMessageEvent.addListener(listener);
        }
        finally {
            this.globalListenersLock.unlock();
        }
    }

    /**
     * Unsubscribes the listener from receiving messages of the provided types. If the listener was subscribed to
     * receive messages of all types, it will continue to do so.
     *
     * @param listener
     * @param messageTypes
     */
    @Override
    public void unsubscribe(Listener<Message> listener, Class<? extends Message>... messageTypes) {
        this.listenersLock.lock();

        try {
            for (Class<? extends Message> type : messageTypes) {
                if (this.messageEvents.containsKey(type)) {
                    this.messageEvents.get(type).removeListener(listener);
                }
            }
        }
        finally {
            this.listenersLock.unlock();
        }
    }

    /**
     * Unsubscribes the listener from all messages that it is currently subscribed to
     *
     * @param listener
     */
    @Override
    public void unsubscribe(Listener<Message> listener) {
        this.globalListenersLock.lock();
        this.listenersLock.lock();

        try {
            this.globalMessageEvent.removeListener(listener);

            this.messageEvents.keySet().stream().forEach((Class<? extends Message> type) -> {
                this.messageEvents.get(type).removeListener(listener);
            });
        }
        finally {
            this.globalListenersLock.unlock();
            this.listenersLock.unlock();
        }
    }

    /**
     * Pushes the provided message onto the queue, to be handled by the next update() command
     *
     * @param message
     */
    @Override
    public void publish(Message message) {
        if (this.publishThread != null) {
            try {
                this.publishThread.join();
            }
            catch (InterruptedException ex) {

            }
        }

        this.publishThread = new Thread(() -> this.publishInternal(message), "Message System Publish Thread");
        this.publishThread.start();
    }

    public void joinUpdateThread() {
        if (this.updateThread != null) {
            try {
                this.updateThread.join();
            }
            catch (InterruptedException ex) {

            }
        }
    }


    // Private Methods
    private void updateInternal() {
        this.queueLock.lock();
        this.globalListenersLock.lock();
        this.listenersLock.lock();
        try {
            while (this.queue.size() > 0) {
                this.isUpdating = true;
                this.queue.stream().forEach((Message message) -> {
                    // Send the message to global listeners
                    this.globalMessageEvent.dispatch(message);

                    // Send the message to specifically subscribed listeners
                    Class<? extends Message> type = message.getClass();
                    if (this.messageEvents.containsKey(type)) {
                        this.messageEvents.get(type).dispatch(message);
                    }
                });

                // Allows systems to have conversations
                this.publishedWhileUpdatingLock.lock();
                try {
                    this.queue.clear();
                    this.queue.addAll(this.publishedWhileUpdating);
                    this.publishedWhileUpdating.clear();
                }
                finally {
                    this.publishedWhileUpdatingLock.unlock();
                }
            }
        }
        finally {
            this.isUpdating = false;
            this.queue.clear();
            this.queueLock.unlock();
            this.globalListenersLock.unlock();
            this.listenersLock.unlock();
        }
    }

    private void publishInternal(Message message) {
        if (this.isUpdating) {
            this.publishedWhileUpdatingLock.lock();

            try {
                this.publishedWhileUpdating.add(message);
            }
            finally {
                this.publishedWhileUpdatingLock.unlock();
            }
        }
        else {
            this.queueLock.lock();

            try {
                this.queue.add(message);
            }
            finally {
                this.queueLock.unlock();
            }
        }
    }

}
