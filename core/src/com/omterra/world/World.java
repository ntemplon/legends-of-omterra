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
import com.omterra.io.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A world, or group of self-contained levels
 *
 * @author Nathan Templon
 */
public class World implements Disposable {

    // Static Methods
    public static World fromDirectory(File dir) {
        World world = new World();

        if (OmterraGame.DEBUG) {
            System.out.println("Loading World: " + dir.getPath());
        }

        // Basic gist: If we are working with a directory, see if it has a subdirectory with levels in it.  If so,
        //  go through each file in that subdirectory and load a level from it, if possible
        if (dir.isDirectory()) {
            File levelDir = new File(FileUtils.combine(dir.getPath(), Level.LEVEL_FOLDER));

            if (levelDir.exists() && levelDir.isDirectory()) {
                for (File file : levelDir.listFiles()) {
                    if (FileUtils.getExtension(file).equalsIgnoreCase(Level.LEVEL_EXTENSION)) {
                        world.addLevel(Level.fromFile(file));
                    }
                }
            }
        }

        return world;
    }


    // Fields
    private final List<Level> levels;


    // Initialization
    public World() {
        levels = new ArrayList<>();
    }


    // Public Methods
    public void addLevel(Level level) {
        this.levels.add(level);
    }


    // Disposable Implementation
    @Override
    public void dispose() {
        if (this.levels != null) {
            for (Level level : levels) {
                level.dispose();
            }
        }
    }

}
