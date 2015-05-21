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
package com.jupiter.europa.world;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A world, or group of self-contained levels
 *
 * @author Nathan Templon
 */
public class World implements Disposable {

    // Constants
    public static final String WORLD_FILE_NAME = "world.json";
    public static final String NAME_KEY = "name";
    public static final String STARTING_LEVEL_KEY = "starting-level";

    public static final String DEFAULT_WORLD_NAME = "default";
    public static final String DEFAULT_STARTING_LEVEL = "Start";


    // Static Methods
    public static World fromDirectory(Path dir) {
        World world = new World();

        // Basic gist: If we are working with a directory, see if it has a subdirectory with levels in it.  If so,
        //  go through each file in that subdirectory and load a level from it, if possible
        if (Files.isDirectory(dir)) {
            Path levelDir = dir.resolve(Level.LEVEL_FOLDER);

            if (Files.exists(levelDir) && Files.isDirectory(levelDir)) {
                // Set the world's properties
                Path configFile = dir.resolve(WORLD_FILE_NAME);
                JsonReader reader = new JsonReader();
                String contents;
                try {
                    contents = new String(Files.readAllBytes(configFile), StandardCharsets.UTF_8);
                    JsonValue value = reader.parse(contents);

                    // Name
                    if (value.has(NAME_KEY)) {
                        world.name = value.getString(NAME_KEY);
                    } else {
                        world.name = DEFAULT_WORLD_NAME;
                    }

                    // Starting Level
                    if (value.has(STARTING_LEVEL_KEY)) {
                        world.startingLevelName = value.getString(STARTING_LEVEL_KEY);
                    } else {
                        world.startingLevelName = DEFAULT_STARTING_LEVEL;
                    }
                } catch (Exception ex) {
                    // Set default properties
                    world.name = DEFAULT_WORLD_NAME;
                    world.startingLevelName = DEFAULT_STARTING_LEVEL;
                }

                // Loop through all levels in the directory
                try (DirectoryStream<Path> paths = Files.newDirectoryStream(levelDir)) {
                    paths.iterator().forEachRemaining((Path file) -> {
                        if (FileUtils.getExtension(file).equalsIgnoreCase(Level.LEVEL_EXTENSION)) {
                            TiledMap map = EuropaGame.game.getAssetManager().get(file.toString(), TiledMap.class);
                            String name = file.getFileName().toString().replace("." + Level.LEVEL_EXTENSION, "");
                            world.addLevel(new Level(name, map, world));
                        }
                    });
                } catch (IOException ex) {

                }
            }
        }

        return world;
    }


    // Fields
    private final Map<String, Level> levels;
    private String name;
    private String startingLevelName;


    // Properties
    public Set<String> getLevelNames() {
        return this.levels.keySet();
    }

    public Level getLevel(String name) {
        return this.levels.get(name);
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

    public String getName() {
        return this.name;
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
        this.levels.keySet().stream().forEach((String key) -> {
            this.levels.get(key).dispose();
        });
    }
}
