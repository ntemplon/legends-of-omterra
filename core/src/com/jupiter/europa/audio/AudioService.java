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
package com.jupiter.europa.audio;

import com.badlogic.gdx.utils.Disposable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.settings.Settings;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Nathan Templon
 */
public abstract class AudioService implements Disposable {
    
    public static final String TITLE_MUSIC = "title";
    public static final String COMBAT_MUSIC = "combat";
    public static final String DUNGEON_MUSIC = "dungeon";
    public static final String AMBIENT_MUSIC = "ambient";
    public static final String DEFAULT_MUSIC = AMBIENT_MUSIC;
    
    public static final String[] MUSIC_TYPES = {TITLE_MUSIC, COMBAT_MUSIC, DUNGEON_MUSIC, AMBIENT_MUSIC};
    public static final String[] MUSIC_EXTENSIONS = {"mp3", "wav"};
    
    // Abstract Methods
    public abstract void playMusic(String type);
    public abstract void setMusicVolume(float volume);
    public abstract void pause();
    public abstract void resume();
    public abstract void stop();
    
}
