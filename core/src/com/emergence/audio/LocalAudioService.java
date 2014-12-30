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
package com.emergence.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.emergence.io.FileLocations;
import com.emergence.io.FileUtils;
import com.emergence.util.ArrayUtils;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public class LocalAudioService implements AudioService {
    
    // Constants
    private final float MASTER_VOLUME_ADJUSTMENT = 1f;
    
    
    // Fields
    private final Map<String, Music[]> musicSets;
    private final Random random = new Random(System.currentTimeMillis());
    
    private Music[] currentSet;
    private Music currentTrack;
    private int currentTrackIndex;
    private float musicVolume = 1.0f;
            

    public LocalAudioService() {
        this.musicSets = new HashMap<>();
        
        for(String type : AudioService.MUSIC_TYPES) {
            File typeFolder = new File(FileLocations.AUDIO_DIRECTORY, type);
            if (typeFolder.exists() && typeFolder.isDirectory()) {
                Set<Music> music = new HashSet<>();
                
                for(File musicFile : typeFolder.listFiles()) {
                    if (ArrayUtils.contains(MUSIC_EXTENSIONS, FileUtils.getExtension(musicFile))) {
                        Music instance = Gdx.audio.newMusic(new FileHandle(musicFile));
                        instance.setOnCompletionListener((Music music1) -> {
                            LocalAudioService.this.trackCompleted(music1);
                        });
                        music.add(instance);
                    }
                }
                
                this.musicSets.put(type, music.toArray(new Music[music.size()]));
            }
            else {
                this.musicSets.put(type, new Music[0]);
            }
        }
    }
    
    
    // Public Methods
    @Override
    public void playMusic(String type) {
        this.stop();
        
        if (this.musicSets.containsKey(type)) {
            this.currentSet = this.musicSets.get(type);
            
            if (this.currentSet.length == 0) {
                this.currentTrack = null;
                this.currentTrackIndex = 0;
            }
            else {
                this.currentTrackIndex = random.nextInt(this.currentSet.length);
                this.currentTrack = this.currentSet[this.currentTrackIndex];
                this.currentTrack.setVolume(this.musicVolume);
                this.currentTrack.play();
            }
        }
    }
    
    @Override
    public void pause() {
        if (this.currentTrack != null) {
            this.currentTrack.pause();
        }
    }
    
    @Override
    public void resume() {
        if (this.currentTrack != null) {
            this.currentTrack.play();
        }
    }
    
    @Override
    public void stop() {
        if (this.currentTrack != null) {
            this.currentTrack.stop();
        }
        
        this.currentTrack = null;
        this.currentSet = null;
        this.currentTrackIndex = -1;
    }
    
    @Override
    public void setMusicVolume(float volume) {
        this.musicVolume = volume * MASTER_VOLUME_ADJUSTMENT;
        this.currentTrack.setVolume(this.musicVolume);
    }
    
    @Override
    public void dispose() {
        this.musicSets.keySet().stream().forEach((key) -> {
            for (Music music : this.musicSets.get(key)) {
                music.dispose();
            }
        });
    }
    
    
    // Private Methods
    private void trackCompleted(Music music) {
        if (this.currentSet.length == 1) {
            this.currentTrackIndex = 0;
            this.currentTrack = this.currentSet[0];
            this.currentTrack.setVolume(this.musicVolume);
            this.currentTrack.play();
        }
        else {
            int nextTrackIndex = this.random.nextInt(this.currentSet.length);
            while (nextTrackIndex == this.currentTrackIndex) {
                nextTrackIndex = this.random.nextInt(this.currentSet.length);
            }
            this.currentTrackIndex = nextTrackIndex;
            this.currentTrack = this.currentSet[this.currentTrackIndex];
            this.currentTrack.setVolume(this.musicVolume);
            this.currentTrack.play();
        }
    }
    
}
