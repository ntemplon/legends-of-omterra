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

package com.jupiter.europa.world

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.JsonReader
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.io.FileUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.HashMap

/**
 * A world, or group of self-contained levels

 * @author Nathan Templon
 */
public class World : Disposable {


    // Fields
    private val levels: MutableMap<String, Level>
    public var name: String? = null
        private set
    private var startingLevelName: String? = null


    // Properties
    public fun getLevelNames(): Set<String> {
        return this.levels.keySet()
    }

    public fun getLevel(name: String): Level {
        return this.levels.get(name)!!
    }

    /**
     * Gets the starting level for a new game for this world

     * @return
     */
    public fun getStartingLevel(): Level? {
        if (this.levels.containsKey("TestLevel")) {
            return this.levels.get("TestLevel")
        }
        return null
    }


    init {
        levels = HashMap<String, Level>()
    }


    // Public Methods
    public fun addLevel(level: Level) {
        this.levels.put(level.name, level)
    }


    // Disposable Implementation
    override fun dispose() {
        // Free all native resources
        for (level in levels.values()) {
            level.dispose()
        }
    }

    companion object {

        // Constants
        public val WORLD_FILE_NAME: String = "world.json"
        public val NAME_KEY: String = "name"
        public val STARTING_LEVEL_KEY: String = "starting-level"

        public val DEFAULT_WORLD_NAME: String = "default"
        public val DEFAULT_STARTING_LEVEL: String = "Start"


        // Static Methods
        public fun fromDirectory(dir: Path): World {
            val world = World()

            // Basic gist: If we are working with a directory, see if it has a subdirectory with levels in it.  If so,
            //  go through each file in that subdirectory and load a level from it, if possible
            if (Files.isDirectory(dir)) {
                val levelDir = dir.resolve(Level.LEVEL_FOLDER)

                if (Files.exists(levelDir) && Files.isDirectory(levelDir)) {
                    // Set the world's properties
                    val configFile = dir.resolve(WORLD_FILE_NAME)
                    val reader = JsonReader()
                    val contents: String
                    try {
                        contents = String(Files.readAllBytes(configFile), StandardCharsets.UTF_8)
                        val value = reader.parse(contents)

                        // Name
                        if (value.has(NAME_KEY)) {
                            world.name = value.getString(NAME_KEY)
                        } else {
                            world.name = DEFAULT_WORLD_NAME
                        }

                        // Starting Level
                        if (value.has(STARTING_LEVEL_KEY)) {
                            world.startingLevelName = value.getString(STARTING_LEVEL_KEY)
                        } else {
                            world.startingLevelName = DEFAULT_STARTING_LEVEL
                        }
                    } catch (ex: Exception) {
                        // Set default properties
                        world.name = DEFAULT_WORLD_NAME
                        world.startingLevelName = DEFAULT_STARTING_LEVEL
                    }


                    // Loop through all levels in the directory
                    try {
                        Files.newDirectoryStream(levelDir).use { paths ->
                            paths.forEach { file ->
                                if (FileUtils.getExtension(file).equals(Level.LEVEL_EXTENSION, ignoreCase = true)) {
                                    val map = EuropaGame.game.assetManager!!.get(file.toString(), javaClass<TiledMap>())
                                    val name = file.getFileName().toString().replace("." + Level.LEVEL_EXTENSION, "")
                                    world.addLevel(Level(name, map, world))
                                }
                            }
                        }
                    } catch (ex: IOException) {
                    }

                }
            }

            return world
        }
    }
}
