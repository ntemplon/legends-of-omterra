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

package com.jupiter.europa.io

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A static enumeration of file locations for Legends of Omterra

 * @author Nathan Templon
 */
public object FileLocations {

    // Constants
    public val ASSET_DIRECTORY: Path = getRootDirectory().resolve("data")// Paths.get("./data");

    public val WORLD_DIRECTORY: Path = ASSET_DIRECTORY.resolve("worlds")

    public val AI_DIRECTORY: Path = ASSET_DIRECTORY.resolve("ai")
    public val UI_DIRECTORY: Path = ASSET_DIRECTORY.resolve("ui")
    public val FONTS_DIRECTORY: Path = UI_DIRECTORY.resolve("fonts")
    public val SKINS_DIRECTORY: Path = UI_DIRECTORY.resolve("skins")
    public val UI_IMAGES_DIRECTORY: Path = UI_DIRECTORY.resolve("img")

    public val GRAPHICS_DIRECTORY: Path = ASSET_DIRECTORY.resolve("graphics")
    public val SPRITES_DIRECTORY: Path = GRAPHICS_DIRECTORY.resolve("sprites")

    public val AUDIO_DIRECTORY: Path = ASSET_DIRECTORY.resolve("audio")

    private val USER_HOME = Paths.get(System.getProperty("user.home"))
    private val LOCAL_DATA_DIRECTORY = USER_HOME.resolve(".omterra")
    public val SAVE_DIRECTORY: Path = LOCAL_DATA_DIRECTORY.resolve("saves")
    public val CONFIGURATION_DIRECTORY: Path = LOCAL_DATA_DIRECTORY.resolve("config")

    public val CHARACTER_SPRITES: String = SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString()
    public val HUD_ATLAS: String = SPRITES_DIRECTORY.resolve("Hud.atlas").toString()
    public val ICON_ATLAS: String = SPRITES_DIRECTORY.resolve("Icons.atlas").toString()


    // Static Initialization
    private fun getRootDirectory(): Path {
        try {
            val jar = Paths.get(javaClass<FileLocations>().getProtectionDomain().getCodeSource().getLocation().toURI())
            var folder = jar.getParent()

            // If the folder contains a "data" subdirectory, then we are good.  Else, we need to find it.
            val dataPath = folder.resolve("data")
            if (!Files.exists(dataPath)) {
                val folderString = folder.toString()
                folder = Paths.get(folderString.substring(0, folderString.indexOf("core") + 4))
            }

            return folder
        } catch (ex: Exception) {
            println("Exception: " + ex.javaClass.getCanonicalName())
            return Paths.get(".")
        }
    }

    init {
        val fields = javaClass<FileLocations>().getDeclaredFields()
        for (field in fields) {
            if (javaClass<Path>().isAssignableFrom(field.getType()) && java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    val file = field.get(null) as Path
                    if (!Files.exists(file)) {
                        Files.createDirectories(file.getParent())
                    }
                } catch (ex: IllegalArgumentException) {
                } catch (ex: IllegalAccessException) {
                } catch (ex: IOException) {
                } finally {
                }
            }
        }
    }

}
