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

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

/**

 * @author Nathan Templon
 */
public class EmergenceAssetManager : AssetManager() {

    // TODO: Handle Relative Paths Automatically

    init {
        // Support for tmx map loading
        this.setLoader(javaClass<TiledMap>(), TmxMapLoader())
    }


    // Public Methods
    public fun loadInternalResources() {
        this.loadResourcesRecursive(FileLocations.ASSET_DIRECTORY)
    }


    // Custom AssetManager Methods
    synchronized override fun <T> get(fileName: String): T {
        // Replace "\\" with "/" to match libGDX internal file naming
        return super.get<T>(FileUtils.crossPlatformFilePath(fileName))
    }

    synchronized override fun <T> get(fileName: String, type: Class<T>): T {
        // Replace "\\" with "/" to match libGDX internal file naming
        return super.get(FileUtils.crossPlatformFilePath(fileName), type)
    }


    // Private Methods
    private fun loadResourcesRecursive(directory: Path) {
        try {
            Files.newDirectoryStream(directory).use { subpaths ->
                subpaths.forEach { subpath ->
                    if (Files.isDirectory(subpath)) {
                        this.loadResourcesRecursive(subpath)
                    } else {
                        val extension = FileUtils.getExtension(subpath)
                        when {
                            MAP_EXTENSIONS.contains(extension) -> this.load(subpath.toString(), javaClass<TiledMap>())
                            FONT_EXTENSIONS.contains(extension) -> this.load(subpath.toString(), javaClass<BitmapFont>())
                            ATLAS_EXTENSIONS.contains(extension) -> this.load(subpath.toString(), javaClass<TextureAtlas>())
                            SKIN_EXTENSIONS.contains(extension) -> this.load(subpath.toString(), javaClass<Skin>())
                            TEXTURE_EXTENSIONS.contains(extension) -> this.load(subpath.toString(), javaClass<Texture>())
                        }
                    }
                }
            }
        } catch (ex: IOException) {
        }
    }

    companion object {
        // Constants
        public val ICON_ATLAS_NAME: String = "Icons.atlas"
        public val HUD_ATLAS_NAME: String = "Hud.atlas"

        public val ATLAS_EXTENSIONS: Array<String> = arrayOf("atlas", "ATLAS")
        public val MAP_EXTENSIONS: Array<String> = arrayOf("tmx", "TMX")
        public val FONT_EXTENSIONS: Array<String> = arrayOf("fnt", "FNT")
        public val SKIN_EXTENSIONS: Array<String> = arrayOf("skin", "SKIN")
        public val TEXTURE_EXTENSIONS: Array<String> = arrayOf("png", "PNG")
    }

}
