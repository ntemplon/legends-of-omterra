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

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.omterra.io.FileUtils;
import com.omterra.io.OmterraAssetManager;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A world, or group of self-contained levels
 *
 * @author Nathan Templon
 */
public class World implements Disposable {

    // Static Methods
    public static World fromDirectory(File dir, OmterraAssetManager assets) {
        World world = new World();

        // Basic gist: If we are working with a directory, see if it has a subdirectory with levels in it.  If so,
        //  go through each file in that subdirectory and load a level from it, if possible
        if (dir.isDirectory()) {
            File levelDir = new File(FileUtils.combine(dir.getPath(), Level.LEVEL_FOLDER));

            if (levelDir.exists() && levelDir.isDirectory()) {
                for (File file : levelDir.listFiles()) {
                    if (FileUtils.getExtension(file).equalsIgnoreCase(Level.LEVEL_EXTENSION)) {
                        TiledMap map = assets.get(file.getPath(), TiledMap.class);
                        String name = file.getName().replace("." + Level.LEVEL_EXTENSION, "");
                        world.addLevel(new Level(name, map));
                    }
                }
            }
        }

        return world;
    }


    // Fields
    private final Map<String, Level> levels;


    // Properties
    public Collection<String> getLevelNames() {
        return this.levels.keySet();
    }

    /**
     * Gets the starting level for a new game for this world
     *
     * @return
     */
    public Level getStartingLevel() {
        if (this.levels.containsKey("TestLevel")) {
            return this.levels.get("TestLevel");
        }
        return null;
    }


    // Initialization
    public World() {
        levels = new HashMap<>();
    }


    // Public Methods
    public void addLevel(Level level) {
        this.levels.put(level.getName(), level);
    }


    // Disposable Implementation
    @Override
    public void dispose() {
        // Free all native resources
        if (this.levels != null) {
            for (String key : this.levels.keySet()) {
                this.levels.get(key).dispose();
            }
        }
    }

}
