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
package com.omterra.world;

import com.badlogic.gdx.utils.Disposable;
import com.omterra.OmterraGame;
import java.io.File;

/**
 * A class representing an individual level (map) in Legends of Omterra
 * @author Nathan Templon
 */
public class Level implements Disposable {
    
    // Constants
    public static final String LEVEL_FOLDER = "Levels";
    public static final String LEVEL_EXTENSION = "tmx";
    
    
    // Static Methods
    public static Level fromFile(File file) {
        Level level = new Level();
        
        if (OmterraGame.DEBUG) {
            System.out.println("Loading Level: " + file.getPath());
        }
        
        return level;
    }
    
    
    // Initialization
    public Level() {
        
    }
    
    
    // Disposable Imiplementation
    @Override
    public void dispose() {
        
    }
    
}
